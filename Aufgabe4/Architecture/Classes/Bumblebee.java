package Architecture.Classes;

import Architecture.Iterator.EmptyIterator;
import Architecture.Iterator.SingleElementIterator;
import Architecture.interfaces.SocialBee;
import Architecture.interfaces.WildBee;

import java.util.Date;
import java.util.Iterator;

/**
 * Beobachtung einer Hummel.
 * Alle Hummeln sind Staatenbildner (sozial) und gelten hier als Wildbienen.
 *
 * Must be a Bee and also Social AND Wild.
 */
public class Bumblebee extends Bee implements SocialBee, WildBee {

    /*
    BumbleBees are all considered WildBees as per document description in the WildBee: section.
    also they may be bred.
     */

    private final boolean isWild;

    /**
     * Constructor if it's know to be Wild or not.
     */
    public Bumblebee(String description, Date date, int time, boolean isWild) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.isWild = isWild;
    }

    public Bumblebee(String description, Date date, int time) {
        this(description, date, time, false);
    }

    @Override
    public Iterator<?> wild(boolean isWild) {
        if (this.isWild != isWild) {
            return EmptyIterator.instance();
        }
        return new SingleElementIterator<>(this);
    }

    @Override
    public Iterator<?> social() {
        // Hummeln sind immer sozial.
        return new SingleElementIterator<>(this);
    }

}
