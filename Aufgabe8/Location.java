import java.util.Arrays;

/**
 * Immutable location wrapper.
 */
public class Location {
    private final double[] coordinates;

    public Location(double[] coordinates) {
        // Optional: defensive copy to prevent external mutation.
        this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
    }

    public double[] getCoordinates() {
        // Optional: defensive copy to keep immutability strict.
        return Arrays.copyOf(coordinates, coordinates.length);
    }
}
