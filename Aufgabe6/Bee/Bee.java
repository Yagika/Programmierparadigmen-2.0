package Bee;

import Flower.Flower;
import Meta.Invariant;
import Meta.Responsible;
import Meta.HistoryConstraint;
import Meta.Precondition;
import Meta.Postcondition;

/**
 * Base class for bee.
 */
@Responsible("Dominik")
@HistoryConstraint("activeTime never increases and visit counters never decrease over the lifetime of a bee.")
@Invariant("A bee must have activeTime >= 0 and visit counters >= 0.")
public abstract class Bee {

    /**
     * Number of visits to each plant type.
     */
    private int collectedFromX = 0;
    private int collectedFromY = 0;
    private int collectedFromZ = 0;

    /**
     * Number of days this bee is active. Bee is inactive when activeTime == 0.
     */
    private int activeTime;
    @Precondition("activeTime parameter must be >= 0.")
    @Postcondition("The bee's activeTime is set to the given parameter.")
    protected Bee(int activeTime) {
        this.activeTime = activeTime;
    }

    /**
     * Decrease activeTime by one day until it reaches 0.
     */
    @Precondition("activeTime is always >= 0 before the call.")
    @Postcondition("activeTime is either unchanged (if it was 0) or decreased by exactly 1.")
    public void decrementActiveTime() {
        if (activeTime > 0) {
            activeTime--;
        }
    }

    /**
     * Returns true if the bee is still active.
     */
    @Postcondition("Result is true if and only if activeTime > 0.")
    public boolean isActive() {
        return activeTime > 0;
    }

    /**
     * Methods required by the assignment:
     * collectedFromX/Y/Z return how many visits this bee made to each plant type.
     */
    public int collectedFromX() {
        return collectedFromX;
    }

    public int collectedFromY() {
        return collectedFromY;
    }

    public int collectedFromZ() {
        return collectedFromZ;
    }

    /**
     * Sum of all visits of this bee.
     */
    public int totalCollected() {
        return collectedFromX + collectedFromY + collectedFromZ;
    }

    /**
     * Collects from the given flower (subclasses implement the behavior).
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

    /**
     * Increase visit counters for plant types.
     * These are called from flowers inside the same package.
     */
    public void increaseX() {
        collectedFromX++;
    }

    public void increaseY() {
        collectedFromY++;
    }

    public void increaseZ() {
        collectedFromZ++;
    }
}
