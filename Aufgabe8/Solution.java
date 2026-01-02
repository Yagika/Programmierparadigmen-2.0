import java.util.Arrays;

/**
 * A candidate solution: location + objective function value.
 */
public class Solution {
    private final Location location;
    private final double result;

    public Solution(Location location, double result) {
        this.location = location;
        this.result = result;
    }

    public Location getLocation() { return location; }
    public double getResult() { return result; }

    @Override
    public String toString() {
        return String.format("Result: %.6f at %s", result, Arrays.toString(location.getCoordinates()));
    }
}
