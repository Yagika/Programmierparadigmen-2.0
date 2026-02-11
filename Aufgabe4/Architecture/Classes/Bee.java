package Architecture.Classes;

import Architecture.Iterator.EmptyIterator;
import Architecture.Iterator.SingleElementIterator;
import Architecture.interfaces.Pollinator;

import java.util.Date;
import java.util.Iterator;


/**
 * Beobachtung einer Biene irgendeiner Art.
 * Untertyp von Wasp und Pollinator.
 *
 * Zusicherung: social() liefert einen Iterator, der genau diese Beobachtung enthält,
 * da die Art immer sozial lebt.
 */
public abstract class Bee extends Wasp implements Pollinator {

    /**
     * Bee is necessarily a subtype of Wasp. this is again an abstract class because it wouldn't make sense
     * to implement each method again and again. Also every Bee is a Pollinator.
     */

    protected int chip;

    /**
     * sameBee(), should return an Iterator of all Observation of the same individual Bee (this specific Observation.)
     * ordered from earliest Observation Date to latest.
     */
    public Iterator<?> sameBee() {
        return new SingleElementIterator<>(this);
    }

    /**
     * same as above, but this time reverse the order from latest to earliest.
     */
    public Iterator<?> sameBee(boolean reverse) {
        return new SingleElementIterator<>(this);
    }

    /**
     * only returns the Iterator sorted from earliest Observation Date to latest in this specific time frame.
     */
    public Iterator<?> sameBee(Date from, Date to) {
        if (date == null || from == null || to == null) {
            return EmptyIterator.instance();
        }
        if (!date.before(from) && !date.after(to)) {
            return new SingleElementIterator<>(this);
        }
        return EmptyIterator.instance();
    }
}
