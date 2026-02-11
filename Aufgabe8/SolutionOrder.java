import java.util.Comparator;

/**
 * Correct ordering + "better" logic for MIN/MAX/ZEROS without floating-equality tricks.
 */
public final class SolutionOrder {
    private SolutionOrder(){}

    public static Comparator<Solution> comparator(ComparisonId cId) {
        switch (cId) {
            case MIN:
                return Comparator.comparingDouble(Solution::getResult);
            case MAX:
                return (a, b) -> Double.compare(b.getResult(), a.getResult());
            case ZEROS:
                return Comparator.comparingDouble(s -> Math.abs(s.getResult()));
            default:
                throw new IllegalArgumentException("Unknown ComparisonId");
        }
    }

    public static boolean better(ComparisonId cId, double best, double cand) {
        switch (cId) {
            case MIN:   return cand < best;
            case MAX:   return cand > best;
            case ZEROS: return Math.abs(cand) < Math.abs(best);
            default:
                throw new IllegalArgumentException("Unknown ComparisonId");
        }
    }
}
