package Classes;
import Meta.Invariant;
import Meta.Responsible;

/**
 * Base class for flower
 * Flower is forced to be public by the implementation of the collect(Flower flower) method.
 */

@Responsible("Dominik")
@Invariant("A flower must have activeTime >= 0 and visit counters >= 0.")
public abstract class Flower {
    /**
     * number of visits by type of bee:
     */
    private int visitedByU = 0;
    private int visitedByV = 0;
    private int visitedByW = 0;

    /**
     * activeTime: sets the amount of days this type of flower is active for. flower is considered inactive if time is 0
     */
    private int activeTime;
    protected Flower(int activeTime) {
        this.activeTime = activeTime;
    }


    /**
     * decrementActiveTime: decreases activeTime until it is 0, flower is considered inactive if time is 0.
     */
    public void decrementActiveTime(){
        if(activeTime > 0){
            activeTime--;
        }
    }

    /**
     * isActive(): returns true if the flower is active, activeTime > 0
     */
    public boolean isActive(){
        return activeTime > 0;
    }
    /**
     * Helper: total number of visits from all bees.
     */
    public int totalVisited() {
        return visitedByU + visitedByV + visitedByW;
    }

    /**
     * methods to increment the visits by type of bee:
     */
    protected void incrementU(){ visitedByU++; }
    protected void incrementV(){ visitedByV++; }
    protected void incrementW(){ visitedByW++; }

    /**
     * abstract methods, implementation of type preferences:
     */
    public abstract void accept(BeeU bee);
    public abstract void accept(BeeV bee);
    public abstract void accept(BeeW bee);

    /**
     * get methods:
     */
    public int getVisitedByU() { return visitedByU; }
    public int getVisitedByV() { return visitedByV; }
    public int getVisitedByW() { return visitedByW; }

    /**
     * Preference classification – double dispatch.
     * Default: everything is neither preferred nor alternative.
     */
    public boolean isPreferredBy(BeeU bee) { return false; }
    public boolean isPreferredBy(BeeV bee) { return false; }
    public boolean isPreferredBy(BeeW bee) { return false; }

    public boolean isAlternativeFor(BeeU bee) { return false; }
    public boolean isAlternativeFor(BeeV bee) { return false; }
    public boolean isAlternativeFor(BeeW bee) { return false; }

    /**
     * For statistics, each flower reports itself to FlowerStatistics.
     */
    public abstract void report(FlowerStatistics stats);
}
