/**
 * Simple bounds wrapper (not required by core algorithm, but convenient).
 */
public class Bounds {
    private final double[][] bounds;
    private final int dimension;

    public Bounds(double[][] bounds) {
        this.bounds = bounds;
        this.dimension = bounds.length;
    }

    public int getDimension() { return dimension; }
    public double getMin(int i) { return bounds[i][0]; }
    public double getMax(int i) { return bounds[i][1]; }

    /** WARNING: returns raw reference (do not modify externally if shared). */
    public double[][] raw() { return bounds; }
}
