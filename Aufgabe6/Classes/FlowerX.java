package Classes;


/**
 * Flower of type X, only accepts BeeU and BeeW.
 * active for 9 days.
 * Implements the Visitor-Pattern as described in the scriptum document on page 212.
 */
public class FlowerX extends Flower{

    /**
     * constructor: activeTime of FlowerX is 9 days.
     */
    public FlowerX() {
        super(9);
    }

    /**
     * accept(BeeU bee): this flower accepts BeeU
     * increments counter of U in FlowerX, increments counter of X in BeeU
     */
    @Override
    public void accept(BeeU bee){
        if(isActive()){
            incrementU();
            bee.increaseX();
        }
    }
    /**
     * accept(BeeW bee): this flower accepts BeeW
     * increments counter of W in FlowerX, increments counter of X in BeeW
     */
    @Override
    public void accept(BeeW bee){
        if(isActive()){
            incrementW();
            bee.increaseX();
        }
    }
    /**
     * accept(BeeV bee): this flower does not accept BeeV
     */
    @Override
    public void accept(BeeV bee){}
    // preferences:

    @Override
    public boolean isPreferredBy(BeeU bee) { return true; }

    @Override
    public boolean isAlternativeFor(BeeW bee) { return true; }

    @Override
    public void report(FlowerStatistics stats) {
        stats.record(this);
    }
}
