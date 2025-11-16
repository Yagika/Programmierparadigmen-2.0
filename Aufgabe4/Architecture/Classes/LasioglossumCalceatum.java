package Architecture.Classes;

import Architecture.interfaces.SocialBee;
import Architecture.interfaces.SolitaryBee;

import java.util.Date;
import java.util.Iterator;

public class LasioglossumCalceatum extends Bee implements SocialBee, SolitaryBee {

    public boolean isSocial;
    public boolean isSolitary;
    public boolean isWild;

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

    /**
     * Todo: Iterator logic.
     */
    public Iterator<Bee> social(){
        if(!isSocial){
            return null;
        }
        return null;
    }

    public Iterator<Bee> solitary(){
        if(!isSolitary){
            return null;
        }
        return null;
    }

    public Iterator<Bee> wild(boolean isWild){
        return null;
    }

}

// Typically Social but is Solitary in colder climates