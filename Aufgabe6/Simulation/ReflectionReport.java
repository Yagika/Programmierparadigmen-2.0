package Simulation;
import Meta.*;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Creates a report utilizing reflection
 */
@ContextBReporter("Reporter")
@Responsible("Aleksandr")
public class ReflectionReport {
    public void printContextBReport(ProgramComponents components) {
        //Print names of all implemented interfaces, methods, classes
        HashMap<String, Integer> personsClassCount = new HashMap<>();
        HashMap<String, Integer> personsMethodsCount = new HashMap<>();
        HashMap<String, Integer> personsGuaranteeCount = new HashMap<>();

        for (Class<?> component : components.value()) {

            // Persons responsible for classes and interfaces
            Responsible responsible = component.getAnnotation(Responsible.class);

            // Count number of classes, interfaces and annotations that the person is responsible for
            if (!personsClassCount.containsKey(responsible.value())) {personsClassCount.put(responsible.value(), 1);}
            else {personsClassCount.put(responsible.value(), personsClassCount.get(responsible.value()) + 1);}

            if (!personsMethodsCount.containsKey(responsible.value())) {personsMethodsCount.put(responsible.value(), 0);}
            if (!personsGuaranteeCount.containsKey(responsible.value())) {personsGuaranteeCount.put(responsible.value(), 0);}

            // Invariants and history constraints of classes and interfaces
            Invariant invariant = component.getAnnotation(Invariant.class);
            HistoryConstraint historyConstraint = component.getAnnotation(HistoryConstraint.class);

            printClassData(component, responsible, invariant, historyConstraint);

            Method[] methods = component.getDeclaredMethods();

            // Print method names, signatures and guarantees
            int methodCounter = 0;
            int guaranteeCounter = 0;
            for (Method method : methods) {
                Precondition precondition = method.getAnnotation(Precondition.class);
                Postcondition postcondition = method.getAnnotation(Postcondition.class);

                if (precondition != null) {guaranteeCounter++;}
                if (postcondition != null) {guaranteeCounter++;}

                printMethodsData(method, precondition, postcondition);
                methodCounter++;
            }
            personsMethodsCount.put(responsible.value(), personsMethodsCount.get(responsible.value()) + methodCounter);
            personsGuaranteeCount.put(responsible.value(), personsGuaranteeCount.get(responsible.value())+ guaranteeCounter);
            System.out.println();
        }
        printClassCount(personsClassCount);
        System.out.println();
        printMethodsCount(personsMethodsCount);
        System.out.println();
        printGuaranteeCount(personsGuaranteeCount);
    }

    public void printClassData(Class<?> component, Responsible responsible, Invariant invariant, HistoryConstraint historyConstraint) {
        System.out.printf("Class name: %s, Responsible: %s \n", component.getName(), responsible.value());
        if (invariant != null) {
            System.out.printf("Invariant: %s \n", invariant.value());
        }
        if (historyConstraint != null) {
            System.out.printf("History Constraint: %s, \n", historyConstraint.value());
        }
    }

    public void printClassCount(HashMap<String, Integer> persons) {
        for (String key : persons.keySet()) {
            System.out.printf("%s is responsible for %d classes, interfaces and annotations \n", key, persons.get(key));
        }
    }

    public void printMethodsCount(HashMap<String, Integer> persons) {
        for (String key : persons.keySet()) {System.out.printf("%s is responsible for %d methods and constructors \n", key, persons.get(key));}
    }

    public void printGuaranteeCount(HashMap<String, Integer> persons) {
        for (String key : persons.keySet()) {System.out.printf("%s is responsible for %d guarantees \n", key, persons.get(key));}
    }

    public void printMethodsData(Method method, Precondition precondition, Postcondition postcondition) {
        System.out.printf("Method name: %s \n", method.getName());

        Class<?>[] parameters = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();

        if (parameters.length != 0) {
            System.out.print("Parameter types: ");
            for (Class<?> parameter : parameters) {
                System.out.printf(parameter.getName());
            }
            System.out.println();
        }

        System.out.printf("Return type: %s \n", returnType.getName());

        if (precondition != null) {
            System.out.printf("Precondition: %s", precondition.value());
        }
        if (postcondition != null) {System.out.printf("Post-condition: %s \n \n", postcondition.value());}
//        System.out.println();
    }


}
