package classes;

/**
 * Base class for all bee observations.
 * Stores a short textual description. Immutable.
 */
public class Bee {

    private final String description;

    /**
     * Creates a new bee observation with the given description.
     *
     * @param description description of the observation (must not be null)
     */
    public Bee(String description) {
        this.description = description;
    }

    /**
     * Returns the description of observation.
     */
    protected String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Bee[" + description + "]";
    }
}
