import java.io.*;

/**
 * Child process entry point.
 * Reads Params from stdin, runs BA, writes Result to stdout.
 */
public class BAProcessMain {
    public static void main(String[] args) throws Exception {
        DataInputStream in = new DataInputStream(new BufferedInputStream(System.in));
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(System.out));

        BAIPC.Params P = BAIPC.Params.readFrom(in);

        ParallelBeesAlgorithm algo = new ParallelBeesAlgorithm();
        BAIPC.Result R = algo.run(P);

        R.writeTo(out);
        out.flush();
    }
}
