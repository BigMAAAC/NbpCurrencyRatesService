package eu.trans.api.v1.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Refresh cache by currency code eg. USD, EUR, etc.", parameters = {
        @Parameter(name = "currencyCode", description = "Currency code", required = true, example = "USD")
}, responses = {
        @ApiResponse(
                description = "Success",
                responseCode = "204"
        ),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
})
public @interface RefreshCacheByCurrencyCodeDoc {
}
