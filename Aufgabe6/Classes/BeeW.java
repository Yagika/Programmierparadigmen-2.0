package Classes;

/**
 * Bee of type W, prefers plantZ, can get plantX, cannot get plantY
 * at time of creation, is active for exactly 10 days.
 */

public class BeeW extends Bee {

    /**
     * collectFrom(Flower flower), calls accept(Bee bee) in given flower if Bee is considered active.
     */
    public void collectFrom(Flower flower){
        if(isActive()){
            flower.accept(this);
        }
    }

    /**
     * constructor: activeTime of W is 10
     */
    public BeeW(){
        activeTime = 10;
    }
}
