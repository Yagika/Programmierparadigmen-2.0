package architecture;

import architecture.Pollinators.Pollinator;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a local group of flowers that share
 * the same spatial position within the simulated environment.
 * <p>
 * STYLE: object-oriented – encapsulates group-level interactions.
 */
public class FlowerGroup {

    private static final double MAX_DISTANCE = 2000.0;

    private ArrayList<FlowerSpecies> speciesList;
    private double x;
    private double y; // Coordinates


    public FlowerGroup(ArrayList<FlowerSpecies> speciesList, double x, double y) {
        this.speciesList = speciesList;
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the total available food supply for all plants in this group.
     *
     * @return sum of food contributions
     */
    public double totalFood() {
        double n = 0.0;
        for (FlowerSpecies f : speciesList) n += f.foodvalue();
        return n;
    }

    /**
     * Applies distributed pollination across all plant species in this group,
     * based on distance from each pollinator and plant attractiveness.
     *
     * @param pollinators set of bee-like species
     * @param day         vegetation day (for active season filtering)
     * @param sunD        today's sun hours (0..12), influences seed increment
     */
    public void applyPollination(ArrayList<? extends Pollinator> pollinators, int day, double sunD) {
        double groupAttractiveness = 0.0;
        double[] plantAttract = new double[speciesList.size()];

        for (int idx = 0; idx < speciesList.size(); idx++) {
            FlowerSpecies plant = speciesList.get(idx);
            double plantSum = 0.0;

            for (Pollinator bee : pollinators) {
                if (!bee.isActive(day)) continue;

                double dx = Math.abs(this.x - bee.getX());
                double dy = Math.abs(this.y - bee.getY());
                double dist = Math.sqrt(dx * dx + dy * dy);
                double distFactor = Math.exp(-dist / 600.0);

                double pref = 0.8;
                try {
                    var cLow = ((architecture.Pollinators.Bee) bee).getPrefLower();
                    var cUp = ((architecture.Pollinators.Bee) bee).getPrefUpper();
                    pref = (plant.getBrightness() >= cLow && plant.getBrightness() <= cUp) ? 1.25 : 0.8;
                } catch (ClassCastException ignored) {
                }

                double a = bee.getActivity();
                double eff = bee.getEffectiveness();
                double pop = bee.getPopulation();

                plantSum += pop * a * eff * distFactor * pref;
            }

            plantAttract[idx] = plantSum;
            groupAttractiveness += plantSum;
        }

        if (groupAttractiveness <= 0) return;

        for (int idx = 0; idx < speciesList.size(); idx++) {
            FlowerSpecies plant = speciesList.get(idx);
            double share = plantAttract[idx] / groupAttractiveness;
            double inc = share * plant.getB() * (sunD + 2.0) * 0.015;
            plant.setS(plant.getS() + inc);
        }
    }

    /**
     * Resets the blooming in the beginning of the new year
     */
    public void resetForVegetation() {
        for (FlowerSpecies plant : speciesList) {
            plant.setB(0.0);
            plant.setS(0.0);

        }
    }

    /**
     * Applies winter resting phase to all species in the group.
     */
    public void resting_phase(Random rand) {
        //parse a number that stays the same, so that everyone gets the same outcome and the data
        //can be recreated.
        for (FlowerSpecies flowerSpecies : speciesList) {
            double randomDouble = rand.nextDouble() *
                    (flowerSpecies.getCUpper() - flowerSpecies.getCLower()) + flowerSpecies.getCLower();

            double newY = flowerSpecies.getY() * flowerSpecies.getS() * randomDouble;
            flowerSpecies.setY(newY);

        }
    }

    public ArrayList<FlowerSpecies> getSpeciesList() {
        return speciesList;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

}
