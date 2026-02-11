package architecture;

import architecture.Pollinators.Bee;

import java.util.ArrayList;
import java.util.Random;


/**
 * Class to represent a flower species with their parameters.
 * Handles its own state changes during simulation
 * <p>
 * STYLE: object-oriented – each species maintains its own state and transitions.
 */
public class FlowerSpecies {
    private double y; // Growth strength (>=0)
    private double b = 0; // Bloom amount (0..1)
    private double s = 0; // Seed quality (0..1)
    private final double c_lower, c_upper; // Reproduction limits
    private final double f_lower, f_upper; // Moisture limits (0 < f_lower < f_upper < 1)
    private final double h_lower, h_upper; // Sunlight limits (hours, cumulative)
    private final double q; // Bloom intensity (0 < q < 1/15)
    private final double p; // Pollination probability (0 < p < 1/(h_upper - h_lower))
    private final double brightness; // Flower brightness (affects bee preference)

    /**
     * Constructs a plant species with given biological parameters.
     */
    public FlowerSpecies(double y, double c_lower, double c_upper, double f_lower, double f_upper,
                         double h_lower, double h_upper, double q, double p, double brightness) {
        this.y = Math.max(0.0, y);
        this.c_lower = c_lower;
        this.c_upper = c_upper;
        this.f_lower = f_lower;
        this.f_upper = f_upper;
        this.h_lower = h_lower;
        this.h_upper = h_upper;
        this.q = q;
        this.p = p;
        this.brightness = brightness;
    }

    /**
     * Calculates food value provided by this flower species (y * b).
     *
     * @return today's food contribution
     */
    public double foodvalue() { //Nahrungsangebot (food supply) n SUM_i(y_i * b_i), in here its the sum of the individual plant,
        return y * b;           //at the end it should be summed up (n for bee population calculation!)
    }

    /**
     * Simulates the resting (winter) phase: y is multiplied by s and by a random
     * factor in [c_lower, c_upper]. Random is passed from outside to control seeding.
     */
    @Deprecated
    public void resting_phase(Random rand) {
        //parse a number that stays the same, so that everyone gets the same outcome and the data
        //can be recreated.
        double randomFactor = rand.nextDouble() * (c_upper - c_lower) + c_lower;
        this.y = this.y * this.s * randomFactor;
        if (this.y < 0) this.y = 0;
    }

    /**
     * Adjusts growth (y) depending on soil moisture (f).
     */
    public void moisture_threshold(double f) { //this calculates new y value based on if a certain f_lower and f_upper threshold is reached
        if (((this.f_lower / 2.0) < f && f < this.f_lower) || (this.f_upper < f && f < (2.0 * this.f_upper))) {
            this.y = this.y * 0.99;
        }
        if (f <= (f_lower / 2.0) || f >= (2.0 * f_upper)) {
            this.y = this.y * 0.97;
        }
        if (this.y < 0) this.y = 0;
    }

    // Sunhours h = sum of sunshine-time d over the time of the vegetationperiod so far.

    /**
     * Changes blooming state based on sunlight amount.
     */
    public void bloom_time(double h, double d) { //changes the bloom-state of the given flower based on sunlight and suntime
        double period = Math.max(1.0, this.h_upper);
        double phase = h % period;
        boolean inWindow = (this.h_lower <= phase && phase < this.h_upper);
        if (inWindow) {
            this.b += (q * (d + 3.0));
            if (this.b > 1.0) this.b = 1.0;
        } else {
            this.b -= (q * (1.5 + 0.1 * d));
            if (this.b < 0.0) this.b = 0.0;
        }
    }

    /**
     * Increases seed quality (s) based on pollination probability and bee activity.
     *
     * @param bees            list of bees that can pollinate this plant
     * @param total_foodvalue n = sum yi*bi across all species
     * @param d               today's sunshine (0..12)
     */
    @Deprecated
    public void pollination_probability(ArrayList<Bee> bees, double totalBees, double total_foodvalue, double d) {
        if (total_foodvalue <= 0.0) {
            // no flowers in bloom -> no seed increase
            return;
        }

        double preference; // preference multiplier for bees that prefer this plant based on color intensity

        for (Bee bee : bees) {
            if (bee.getPrefLower() <= this.brightness && this.brightness <= bee.getPrefUpper()) {
                preference = 1.25;
            } else {
                preference = 0.75;
            }

            double beeActivity = bee.getActivity();

            if (totalBees >= total_foodvalue) {
                this.s += this.p * this.b * (d + 1.0) * beeActivity * preference / 5;
            } else {
                this.s += this.p * this.b * (d + 1.0) * (totalBees / total_foodvalue) *
                        beeActivity * preference / 5;
            }
            if (this.s > 1.0) this.s = 1.0;
            if (this.s < 0.0) this.s = 0.0;
        }
    }

    /**
     * Copy constructor to create new object with same parameters.
     */
    public FlowerSpecies copy() {
        FlowerSpecies f = new FlowerSpecies(this.y, this.c_lower, this.c_upper, this.f_lower, this.f_upper,
                this.h_lower, this.h_upper, this.q, this.p, this.brightness);
        f.b = this.b;
        f.s = this.s;
        return f;
    }

    @Override
    public String toString() {
        return String.format("Flower [y=%.2f, b=%.2f, s=%.2f, f=(%.2f-%.2f), h=(%.1f-%.1f)]",
                y, b, s, f_lower, f_upper, h_lower, h_upper);
    }

    public double getY() {
        return y;
    }

    public double getB() {
        return b;
    }

    public double getS() {
        return s;
    }

    public void setY(double y) {
        this.y = Math.max(0.0, y);
    }

    public void setB(double b) {
        this.b = Math.max(0.0, Math.min(1.0, b));
    }

    public void setS(double s) {
        this.s = Math.max(0.0, Math.min(1.0, s));
    }

    public double getCLower() {
        return c_lower;
    }

    public double getCUpper() {
        return c_upper;
    }

    public double getBrightness() {
        return brightness;
    }
}



