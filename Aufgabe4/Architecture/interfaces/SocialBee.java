package Architecture.interfaces;

import java.util.Iterator;

public interface SocialBee extends Observation {

    /*
    A Social Bee has to implement this method based on it's type:
     */

    Iterator<?> social();
}
