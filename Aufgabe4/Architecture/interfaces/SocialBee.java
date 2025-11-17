package Architecture.interfaces;

import java.util.Iterator;

/**
 * SocialBee: Beobachtung einer Biene einer sozialen Art.
 * Wird nur von Bee-Untertypen implementiert.
 */
public interface SocialBee extends Observation {

    /**
     * SocialBee: direct subtype of Observation, since no other dependency exists.
     */


    /*
    A Social Bee has to implement this method based on it's type:
     */

    Iterator<?> social();
}
