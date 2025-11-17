package Architecture.Classes;

import Architecture.Iterator.EmptyIterator;
import Architecture.Iterator.SingleElementIterator;
import Architecture.interfaces.CommunalBee;

import java.util.Date;
import java.util.Iterator;

/**
 * Beobachtung einer Biene der Art Andrena bucephala.
 * Diese Art ist eine kommunale Wildbiene, kann aber auch solitär leben.
 */
public class AndrenaBucephala extends Bee implements CommunalBee {

    private final boolean isCommunal;
    private final boolean isSolitary;
    private final boolean isWild;

    /**
     * Constructor, but assume we always know if it's wild, communal and solitary.
     */
    public AndrenaBucephala(String description, Date date, int time, boolean isCommunal, boolean isSolitary, boolean isWild) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.isCommunal = isCommunal;
        this.isSolitary = isSolitary;
        this.isWild = isWild;
    }

    /**
     * Todo: Iterators again
     * For communal and solitary: if it isn't one of those, it shouldn't return any iterator
     * since it cannot be compared to other Bees who are.
     */
    @Override
    public Iterator<?> communal() {
        if (!isCommunal) {
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
