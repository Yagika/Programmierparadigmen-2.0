package Classes;

/**
 * Collects statistics for flowers per species.
 */
public class FlowerStatistics {

    private int countX;
    private int totalVisitsX;

    private int countY;
    private int totalVisitsY;

    private int countZ;
    private int totalVisitsZ;

    public void record(FlowerX x) {
        countX++;
        totalVisitsX += x.totalVisited();
    }

    public void record(FlowerY y) {
        countY++;
        totalVisitsY += y.totalVisited();
    }

    public void record(FlowerZ z) {
        countZ++;
        totalVisitsZ += z.totalVisited();
    }

    public int getCountX() { return countX; }
    public int getTotalVisitsX() { return totalVisitsX; }

    public int getCountY() { return countY; }
    public int getTotalVisitsY() { return totalVisitsY; }

    public int getCountZ() { return countZ; }
    public int getTotalVisitsZ() { return totalVisitsZ; }
}
