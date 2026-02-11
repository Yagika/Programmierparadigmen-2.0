package Simulation;

import java.util.Random;


import Bee.Bee;
import Bee.BeeU;
import Bee.BeeV;
import Bee.BeeW;
import Bee.BeeStatistics;
import Flower.Flower;
import Flower.FlowerX;
import Flower.FlowerY;
import Flower.FlowerZ;
import Flower.FlowerStatistics;
import Meta.Precondition;
import Meta.Postcondition;
import Meta.Responsible;

/**
 * Simulation of bees and flowers.
 */
@Responsible("Yana")
public class Simulation {

    private final Random random = new Random();

    // sets of all bees and flowers (no generics)
    private final Set bees = new Set();
    private final Set flowers = new Set();

    public Simulation() {
    }
    @Precondition("At start of run(), both sets bees and flowers are non-null.")
    @Postcondition("After run(), no bee is active or no flower is visitable.")
    public void run() {
        // 7 days spawning
        for (int day = 1; day <= 7; day++) {
            spawnForDay(day);
            simulateOneDay(day);
        }

        // After that: continue until no active bee or no visitable flower
        int day = 8;
        while (existsActiveBee() && existsVisitableFlower()) {
            simulateOneDay(day);
            day++;
        }
    }

    private void spawnForDay(int day) {
        int beeTypePair = random.nextInt(3);    // 0: U+V, 1: U+W, 2: V+W
        int flowerTypePair = random.nextInt(3); // 0: X+Y, 1: X+Z, 2: Y+Z

        int numBees = 1 + random.nextInt(3);     // 1..3 bees per day
        int numFlowers = 1 + random.nextInt(3);  // 1..3 flowers per day

        for (int i = 0; i < numBees; i++) {
            bees.add(createRandomBee(beeTypePair));
        }

        for (int i = 0; i < numFlowers; i++) {
            flowers.add(createRandomFlower(flowerTypePair));
        }
    }

    private Bee createRandomBee(int pair) {
        switch (pair) {
            case 0: // U + V
                return random.nextBoolean() ? new BeeU() : new BeeV();
            case 1: // U + W
                return random.nextBoolean() ? new BeeU() : new BeeW();
            default: // 2: V + W
                return random.nextBoolean() ? new BeeV() : new BeeW();
        }
    }

    private Flower createRandomFlower(int pair) {
        switch (pair) {
            case 0: // X + Y
                return random.nextBoolean() ? new FlowerX() : new FlowerY();
            case 1: // X + Z
                return random.nextBoolean() ? new FlowerX() : new FlowerZ();
            default: // 2: Y + Z
                return random.nextBoolean() ? new FlowerY() : new FlowerZ();
        }
    }

    /**
     * Simulate a single day:
     * - each active bee makes some visits,
     *   respecting preference / fallback rules.
     * - at the end, decrement active days of bees and flowers.
     */
    @Precondition("day >= 1.")
    private void simulateOneDay(int day) {
        int beeCount = bees.size();

        for (int i = 0; i < beeCount; i++) {
            Bee bee = (Bee) bees.get(i);
            if (!bee.isActive()) continue;

            int visitsToday = 1 + random.nextInt(3); // 1..3 visits per day
            for (int v = 0; v < visitsToday; v++) {
                Flower flower = chooseFlowerForBee(bee);
                if (flower != null) {
                    bee.collectFrom(flower);
                } else {
                    // no suitable flower -> stop visiting
                    break;
                }
            }
        }

        // decrement active days
        for (int i = 0; i < beeCount; i++) {
            Bee bee = (Bee) bees.get(i);
            bee.decrementActiveTime();
        }

        int flowerCount = flowers.size();
        for (int i = 0; i < flowerCount; i++) {
            Flower flower = (Flower) flowers.get(i);
            flower.decrementActiveTime();
        }
    }

    /**
     * Choose a flower for a bee:
     * - if any active preferred flowers exist -> choose uniformly among them;
     * - else, if any active alternative flowers exist -> choose among them;
     * - else -> null.
     *
     * Implemented without instanceof via double dynamic dispatch.
     */
    private Flower chooseFlowerForBee(Bee bee) {
        Flower preferred = chooseFlowerWithRelation(bee, true);
        if (preferred != null) return preferred;
        return chooseFlowerWithRelation(bee, false);
    }

    private Flower chooseFlowerWithRelation(Bee bee, boolean preferred) {
        int n = flowers.size();
        int countMatches = 0;

        // 1st pass: count matches
        for (int i = 0; i < n; i++) {
            Flower f = (Flower) flowers.get(i);
            if (!f.isActive()) continue;
            boolean ok = preferred ? bee.isPreferred(f) : bee.isAlternative(f);
            if (ok) {
                countMatches++;
            }
        }

        if (countMatches == 0) return null;

        // choose random index among matches
        int target = random.nextInt(countMatches);
        int seen = 0;

        // 2nd pass: return target-th matching flower
        for (int i = 0; i < n; i++) {
            Flower f = (Flower) flowers.get(i);
            if (!f.isActive()) continue;
            boolean ok = preferred ? bee.isPreferred(f) : bee.isAlternative(f);
            if (ok) {
                if (seen == target) {
                    return f;
                }
                seen++;
            }
        }
        return null; // should not happen
    }

    private boolean existsActiveBee() {
        int n = bees.size();
        for (int i = 0; i < n; i++) {
            Bee bee = (Bee) bees.get(i);
            if (bee.isActive()) return true;
        }
        return false;
    }

    private boolean existsVisitableFlower() {
        int n = flowers.size();
        for (int i = 0; i < n; i++) {
            Flower f = (Flower) flowers.get(i);
            if (f.isActive()) return true;
        }
        return false;
    }

    // --- Statistics at the end of simulation ---

    public void printStatistics() {
        int nBees = bees.size();
        int nFlowers = flowers.size();

        BeeStatistics beeStats = new BeeStatistics();
        for (int i = 0; i < nBees; i++) {
            Bee b = (Bee) bees.get(i);
            b.report(beeStats);
        }

        FlowerStatistics flowerStats = new FlowerStatistics();
        for (int i = 0; i < nFlowers; i++) {
            Flower f = (Flower) flowers.get(i);
            f.report(flowerStats);
        }

        System.out.println("=== Kontext A: Statistiken ===");

        System.out.println("=== Bienenarten: Gesamtbesuche und Durchschnitt pro Pflanze ===");
        if (beeStats.getCountU() > 0) {
            int totalU = beeStats.getTotalVisitsU();
            double avgPerPlantU = (nFlowers == 0) ? 0.0 : ((double) totalU) / nFlowers;
            System.out.printf("Bienenart U: Gesamtbesuche = %d, Durchschnitt pro Pflanze = %.2f%n",
                    totalU, avgPerPlantU);
        }
        if (beeStats.getCountV() > 0) {
            int totalV = beeStats.getTotalVisitsV();
            double avgPerPlantV = (nFlowers == 0) ? 0.0 : ((double) totalV) / nFlowers;
            System.out.printf("Bienenart V: Gesamtbesuche = %d, Durchschnitt pro Pflanze = %.2f%n",
                    totalV, avgPerPlantV);
        }
        if (beeStats.getCountW() > 0) {
            int totalW = beeStats.getTotalVisitsW();
            double avgPerPlantW = (nFlowers == 0) ? 0.0 : ((double) totalW) / nFlowers;
            System.out.printf("Bienenart W: Gesamtbesuche = %d, Durchschnitt pro Pflanze = %.2f%n",
                    totalW, avgPerPlantW);
        }

        System.out.println();
        System.out.println("=== Pflanzenarten: Gesamtbesuche und Durchschnitt pro Biene ===");
        if (flowerStats.getCountX() > 0) {
            int totalX = flowerStats.getTotalVisitsX();
            double avgPerBeeX = (nBees == 0) ? 0.0 : ((double) totalX) / nBees;
            System.out.printf("Pflanzenart X: Gesamtbesuche = %d, Durchschnitt pro Biene = %.2f%n",
                    totalX, avgPerBeeX);
        }
        if (flowerStats.getCountY() > 0) {
            int totalY = flowerStats.getTotalVisitsY();
            double avgPerBeeY = (nBees == 0) ? 0.0 : ((double) totalY) / nBees;
            System.out.printf("Pflanzenart Y: Gesamtbesuche = %d, Durchschnitt pro Biene = %.2f%n",
                    totalY, avgPerBeeY);
        }
        if (flowerStats.getCountZ() > 0) {
            int totalZ = flowerStats.getTotalVisitsZ();
            double avgPerBeeZ = (nBees == 0) ? 0.0 : ((double) totalZ) / nBees;
            System.out.printf("Pflanzenart Z: Gesamtbesuche = %d, Durchschnitt pro Biene = %.2f%n",
                    totalZ, avgPerBeeZ);
        }
    }
}
