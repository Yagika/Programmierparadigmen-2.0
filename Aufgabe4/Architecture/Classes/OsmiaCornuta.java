package Architecture.Classes;

import Architecture.interfaces.SolitaryBee;

import java.util.Date;
import java.util.Iterator;

public class OsmiaCornuta extends Bee implements SolitaryBee {
    /*
    It only exhibits a solitary style as per document.
     */

    /**
     * it is not apparent from the document if this Bee is also able to be bred/kept like the
     * Bumblebee is.
     */
    public boolean isWild;

    /**
     * Constructor without isWild
     */
    public OsmiaCornuta(String description, Date date, int time) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.isWild = false;
        this.removed = false;
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



    /**
     * Todo: iterator logic
     */
    @Override
    public Iterator<Bee> solitary() {
        return null;
    }
    /**
     * Todo: iterator logic
     */
    @Override
    public Iterator<Bee> wild(boolean isWild) {
        return null;
    }

}
