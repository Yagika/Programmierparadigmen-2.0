package classes;

import interfaces.Modifiable;

/**
 * Observation of a wild bee.
 * Stores a description and an estimated body length.
 */
public class WildBee extends Bee implements Modifiable<Integer, WildBee> {

    private final int length; // mm

    /**
     * Creates a new wild bee observation.
     *
     * @param description textual description of the observation
     * @param length      estimated length in millimetres (must be > 0)
     */
    public WildBee(String description, int length) {
        super(description);
        this.length = length;
    }

    /**
     * Returns the length in mm.
     */
    public int length() {
        return length;
    }

    @Override
    public WildBee add(Integer x) {
        if (x == null || x <= 0) {
            return this;
        }
        return new WildBee(getDescription(), length + x);
    }

    @Override
    public WildBee subtract(Integer x) {
        if (x == null || x <= 0 || x >= length) {
            return this;
        }
        return new WildBee(getDescription(), length - x);
    }

    @Override
    public String toString() {
        return "WildBee[description=" + getDescription()
                + ", length=" + length + "mm]";
    }
}
