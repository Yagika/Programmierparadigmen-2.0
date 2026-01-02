import java.io.*;
import java.util.*;

/**
 * Binary IPC protocol between parent JVM (ExecuteBA) and child JVM (BAProcessMain).
 * Uses DataInput/DataOutput to keep overhead low.
 */
public final class BAIPC {

    /** Parameters sent from parent to child. */
    public static final class Params {
        public int a, t, n, m, e, p, q, r;
        public int b, k;
        public double s;
        public long seed;
        public FunctionId fId;
        public ComparisonId cId;
        public double[][] bounds;

        /** Writes parameters in a stable order. */
        public void writeTo(DataOutput out) throws IOException {
            out.writeInt(a); out.writeInt(t);
            out.writeInt(n); out.writeInt(m); out.writeInt(e);
            out.writeInt(p); out.writeInt(q); out.writeInt(r);
            out.writeInt(b); out.writeInt(k);
            out.writeDouble(s);
            out.writeLong(seed);
            out.writeInt(fId.ordinal());
            out.writeInt(cId.ordinal());

            out.writeInt(bounds.length);
            for (int i = 0; i < bounds.length; i++) {
                out.writeDouble(bounds[i][0]);
                out.writeDouble(bounds[i][1]);
            }
        }

        /** Reads parameters in the same order as writeTo(). */
        public static Params readFrom(DataInput in) throws IOException {
            Params P = new Params();
            P.a = in.readInt(); P.t = in.readInt();
            P.n = in.readInt(); P.m = in.readInt(); P.e = in.readInt();
            P.p = in.readInt(); P.q = in.readInt(); P.r = in.readInt();
            P.b = in.readInt(); P.k = in.readInt();
            P.s = in.readDouble();
            P.seed = in.readLong();
            P.fId = FunctionId.values()[in.readInt()];
            P.cId = ComparisonId.values()[in.readInt()];

            int dim = in.readInt();
            P.bounds = new double[dim][2];
            for (int i = 0; i < dim; i++) {
                P.bounds[i][0] = in.readDouble();
                P.bounds[i][1] = in.readDouble();
            }
            return P;
        }
    }

    /** Result sent from child back to parent. */
    public static final class Result {
        public long evalCount;
        public long millis;
        public List<Solution> top;

        public void writeTo(DataOutput out) throws IOException {
            out.writeLong(evalCount);
            out.writeLong(millis);
            out.writeInt(top.size());
            for (Solution s : top) {
                double[] x = s.getLocation().getCoordinates();
                out.writeInt(x.length);
                for (double v : x) out.writeDouble(v);
                out.writeDouble(s.getResult());
            }
        }

        public static Result readFrom(DataInput in) throws IOException {
            Result R = new Result();
            R.evalCount = in.readLong();
            R.millis = in.readLong();
            int cnt = in.readInt();

            ArrayList<Solution> list = new ArrayList<>(cnt);
            for (int i = 0; i < cnt; i++) {
                int d = in.readInt();
                double[] x = new double[d];
                for (int j = 0; j < d; j++) x[j] = in.readDouble();
                double fx = in.readDouble();
                list.add(new Solution(new Location(x), fx));
            }
            R.top = list;
            return R;
        }
    }

    private BAIPC() {}
}
