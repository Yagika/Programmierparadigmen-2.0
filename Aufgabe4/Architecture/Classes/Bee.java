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
     * Observation of a Bee. must be an abstract class because just "Bee" is not specific.
     * As per the document, A Bee Observation might be that of the exact same Bee that was already Observed.
     * This means if information is present that it's the same Bee.
     * -If it is chipped: print (?) out its number
     * -If Observation already exsists: print (?) out its earliest observed date
     * The document leaves this up to interpretation I guess?
     * I honestly don't know what they mean, probably instead of creating a new object return the old one.
     * Todo: find out what they mean with this and implement it
     */

    protected int chip;

    /**
     * Todo: implement logic for Iterator
     * sameBee(), should return an Iterator of all Observation of the same individual Bee (this specific Observation.)
     * ordered from earliest Observation Date to latest.
     */
    public Iterator<?> sameBee() {
        return new SingleElementIterator<>(this);
    }

    /**
     * Todo: implement logic
     * same as above, but this time reverse the order from latest to earliest.
     */
    public Iterator<?> sameBee(boolean reverse) {
        return new SingleElementIterator<>(this);
    }

    /**
     * Todo: implement logic
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
