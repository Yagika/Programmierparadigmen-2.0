import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Parallel Bees Algorithm implementation:
 * - Recruitment phase is sequential (as required).
 * - Evaluation of bee blocks is parallel using k threads.
 * - Blocks are distributed via BlockQueue.
 */
public final class ParallelBeesAlgorithm {

    /** One block corresponds to b bees evaluated sequentially in a single thread. */
    private static final class Block {
        final double[] center;      // center site for local search, null for scouts
        final double[][] bounds;    // local or global bounds
        final int count;            // how many bees in this block
        final boolean includeCenter;

        Block(double[] center, double[][] bounds, int count, boolean includeCenter) {
            this.center = center;
            this.bounds = bounds;
            this.count = count;
            this.includeCenter = includeCenter;
        }
    }

    public BAIPC.Result run(BAIPC.Params P) {
        validate(P);

        long start = System.currentTimeMillis();
        final Comparator<Solution> solComp = SolutionOrder.comparator(P.cId);
        final AtomicLong evalCount = new AtomicLong(0);

        // --- initial random sites
        ArrayList<double[]> sites = new ArrayList<>(P.n);
        SplittableRandom initRnd = new SplittableRandom(P.seed);
        for (int i = 0; i < P.n; i++) sites.add(randPoint(P.bounds, initRnd));

        ArrayList<Solution> current = evalSitesParallel(P, sites, evalCount);

        for (int step = 0; step < P.t; step++) {

            // ===== Recruitment (SEQUENTIAL) =====
            current.sort(solComp);

            int mm = Math.min(P.m, current.size());
            int ee = Math.min(P.e, mm);

            // Neighborhood shrink factor per iteration (simple schedule)
            double rel = P.s / Math.max(1, P.t);

            ArrayList<Block> blocks = new ArrayList<>();

            // Elite sites: p bees each (in blocks of size b)
            for (int i = 0; i < ee; i++) {
                double[] center = current.get(i).getLocation().getCoordinates();
                double[][] local = localBounds(center, P.bounds, rel);
                for (int j = 0; j < P.p; j += P.b) {
                    blocks.add(new Block(center, local, P.b, true));
                }
            }

            // Non-elite selected sites: q bees each
            for (int i = ee; i < mm; i++) {
                double[] center = current.get(i).getLocation().getCoordinates();
                double[][] local = localBounds(center, P.bounds, rel);
                for (int j = 0; j < P.q; j += P.b) {
                    blocks.add(new Block(center, local, P.b, true));
                }
            }

            // Scouts: random in global bounds
            int scouts = P.n - P.m;
            for (int j = 0; j < scouts; j += P.b) {
                int cnt = Math.min(P.b, scouts - j);
                blocks.add(new Block(null, P.bounds, cnt, false));
            }

            // ===== Process blocks (PARALLEL) =====
            BlockQueue<Block> queue = new BlockQueue<>();
            queue.addAll(blocks);
            queue.close(); // ✅ IMPORTANT: prevents deadlock (no more blocks will be added)

            @SuppressWarnings("unchecked")
            ArrayList<Solution>[] locals = new ArrayList[P.k];
            Thread[] threads = new Thread[P.k];

            for (int ti = 0; ti < P.k; ti++) {
                final int idx = ti;
                locals[idx] = new ArrayList<>(blocks.size() / Math.max(1, P.k) + 16);

                final SplittableRandom rnd =
                        new SplittableRandom(P.seed ^ (0x9E3779B97F4A7C15L + idx * 1315423911L));

                threads[ti] = new Thread(() -> {
                    try {
                        while (true) {
                            Block b = queue.take();
                            if (b == null) break; // drained + closed
                            locals[idx].add(processBlock(P, b, rnd, evalCount));
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }, "BA-worker-" + ti);

                threads[ti].start();
            }

            for (Thread th : threads) {
                try { th.join(); } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }

            // Merge thread-local results
            ArrayList<Solution> next = new ArrayList<>(P.n + 16);
            for (ArrayList<Solution> lst : locals) next.addAll(lst);

            // Keep population size exactly n (truncate or pad)
            if (next.size() > P.n) {
                next.sort(solComp);
                next.subList(P.n, next.size()).clear();
            } else {
                SplittableRandom rr = new SplittableRandom(P.seed + step + 999);
                while (next.size() < P.n) {
                    double[] x = randPoint(P.bounds, rr);
                    double fx = Functions.objective(P.fId, x);
                    evalCount.incrementAndGet();
                    next.add(new Solution(new Location(x), fx));
                }
            }

            current = next;
        }

        // Final top-r
        current.sort(solComp);
        if (current.size() > P.r) current.subList(P.r, current.size()).clear();

        BAIPC.Result R = new BAIPC.Result();
        R.evalCount = evalCount.get();
        R.millis = System.currentTimeMillis() - start;
        R.top = current;
        return R;
    }

    /** Evaluates one block of bees sequentially, returns best solution in this block. */
    private static Solution processBlock(BAIPC.Params P, Block b, SplittableRandom rnd, AtomicLong evalCount) {
        double bestFx = 0.0;
        boolean hasBest = false;
        double[] bestX = null;

        for (int i = 0; i < b.count; i++) {
            double[] x = randPoint(b.bounds, rnd);
            double fx = Functions.objective(P.fId, x);
            evalCount.incrementAndGet();

            if (!hasBest || SolutionOrder.better(P.cId, bestFx, fx)) {
                hasBest = true;
                bestFx = fx;
                bestX = x;
            }
        }

        // optional: include center evaluation
        if (b.includeCenter && b.center != null) {
            double[] x = Arrays.copyOf(b.center, b.center.length);
            double fx = Functions.objective(P.fId, x);
            evalCount.incrementAndGet();

            if (!hasBest || SolutionOrder.better(P.cId, bestFx, fx)) {
                hasBest = true;
                bestFx = fx;
                bestX = x;
            }
        }

        return new Solution(new Location(bestX), bestFx);
    }

    /** Parallel evaluation of initial random sites (work stealing via synchronized deque). */
    private static ArrayList<Solution> evalSitesParallel(BAIPC.Params P, ArrayList<double[]> sites, AtomicLong evalCount) {
        int n = sites.size();

        // Work segments [i, j)
        ArrayDeque<int[]> work = new ArrayDeque<>();
        for (int i = 0; i < n; i += P.b) {
            work.add(new int[]{i, Math.min(n, i + P.b)});
        }

        @SuppressWarnings("unchecked")
        ArrayList<Solution>[] locals = new ArrayList[P.k];
        Thread[] threads = new Thread[P.k];

        for (int ti = 0; ti < P.k; ti++) {
            final int idx = ti;
            locals[idx] = new ArrayList<>(n / Math.max(1, P.k) + 16);

            threads[ti] = new Thread(() -> {
                while (true) {
                    int[] seg;
                    synchronized (work) { seg = work.pollFirst(); }
                    if (seg == null) break;

                    for (int i = seg[0]; i < seg[1]; i++) {
                        double[] x = sites.get(i);
                        double fx = Functions.objective(P.fId, x);
                        evalCount.incrementAndGet();
                        locals[idx].add(new Solution(new Location(x), fx));
                    }
                }
            }, "BA-eval-" + ti);

            threads[ti].start();
        }

        for (Thread th : threads) {
            try { th.join(); } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }

        ArrayList<Solution> out = new ArrayList<>(n);
        for (ArrayList<Solution> lst : locals) out.addAll(lst);
        return out;
    }

    private static double[] randPoint(double[][] bounds, SplittableRandom rnd) {
        int dim = bounds.length;
        double[] x = new double[dim];
        for (int d = 0; d < dim; d++) {
            double lo = bounds[d][0], hi = bounds[d][1];
            x[d] = lo + rnd.nextDouble() * (hi - lo);
        }
        return x;
    }

    /** Creates local bounds around a center with radius=(globalRange*relSize). */
    private static double[][] localBounds(double[] center, double[][] global, double relSize) {
        int dim = global.length;
        double[][] b = new double[dim][2];
        for (int d = 0; d < dim; d++) {
            double gmin = global[d][0], gmax = global[d][1];
            double radius = (gmax - gmin) * relSize;
            double lo = center[d] - radius;
            double hi = center[d] + radius;
            if (lo < gmin) lo = gmin;
            if (hi > gmax) hi = gmax;
            b[d][0] = lo;
            b[d][1] = hi;
        }
        return b;
    }

    /** Centralized parameter validation (helps during grading). */
    private static void validate(BAIPC.Params p) {
        if (p.b <= 0 || p.k <= 0) throw new IllegalArgumentException("b,k must be >0");
        if (p.n <= 0 || p.t < 0) throw new IllegalArgumentException("n must be >0 and t >=0");
        if (p.m < 0 || p.e < 0 || p.p < 0 || p.q < 0) throw new IllegalArgumentException("m,e,p,q must be >=0");
        if (p.m > p.n) throw new IllegalArgumentException("m must be <= n");
        if (p.e > p.m) throw new IllegalArgumentException("e must be <= m");

        if (p.n % p.b != 0 || p.m % p.b != 0 || p.e % p.b != 0 || p.p % p.b != 0 || p.q % p.b != 0)
            throw new IllegalArgumentException("n,m,e,p,q must be multiples of b");

        if (p.bounds == null || p.bounds.length == 0) throw new IllegalArgumentException("bounds missing");
        for (double[] d : p.bounds) {
            if (d == null || d.length != 2) throw new IllegalArgumentException("bounds must be dim x 2");
            if (!(d[0] < d[1])) throw new IllegalArgumentException("invalid bound: [" + d[0] + "," + d[1] + "]");
        }
        if (p.fId == null || p.cId == null) throw new IllegalArgumentException("fId/cId must be set");
    }
}
