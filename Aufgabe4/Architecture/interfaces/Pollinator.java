package Architecture.interfaces;

/**
 * Beobachtung eines Insekts, das als üblicher Bestäuber gilt.
 * Alle Bees sind Pollinator, FlowerFly auch, weitere Typen könnten folgen.
 */
public interface Pollinator extends Observation {

    /**
     * This has to be an Interface because Bee is already a Subclass of Wasp, cannot inherit from two classes.
     * Also if at some point we wanted to add methods that work differently from Bees and FlowerFlies, an abstract Class
     * would add more work
     */
}
