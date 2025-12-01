package Bee;

import Meta.Responsible;

/**
 * Collects statistics for bees per species.
 */
@Responsible("Aleksander")
public class BeeStatistics {

    private int countU;
    private int totalVisitsU;

    private int countV;
    private int totalVisitsV;

    private int countW;
    private int totalVisitsW;

    public void record(BeeU bee) {
        countU++;
        totalVisitsU += bee.totalCollected();
    }

    public void record(BeeV bee) {
        countV++;
        totalVisitsV += bee.totalCollected();
    }

    public void record(BeeW bee) {
        countW++;
        totalVisitsW += bee.totalCollected();
    }

    public int getCountU() { return countU; }
    public int getTotalVisitsU() { return totalVisitsU; }

    public int getCountV() { return countV; }
    public int getTotalVisitsV() { return totalVisitsV; }

    public int getCountW() { return countW; }
    public int getTotalVisitsW() { return totalVisitsW; }
}
