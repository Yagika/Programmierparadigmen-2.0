package Classes;
import Meta.Invariant;
import Meta.Responsible;

/**
 * Base class for bee.
 */
@Responsible("Dominik")
@Invariant("A bee must have activeTime >= 0 and visit counters >= 0.")
public abstract class Bee {
    /**
     * number of visits to a type of plant:
     */
    private int collectedfromX = 0;
    private int collectedfromY = 0;
    private int collectedfromZ = 0;

    /**
     * activeTime: sets the amount of days this type of bee is active for. Bee is considered inactive if time is 0
     */
    private int activeTime;

    protected Bee(int activeTime) {
        this.activeTime = activeTime;
    }


    /**
     * decrementActiveTime: decreases activeTime until it is 0, Bee is considered inactive if time is 0.
     */
    public void decrementActiveTime() {
        if (activeTime > 0) {
            activeTime--;
        }
    }

    /**
     * isActive(): returns true if the bee is active, activeTime > 0
     */
    public boolean isActive() {
        return activeTime > 0;
    }

    /**
     * get methods for visits of a certain type of plant:
     */
    public int getCollectedFromX() {
        return collectedfromX;
    }

    public int getCollectedFromY() {
        return collectedfromY;
    }

    public int getCollectedFromZ() {
        return collectedfromZ;
    }

    /**
     * abstract method collectFrom(Flower flower), implementation varies per type of bee.
     */
    public abstract void collectFrom(Flower flower);

    /**
     * Preference queries: implemented by subclasses via double dispatch
     * on Flower (isPreferredBy / isAlternativeFor).
     */
    public abstract boolean isPreferred(Flower flower);
    public abstract boolean isAlternative(Flower flower);

    /**
     * For statistics, each bee can report itself to a BeeStatistics collector.
     */
    public abstract void report(BeeStatistics stats);
    public int totalCollected() {
        return collectedfromX + collectedfromY + collectedfromZ;
    }
    /**
     * methods to increment the visits of a certain type of plant:
     */
    protected void increaseX() {
        collectedfromX++;
    }

    protected void increaseY() {
        collectedfromY++;
    }

    protected void increaseZ() {
        collectedfromZ++;
    }
}
