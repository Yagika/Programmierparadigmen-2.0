package Architecture.interfaces;

import java.util.Iterator;

public interface SolitaryBee extends WildBee {

    /*
    A solitary Bee has to implement this method based on it's type:
     */

    Iterator<?> solitary();

}
