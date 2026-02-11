public class Bounds {
    private final double[][] bounds;
    private final int dimension; // a - number of parameters

    public Bounds(double [][] bounds) {
        this.bounds = bounds;
        this.dimension = bounds.length;
    }

    public int getDimension() {return dimension;}

    public double getMin(int dimensionIndex) {return bounds[dimensionIndex][0];}

    public double getMax(int dimensionIndex) {return bounds[dimensionIndex][1];}
}
