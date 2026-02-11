package Meta;

import java.lang.annotation.*;

@Responsible("Aleksandr")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Postcondition {
    String value();
}
