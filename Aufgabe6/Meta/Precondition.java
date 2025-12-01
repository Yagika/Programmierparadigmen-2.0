package Meta;

import java.lang.annotation.*;

@Responsible("Aleksandr")
@Invariant("A Precondition annotation always contains a human-readable description of an assumed condition.")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Precondition {
    String value();
}
