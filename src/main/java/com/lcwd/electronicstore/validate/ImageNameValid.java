package com.lcwd.electronicstore.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {      // This is Custom annotation

    // error message
    String message() default "Invalid image name!!";

    // represent group of Constraints
    Class<?>[] groups() default {};

     //additional information about annotation
     Class<? extends Payload>[] payload() default {};
}
