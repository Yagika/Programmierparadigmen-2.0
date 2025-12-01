package Meta;

import java.lang.annotation.*;

@Responsible("Aleksandr")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Responsible {
    String value();   // имя/инициалы участника
}
