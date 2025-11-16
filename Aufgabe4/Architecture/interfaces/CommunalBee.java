package Architecture.interfaces;

import java.util.Iterator;

public interface CommunalBee extends SolitaryBee{

    /**
     * Because every communalBee can also be solitary, it is necessary to implement this relationship.
     * since SolitaryBee is an interface, so must this also be to not alter the inheritance from the
     * Abstract Bee class.
     */
    Iterator<?> communal();

}
