package Classes;

/**
 * Base class for flower
 * Flower is forced to be public by the implementation of the collect(Flower flower) method.
 */

public abstract class Flower {
    /**
     * number of visits by type of bee:
     */
    protected int visitedByU = 0;
    protected int visitedByV = 0;
    protected int visitedByW = 0;

    /**
     * activeTime: sets the amount of days this type of flower is active for. flower is considered inactive if time is 0
     */
    protected int activeTime;

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
}
