package Classes;

/**
 * Flower of type Z, only accepts BeeW and BeeV.
 * active for 10 days.
 * Implements the Visitor-Pattern as described in the scriptum document on page 212.
 */
public class FlowerZ extends Flower{

    /**
     * constructor: activeTime of FlowerZ is 10 days.
     */
    public FlowerZ() {
        super(10);
    }
    /**
     * accept(BeeW bee): this flower accepts BeeW
     * increments counter of W in FlowerZ, increments counter of Z in BeeW
     */
    @Override
    public void accept(BeeW bee){
        if(isActive()){
            incrementW();
            bee.increaseZ();
        }
    }
    /**
     * accept(BeeV bee): this flower accepts BeeV
     * increments counter of V in FlowerZ, increments counter of Z in BeeV
     */
    @Override
    public void accept(BeeV bee){
        if(isActive()){
            incrementV();
            bee.increaseZ();
        }
    }
    /**
     * accept(BeeU bee): this flower does not accept BeeU
     */
    @Override
    public void accept(BeeU bee){}

    @Override
    public boolean isPreferredBy(BeeW bee) { return true; }

    @Override
    public boolean isAlternativeFor(BeeV bee) { return true; }

    @Override
    public void report(FlowerStatistics stats) {
        stats.record(this);
    }
}
