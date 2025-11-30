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
        activeTime = 10;
    }

    /**
     * accept(BeeW bee): this flower accepts BeeW
     * increments counter of W in FlowerZ, increments counter of Z in BeeW
     */
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
    public void accept(BeeV bee){
        if(isActive()){
            incrementV();
            bee.increaseZ();
        }
    }
    /**
     * accept(BeeU bee): this flower does not accept BeeU
     */
    public void accept(BeeU bee){}
}
