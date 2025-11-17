package Architecture.Classes;

import Architecture.interfaces.Observation;
import Architecture.Iterator.EmptyIterator;

import java.util.Date;
import java.util.Iterator;

/**
 * Eine Beobachtung eines Tiers einer Wespenart.
 * Jede Biene ist eine Wesp (Unterfamilie der Stechimmen), daher ist Bee ein Untertyp von Wasp.
 */
public abstract class Wasp implements Observation {

    /**
     * This class is abstract because it doesn't count as an Observation, since it is
     * just an overreaching term describing bugs with stingers.
     * This could technically also be an Interface, but it wouldn't make sense to
     * implement all the methods for each new class we want to add.
     * All Bees are Wasps, but not all Wasps are bees.
     */
    protected Date date;
    protected int time;
    protected String description;
    protected boolean removed;

    /**
     * Implement getter Methods:
     */
    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public int getTime() {
        return time;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Marks this Observation as removed.
     */
    @Override
    public void remove() {
        removed = true;
    }

    /**
     * Checks if Observation is removed.
     */
    @Override
    public boolean valid() {
        return !removed;
    }

    /**
     * Todo: Implement logic for Iterator.
     */
    @Override
    public Iterator<?> earlier() {
        return EmptyIterator.instance();
    }

    /**
     * Todo: Implement logic for Iterator.
     */
    @Override
    public Iterator<?> later() {
        return EmptyIterator.instance();
    }
}
