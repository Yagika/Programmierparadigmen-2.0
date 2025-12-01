import Simulation.Simulation;
import Meta.ProgramComponents;
import Meta.Responsible;

@ProgramComponents({
        Classes.Bee.class,
        Classes.BeeU.class,
        Classes.BeeV.class,
        Classes.BeeW.class,
        Classes.Flower.class,
        Classes.FlowerX.class,
        Classes.FlowerY.class,
        Classes.FlowerZ.class,
        Classes.Set.class,
        Simulation.Simulation.class,
        Meta.Precondition.class,
        Meta.Postcondition.class,
        Meta.Invariant.class,
        Meta.HistoryConstraint.class,
        Meta.ProgramComponents.class,
        Meta.Responsible.class,
        Meta.ContextBReporter.class
})
@Responsible("Yana")
public class Test {

    public static void main(String[] args) {
        // --- Kontext A ---
        for (int run = 1; run <= 3; run++) {
            System.out.println("===== Simulation Lauf " + run + " =====");
            Simulation sim = new Simulation();
            sim.run();
            sim.printStatistics();
            System.out.println();
        }

        // --- Kontext B ---
        ReflectionReport.printContextBReport();
    }
}
