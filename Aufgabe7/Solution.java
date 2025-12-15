import java.util.Arrays;

public class Solution {
    private final Location location;
    private final double result;

    public Solution(Location location, double result) {
        this.location = location;
        this.result = result;
    }

    public Location getLocation() { return location; }
    public double getResult() { return result; }

    // Optional: for better debug output
    @Override
    public String toString() {
        return String.format("Result: %.4f at %s", result, Arrays.toString(location.getCoordinates()));
    }
}