package Classes;
/**
 * Base class for bee:
 */
abstract class Bee {
    /**
     * number of visits to a type of plant:
     */
    protected int collectedfromX = 0;
    protected int collectedfromY = 0;
    protected int collectedfromZ = 0;

    /**
     * activeTime: sets the amount of days this type of bee is active for. Bee is considered inactive if time is 0
     */
    protected int activeTime;

    /**
     * decrementActiveTime: decreases activeTime until it is 0, Bee is considered inactive if time is 0.
     */
    public void decrementActiveTime(){
        if(activeTime > 0){
            activeTime--;
        }
    }

    /**
     * isActive(): returns true if the bee is active, activeTime > 0
     */
    public boolean isActive(){
        return activeTime > 0;
    }

    /**
     * get methods for visits of a certain type of plant:
     */
    public int getCollectedFromX(){ return collectedfromX; }
    public int getCollectedFromY(){ return collectedfromY; }
    public int getCollectedFromZ(){ return collectedfromZ; }

    /**
     * abstract method collectFrom(Flower flower), implementation varies per type of bee.
     */
    public abstract void collectFrom(Flower flower);

    /**
     * methods to increment the visits of a certain type of plant:
     */
    protected void increaseX() { collectedfromX++; }
    protected void increaseY() { collectedfromY++; }
    protected void increaseZ() { collectedfromZ++; }
}
