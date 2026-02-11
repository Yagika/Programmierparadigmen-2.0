package Architecture.interfaces;

import java.util.Date;
import java.util.Iterator;
/**
 * Obertyp aller Beobachtungen.
 * Zusicherungen:
 *  - Eine Beobachtung hat ein Datum, eine Uhrzeit und eine Beschreibung.
 *  - remove() markiert die Beobachtung logisch als entfernt.
 *  - valid() == false genau dann, wenn remove() zuvor aufgerufen wurde.
 *  - earlier()/later() liefern niemals null.
 */
public interface Observation {

    /**
     * @return Date of observation
     */
    Date getDate();


    /**
     * @return Time of observation
     * Note: lets just use an int instead of having to import more things
     */
    int getTime();


    /**
     * @return Description of the observed Bug (Architecture.Classes.Wasp/Architecture.Classes.Bee/Architecture.interfaces.Pollinator)
     */
    String getDescription();


    /**
     * Note: returning a String with information about what got "removed"
     * would probably help with debugging, but thats too much work.
     */
    void remove();


    /**
     * @return false if observation removed, true if it is NOT removed.
     */
    boolean valid();


    /**
     * @return Iterator of observations compared to THIS that have a later date
     * sorted from closest to the observation to the last.
     */
    Iterator<?> later();


    /**
     * @return Iterator of observations compared to THIS that have a earlier date
     * sorted from closest to this observation to earliest.
     */
    Iterator<?> earlier();


}
