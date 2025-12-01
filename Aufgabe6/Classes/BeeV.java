package Classes;

/**
 * Bee of type V, prefers plantY, can get plantZ, cannot get plantX
 * at time of creation, is active for exactly 8 days.
 */
public class BeeV extends Bee {

    public BeeV() {
        super(8);
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
