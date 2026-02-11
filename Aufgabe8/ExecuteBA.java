import java.io.*;
import java.util.*;

/**
 * Parent-side launcher:
 * - starts child JVM processes
 * - sends Params (including per-process bounds)
 * - reads Results
 * - merges solutions
 */
public final class ExecuteBA {

    public static List<Solution> executeAll(BAIPC.Params base, double[][][] wPerProc) throws IOException {
        int proc = wPerProc.length;

        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String cp = System.getProperty("java.class.path");

        Process[] ps = new Process[proc];
        DataOutputStream[] childIn = new DataOutputStream[proc];
        DataInputStream[] childOut = new DataInputStream[proc];

        // Start children
        for (int i = 0; i < proc; i++) {
            ProcessBuilder pb = new ProcessBuilder(javaBin, "-cp", cp, "BAProcessMain");
            pb.redirectError(ProcessBuilder.Redirect.INHERIT); // stderr visible for debugging
            ps[i] = pb.start();

            childIn[i] = new DataOutputStream(new BufferedOutputStream(ps[i].getOutputStream()));
            childOut[i] = new DataInputStream(new BufferedInputStream(ps[i].getInputStream()));
        }

        // Send params
        for (int i = 0; i < proc; i++) {
            BAIPC.Params P = copy(base);
            P.bounds = wPerProc[i];
            P.seed = base.seed + i * 99991L; // make seeds different across processes

            P.writeTo(childIn[i]);
            childIn[i].flush();
            childIn[i].close(); // EOF: important to let child know input ended
        }

        // Read results
        ArrayList<Solution> merged = new ArrayList<>();
        for (int i = 0; i < proc; i++) {
            BAIPC.Result r = BAIPC.Result.readFrom(childOut[i]);
            merged.addAll(r.top);
            childOut[i].close();
        }

        // Wait children + verify exit code
        for (int i = 0; i < proc; i++) {
            try { ps[i].waitFor(); } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            if (ps[i].exitValue() != 0) {
                throw new IOException("Child process " + i + " failed, exit=" + ps[i].exitValue());
            }
        }

        return merged;
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
        p.bounds = x.bounds; // note: overwritten per process
        return p;
    }

    private ExecuteBA() {}
}
