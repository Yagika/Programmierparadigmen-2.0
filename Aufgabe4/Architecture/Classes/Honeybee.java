package Architecture.Classes;

import Architecture.interfaces.SocialBee;

import java.util.Date;
import java.util.Iterator;

public class Honeybee extends Bee implements SocialBee{

    /**
     * Constructor for Honeybee
     */
    public Honeybee(String description, Date date, int time) {
        this.description = description;
        this.date = date;
        this.time = time;
        this.removed = false;
    }

    @Override
    public Iterator<?> social() {
        return null;
    }
}
