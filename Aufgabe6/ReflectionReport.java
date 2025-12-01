import Meta.ProgramComponents;
import Meta.ContextBReporter;
import Meta.Responsible;


/**
 * Helper to trigger Context B reporting from Test.
 */
@Responsible("Aleksander")
public class ReflectionReport {

    public static void printContextBReport() {
        Class<Test> testClass = Test.class;

        ProgramComponents pc = testClass.getAnnotation(ProgramComponents.class);
        if (pc == null) {
            System.out.println("Keine @ProgramComponents Annotation auf Test gefunden.");
            return;
        }

        Class<?>[] components = pc.value();
        ContextBReporter.printReport(components);
    }
}

