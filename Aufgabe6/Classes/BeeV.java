package Classes;

/**
 * Bee of type V, prefers plantY, can get plantZ, cannot get plantX
 * at time of creation, is active for exactly 8 days.
 */

public class BeeV extends Bee {

    /**
     * collectFrom(Flower flower), calls accept(Bee bee) in given flower if Bee is considered active.
     */
    public void collectFrom(Flower flower){
        if(isActive()){
            flower.accept(this);
        }
    }

    /**
     * constructor: activeTime of V is 8
     */
    public BeeV(){
        activeTime = 8;
    }
}
