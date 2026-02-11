package classes;

import interfaces.Modifiable;

/**
 * Observation of a honey bee.
 * Has a description and a 'kind' string.
 */
public class HoneyBee extends Bee implements Modifiable<String, HoneyBee> {

    private final String kind;

    /**
     * Creates a new honey bee observation.
     *
     * @param description textual description of the observation
     * @param kind        species or breed string
     */
    public HoneyBee(String description, String kind) {
        super(description);
        this.kind = kind;
    }

    /**
     * Returns the species string
     */
    public String sort() {
        return kind;
    }

    @Override
    public HoneyBee add(String suffix) {
        if (suffix == null || suffix.isEmpty()) {
            return this;
        }
        return new HoneyBee(getDescription(), kind + suffix);
    }

    @Override
    public HoneyBee subtract(String part) {
        if (part == null || part.isEmpty()) {
            return this;
        }
        String newKind = kind.replace(part, "");
        if (newKind.equals(kind)) {
            return this;
        }
        return new HoneyBee(getDescription(), newKind);
    }

    @Override
    public String toString() {
        return "HoneyBee[description=" + getDescription()
                + ", kind=" + kind + "]";
    }
}
