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
        activeTime = 9;
    }

    /**
     * accept(BeeU bee): this flower accepts BeeU
     * increments counter of U in FlowerX, increments counter of X in BeeU
     */
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
    public void accept(BeeW bee){
        if(isActive()){
            incrementW();
            bee.increaseX();
        }
    }
    /**
     * accept(BeeV bee): this flower does not accept BeeV
     */
    public void accept(BeeV bee){}
}
