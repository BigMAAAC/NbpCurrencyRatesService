package eu.trans.api.v1.docs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@OpenAPIDefinition(
        info =
        @Info(
                title = "NBP api middleware currency rates service",
                version = "v1",
                description = "API NBP Middleware documentation."))
public @interface ControllerV1Doc {
}
