package Architecture.interfaces;

import java.util.Iterator;
/**
 * SolitaryBee: Biene einer Art, die solitär leben kann.
 * Untertyp von WildBee (alle solitären Arten sind Wildbienen).
 */
public interface SolitaryBee extends WildBee {

    /*
    A solitary Bee has to implement this method based on it's type:
     */

    Iterator<?> solitary();

}
