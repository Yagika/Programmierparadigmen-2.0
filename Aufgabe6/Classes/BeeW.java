package Classes;

/**
 * Bee of type W, prefers plantZ, can get plantX, cannot get plantY
 * at time of creation, is active for exactly 10 days.
 */
public class BeeW extends Bee {

    public BeeW() {
        super(10);
    }

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
