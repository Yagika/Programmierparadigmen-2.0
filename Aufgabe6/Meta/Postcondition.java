package Meta;

import java.lang.annotation.*;

@Responsible("Aleksander")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Postcondition {
    String value();
}
