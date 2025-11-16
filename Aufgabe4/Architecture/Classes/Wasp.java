package Architecture.Classes;

import Architecture.interfaces.Observation;
import Architecture.interfaces.Pollinator;

import java.util.Date;
import java.util.Iterator;

public abstract class Wasp implements Observation {

    /**
     * This class is abstract because it doesn't count as an Observation, since it is
     * just an overreaching term describing bugs with stingers.
     * All Bees are Wasps, but not all Wasps are bees.
     */
    public Date date;
    public int time;
    public String description;
    public boolean removed;

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
    public void remove(){
        removed = true;
    }

    /**
     * Checks if Observation is removed.
     */
    @Override
    public boolean valid(){
        return removed;
    }

    /**
     * Todo: Implement logic for Iterator.
     */
    @Override
    public Iterator<Pollinator> earlier(){
        return null;
    }

    /**
     * Todo: Implement logic for Iterator.
     */
    @Override
    public Iterator<Pollinator> later(){
        return null;
    }

}
