package interfaces;

/**
 * Generic interface for immutable objects that support a logical
 * 'add' and 'subtract' operation. Both operations return a new object.
 *
 * X – parameter type
 * T – concrete implementing type
 */
public interface Modifiable<X, T extends Modifiable<X, T>> {


    /**
     * Returns a new object representing this extended by x.
     * If extension is not possible, returns this.
     */
    T add(X x);

    /**
     * Returns a new object representing this with x removed.
     * If removal is not possible, returns this.
     */
    T subtract(X x);
}