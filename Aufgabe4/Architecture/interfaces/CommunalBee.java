package Architecture.interfaces;

import java.util.Iterator;

/**
 * CommunalBee: Biene einer kommunalen Art.
 * Jede kommunale Art kann auch solitär leben → Untertyp von SolitaryBee.
 */
public interface CommunalBee extends SolitaryBee {

    /**
     * CommunalBee, subtype of SolitaryBee since all CommunalBees CAN be SolitaryBee,
     * if stated otherwise CommunalBee wouldn't have this relationship with SolitaryBee.
     */
    Iterator<?> communal();

}
