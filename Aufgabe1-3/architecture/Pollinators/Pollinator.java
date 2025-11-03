package architecture.Pollinators;

/**
 * Interface for all pollinator species participating in the simulation.
 * It defines methods required to model bees interaction with plants and the environment.
 * <p>
 * STYLE: object-oriented – nominal abstraction that enables polymorphism.
 */
public interface Pollinator {

    /**
     * @return species display name
     * NOTE: must return non-null identifier for console output
     */
    String getName();

    /**
     * @return current population size (individuals)
     */
    double getPopulation();

    /**
     * Sets population (implementations should enforce a viable minimum).
     */
    void setPopulation(double v);

    /**
     * @return daily activity multiplier (≈ 0.1..1.8)
     */
    double getActivity();

    /**
     * @return intrinsic per-visit effectiveness multiplier
     */
    double getEffectiveness();

    /**
     * @return X coordinate of the nest/hive in the environment
     */
    double getX();

    /**
     * @return Y coordinate of the nest/hive in the environment
     */
    double getY();

    /**
     * @return whether the species is seasonally active on a given day (0..240)
     */
    boolean isActive(int day);

    /**
     * Update the population for the next day based on current food and reserves.
     *
     * @param n       today's total food available in the landscape
     * @param reserve stored food reserve (shared environment reserve)
     *                PRECONDITION: n >= 0, reserve >= 0
     *                POSTCONDITION: – population >= 5
     */
    void updatePopulation(double n, double reserve);

    /**
     * Update the daily activity considering seasonal cycle and weather.
     *
     * @param day     vegetation day (1..240)
     * @param weather current weather state
     *                POSTCONDITION: activity within [0.1, 1.8]
     */
    void updateActivity(int day, architecture.Weather weather);
}
