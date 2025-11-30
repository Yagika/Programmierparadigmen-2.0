package Classes;

/**
 * Flower of type Y, only accepts BeeV and BeeU.
 * active for 9 days.
 * Implements the Visitor-Pattern as described in the scriptum document on page 212.
 */
public class FlowerY extends Flower{

    /**
     * constructor: activeTime of FlowerY is 9 days.
     */
    public FlowerY() {
        activeTime = 9;
    }

    /**
     * accept(BeeV bee): this flower accepts BeeV
     * increments counter of V in FlowerY, increments counter of Y in BeeV
     */
    public void accept(BeeV bee){
        if(isActive()){
            incrementV();
            bee.increaseY();
        }
    }
    /**
     * accept(BeeU bee): this flower accepts BeeU
     * increments counter of U in FlowerY, increments counter of Y in BeeU
     */
    public void accept(BeeU bee){
        if(isActive()){
            incrementU();
            bee.increaseY();
        }
    }
    /**
     * accept(BeeW bee): this flower does not accept BeeW
     */
    public void accept(BeeW bee){}
}
