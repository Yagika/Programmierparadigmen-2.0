package Classes;

/**
 * Bee of type U, prefers plantX, can get plantY, cannot get plantZ
 * at time of creation, is active for exactly 9 days.
 */
public class BeeU extends Bee {

    public BeeU() {
        super(9);
    }

    /**
     * collectFrom(Flower flower), calls accept(BeeU bee) in given flower if Bee is active.
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
