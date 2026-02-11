package Architecture.Classes;

import Architecture.interfaces.Pollinator;
import Architecture.Iterator.EmptyIterator;

import java.util.Date;
import java.util.Iterator;

/**
 * Beobachtung einer Schwebfliege (FlowerFly).
 * Sie ist ein Pollinator, aber keine Wasp und keine Bee.
 */
public class FlowerFly implements Pollinator {
    /**
     * FlowerFly is just an Observation of a Pollinator.
     */
    private Date date;
    private int time;
    private String description;
    private boolean removed = false;

    public FlowerFly(String description, Date date, int time) {
        this.description = description;
        this.date = date;
        this.time = time;
    }

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

    @Override
    public Iterator<?> earlier() {
        return EmptyIterator.instance();
    }

    @Override
    public Iterator<?> later() {
        return EmptyIterator.instance();
    }

}
