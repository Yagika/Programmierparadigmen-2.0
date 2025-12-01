package Meta;

import java.lang.annotation.*;

@Responsible("Aleksander")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Invariant {
    String value();
}
