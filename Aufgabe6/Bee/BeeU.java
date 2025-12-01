package Bee;

import Flower.Flower;
import Meta.Precondition;
import Meta.Postcondition;
import Meta.Responsible;

/**
 * Bee of type U.
 * Prefers plant X, can use plant Y, cannot use plant Z.
 * When created, is active for exactly 9 days.
 */
@Responsible("Dominik")
public class BeeU extends Bee {
    @Precondition("No specific precondition. Bee U always starts with fixed activeTime 9.")
    @Postcondition("The bee's activeTime is 9 days.")
    public BeeU() {
        super(9);
    }

    /**
     * If both bee and flower are active, this bee visits the given flower.
     */
    @Override
    public void collectFrom(Flower flower) {
        if (isActive() && flower != null && flower.isActive()) {
            flower.accept(this);
        }
    }

    @Override
    public boolean isPreferred(Flower flower) {
        return flower.isPreferredBy(this);
    }

    @Override
    public boolean isAlternative(Flower flower) {
        return flower.isAlternativeFor(this);
    }

    @Override
    public void report(BeeStatistics stats) {
        stats.record(this);
    }
}
