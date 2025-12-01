import Meta.ProgramComponents;
import Meta.ContextBReporter00;
import Meta.Responsible;


/**
 * Helper to trigger Context B reporting from Test.
 */
@Responsible("Aleksander")
public class ReflectionReport {

    public static void printContextBReport() {
        Class<Test00> testClass = Test00.class;

        ProgramComponents pc = testClass.getAnnotation(ProgramComponents.class);
        if (pc == null) {
            System.out.println("Keine @ProgramComponents Annotation auf Test gefunden.");
            return;
        }

        Class<?>[] components = pc.value();
        ContextBReporter00.printReport(components);
    }
}
