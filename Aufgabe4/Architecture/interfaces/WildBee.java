package Architecture.interfaces;

import java.util.Iterator;
/**
 * WildBee: Beobachtung einer Wildbiene.
 */
public interface WildBee extends Observation{
    /**
     * iterator over all Observations of this type based on if its
     * bred or not.
     *
     * WildBee might also be able to be done as an Abstract Class, although the solution
     * with interfaces seems to be cleaner, because some Bees might have multiple types.
     */
    Iterator<?> wild(boolean isWild);
}
