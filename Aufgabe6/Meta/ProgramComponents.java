package Meta;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProgramComponents {
    Class<?>[] value();
}
