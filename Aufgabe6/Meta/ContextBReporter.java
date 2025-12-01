package Meta;

import java.lang.annotation.*;

@Responsible("Aleksandr")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ContextBReporter {
    String value();
}
