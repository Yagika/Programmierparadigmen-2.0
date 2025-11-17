package Architecture.Classes;

import Architecture.interfaces.SolitaryBee;
import Architecture.Iterator.EmptyIterator;
import Architecture.Iterator.SingleElementIterator;

import java.util.Date;
import java.util.Iterator;

/**
 * Beobachtung einer Osmia cornuta (gehörnte Mauerbiene).
 * Solitäre Wildbiene.
 */
public class OsmiaCornuta extends Bee implements SolitaryBee {
    /*
    It only exhibits a solitary style as per document.
     */

    /**
     * it is not apparent from the document if this Bee is also able to be bred/kept like the
     * Bumblebee is.
     */
    private final boolean isWild;

    /**
     * Constructor without isWild
     */
    public OsmiaCornuta(String description, Date date, int time) {
        this(description, date, time, false);
    }

    /**
     * Constructor with isWild
     */
    public OsmiaCornuta(String description, Date date, int time, boolean isWild) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.isWild = isWild;
        this.removed = false;
    }


    @Override
    public Iterator<?> solitary() {
        return new SingleElementIterator<>(this);
    }

    @Override
    public Iterator<?> wild(boolean isWild) {
        if (this.isWild != isWild) {
            return EmptyIterator.instance();
        }
        return new SingleElementIterator<>(this);
    }
}

