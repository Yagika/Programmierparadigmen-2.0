import java.util.*;
import java.io.*;

public class Test {

    /*
     * Work Distribution:
     * Aleksandr: ParallelBeesAlgorithm (blocks + threads)
     * Yana: IPC (BAIPC) + processes (ExecuteBA / BAProcessMain)
     * Dominik: Test parametrization + function choices + output
     */

    public static void main(String[] args) throws Exception {
        long startAll = System.currentTimeMillis();

        BAIPC.Params base = new BAIPC.Params();
        base.a = 2;

        base.t = 250;
        base.n = 120;
        base.m = 40;
        base.e = 20;
        base.p = 40;
        base.q = 20;
        base.r = 10;
        base.s = 0.35;
        base.seed = 1234567L;

        base.fId = FunctionId.HEAVY_MULTI;
        base.cId = ComparisonId.MIN;

        double[][] global = new double[][] {
                {-6.0, 6.0},
                {-6.0, 6.0}
        };

        System.out.println("=== Test 1: 4 Prozesse, k=1 ===");
        runCase(base, global, 4, 1, 10);

        System.out.println("\n=== Test 2: 1 Prozess, k=6 ===");
        runCase(base, global, 1, 6, 10);

        System.out.println("\n=== Test 3: 2 Prozesse, k=3 ===");
        runCase(base, global, 2, 3, 10);

        long endAll = System.currentTimeMillis();
        System.out.printf(Locale.US, "%nTOTAL wall time: %.3f s%n", (endAll - startAll) / 1000.0);
    }

    private static void runCase(BAIPC.Params base, double[][] global, int processes, int k, int b) throws Exception {
        BAIPC.Params p = copy(base);
        p.k = k;
        p.b = b;

        // validate block constraints early (assignment requirement)
        if (p.n % b != 0 || p.m % b != 0 || p.e % b != 0 || p.p % b != 0 || p.q % b != 0) {
            throw new IllegalArgumentException("n,m,e,p,q must be multiples of b");
        }

        double[][][] wPerProc = splitBounds(processes, global);

        long t0 = System.currentTimeMillis();
        List<Solution> all = ExecuteBA.executeAll(p, wPerProc);
        long wall = System.currentTimeMillis() - t0;

        Comparator<Solution> comp = SolutionOrder.comparator(p.cId);

        all.sort(comp);
        if (all.size() > p.r) all = all.subList(0, p.r);

        System.out.printf(Locale.US, "proc=%d, k=%d, b=%d | wall=%d ms | top=%d%n",
                processes, k, b, wall, all.size());

        for (int i = 0; i < all.size(); i++) {
            Solution s = all.get(i);
            double[] x = s.getLocation().getCoordinates();
            System.out.printf(Locale.US, "  #%d x=(%.5f, %.5f)  f=%.8f%n",
                    i + 1, x[0], x[1], s.getResult());
        }
    }

    private static double[][][] splitBounds(int proc, double[][] global) {
        int dim = global.length;
        double lo = global[0][0], hi = global[0][1];
        double step = (hi - lo) / proc;

        double[][][] out = new double[proc][dim][2];

        for (int i = 0; i < proc; i++) {
            for (int d = 0; d < dim; d++) {
                out[i][d][0] = global[d][0];
                out[i][d][1] = global[d][1];
            }
            out[i][0][0] = lo + i * step;
            out[i][0][1] = (i == proc - 1) ? hi : (lo + (i + 1) * step);
        }
        return out;
    }

    private static BAIPC.Params copy(BAIPC.Params x) {
        BAIPC.Params p = new BAIPC.Params();
        p.a = x.a; p.t = x.t;
        p.n = x.n; p.m = x.m; p.e = x.e;
        p.p = x.p; p.q = x.q; p.r = x.r;
        p.b = x.b; p.k = x.k;
        p.s = x.s;
        p.seed = x.seed;
        p.fId = x.fId;
        p.cId = x.cId;
        p.bounds = x.bounds;
        return p;
    }
}
