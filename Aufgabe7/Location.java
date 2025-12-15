// Represents a multi-dimensional point in the search space (the argument vector)
public class Location {
    private final double[] coordinates;

    public Location(double[] coordinates) {this.coordinates = coordinates;}

    public double[] getCoordinates() {
        return coordinates;
    }
}