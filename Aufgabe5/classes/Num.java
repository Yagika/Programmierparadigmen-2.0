package classes;

import interfaces.Modifiable;

/**
 * Simple immutable integer wrapper used for testing.
 */
public class Num implements Modifiable<Num, Num> {

    private final int value;

    /**
     * Creates a new Num with the given integer value.
     */
    public Num(int value) {
        this.value = value;
    }

    @Override
    public Num add(Num other) {
        if (other == null) {
            return this;
        }
        return new Num(this.value + other.value);
    }

    @Override
    public Num subtract(Num other) {
        if (other == null) {
            return this;
        }
        return new Num(this.value - other.value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
