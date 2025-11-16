package Architecture.Classes;

import Architecture.interfaces.SocialBee;
import Architecture.interfaces.WildBee;

import java.util.Date;
import java.util.Iterator;

public class Bumblebee extends Bee implements SocialBee, WildBee {

    /*
    BumbleBees are all considered WildBees as per document description in the WildBee: section.
    also they may be bred.
     */

    public boolean isWild;

    /**
     * Todo: implement iterator logic:
     * this.isWild should be used, probably
     */
    @Override
    public Iterator<Bee> wild(boolean isWild) {
        return null;
    }

    /**
     * Todo: implement iterator logic:
     */
    @Override
    public Iterator<Bee> social() {
        return null;
    }

    /**
     * Constructor if it's know to be Wild or not.
     */
    public Bumblebee(String description, Date date, int time, boolean isWild){
        this.description = description;
        this.date = date;
        this.time = time;
        this.isWild = isWild;
    }

    /**
     * Constructor if it is not know to be wild or not.
     */
    public Bumblebee(String description, Date date, int time){
        this.description = description;
        this.date = date;
        this.time = time;
        this.isWild = false;
    }
}
