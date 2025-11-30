package Classes;

/**
 * Bee of type U, prefers plantX, can get plantY, cannot get plantZ
 * at time of creation, is active for exactly 9 days.
 */

public class BeeU extends Bee {

    /**
     * collectFrom(Flower flower), calls accept(Bee bee) in given flower if Bee is considered active.
     */
    public void collectFrom(Flower flower){
        if(isActive()){
            flower.accept(this);
        }
    }
    /**
     * constructor: activeTime of U is 9 days.
     */
    public BeeU(){
        activeTime = 9;
    }

}
