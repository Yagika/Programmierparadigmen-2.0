package Flower;
import Bee.BeeU;
import Bee.BeeV;
import Bee.BeeW;
import Meta.Responsible;
import Meta.Precondition;
import Meta.Postcondition;

/**
 * Flower of type X, only accepts BeeU and BeeW.
 * active for 9 days.
 * Implements the Visitor-Pattern as described in the scriptum document on page 212.
 */
@Responsible("Dominik")
public class FlowerX extends Flower{

    @Precondition("No specific precondition. Flower X always starts with fixed activeTime 9.")
    @Postcondition("The flower's activeTime is 9 days.")
    public FlowerX() {
        super(9);
    }

    /**
     * accept(BeeU bee): this flower accepts BeeU
     * increments counter of U in FlowerX, increments counter of X in BeeU
     */
    @Precondition("Both bee and this flower may be active; if bee or flower is inactive, no effect.")
    @Postcondition("If both were active, the visit counter for U and the bee's counter for X are increased by 1.")
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
