package Architecture.Classes;

import Architecture.interfaces.CommunalBee;

import java.util.Date;
import java.util.Iterator;

public class AndrenaBucephala extends Bee implements CommunalBee {

    public boolean isCommunal;
    public boolean isSolitary;
    public boolean isWild;

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
    public Iterator<Bee> communal(){
        if(!isCommunal){
            return null;
        }
        return null;
    }

    @Override
    public Iterator<?> solitary() {
        if(!isSolitary){
            return null;
        }
        return null;
    }

    @Override
    public Iterator<?> wild(boolean isWild) {
        return null;
    }
}
