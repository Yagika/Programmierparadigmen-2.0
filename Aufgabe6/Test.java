import Bee.*;
import Meta.*;
import Simulation.*;

/**
 * Entry point for the program.
 * Runs several simulations and then triggers Context B reporting.
 */
@ProgramComponents({
        Bee.class,
        BeeU.class,
        BeeV.class,
        BeeW.class,
        BeeStatistics.class,
        Flower.Flower.class,
        Flower.FlowerX.class,
        Flower.FlowerY.class,
        Flower.FlowerZ.class,
        Flower.FlowerStatistics.class,
        Set.class,
        Simulation.class,
        ContextBReporter.class,
        Meta.Precondition.class,
        Meta.Postcondition.class,
        Meta.Invariant.class,
        Meta.HistoryConstraint.class,
        Meta.ProgramComponents.class,
        Meta.Responsible.class,
        Meta.ContextBReporter.class,
        ReflectionReport.class,
        Test.class
})
@Responsible("Yana")
public class Test {

    public static void main(String[] args) {
        // --- Context A ---
        for (int run = 1; run <= 3; run++) {
            System.out.println("===== Simulation run " + run + " =====");
            Simulation sim = new Simulation();
            sim.run();
            sim.printStatistics();
            System.out.println();
        }

        // --- Context B ---
        ProgramComponents components = Test.class.getAnnotation(ProgramComponents.class);

        ReflectionReport reporter = new ReflectionReport();
        reporter.printContextBReport(components);
    }
}