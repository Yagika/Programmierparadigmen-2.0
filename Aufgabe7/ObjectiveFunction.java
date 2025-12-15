import java.util.function.Function;

@FunctionalInterface
public interface ObjectiveFunction extends Function<Location, Double> {
}
