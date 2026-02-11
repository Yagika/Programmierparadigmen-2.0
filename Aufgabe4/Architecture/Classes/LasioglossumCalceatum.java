package Architecture.Classes;

import Architecture.interfaces.SocialBee;
import Architecture.interfaces.SolitaryBee;
import Architecture.Iterator.EmptyIterator;
import Architecture.Iterator.SingleElementIterator;

import java.util.Date;
import java.util.Iterator;

/**
 * Beobachtung einer Lasioglossum calceatum.
 * Art kann je nach Bedingungen sozial oder solitär leben.
 */
public class LasioglossumCalceatum extends Bee implements SocialBee, SolitaryBee {


    /**
     * This Bee is either Social or Solitary. If Solitary it also can be Wild/Bred
     */
    private final boolean isSocial;
    private final boolean isSolitary;
    private final boolean isWild;

    /**
     * Constructor, It has to either be Social or Solitary.
     * So if it is Social it cannot be solitary, if it isn't social it has to be solitary.
     */
    public LasioglossumCalceatum(String description, Date date, int time, boolean isSocial, boolean isWild) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.isSocial = isSocial;
        this.isSolitary = !isSocial;
        this.isWild = isWild;
    }


    @Override
    public Iterator<?> social() {
        if (!isSocial) {
            return EmptyIterator.instance();
        }
        return new SingleElementIterator<>(this);
    }

    @Override
    public Iterator<?> solitary() {
        if (!isSolitary) {
            return EmptyIterator.instance();
        }
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