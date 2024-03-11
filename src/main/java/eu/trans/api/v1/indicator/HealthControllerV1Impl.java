package eu.trans.api.v1.indicator;

import eu.trans.api.v1.docs.CheckHealthDoc;
import eu.trans.api.v1.docs.HealthControllerV1Doc;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@HealthControllerV1Doc
@RestController
@RequiredArgsConstructor
public class HealthControllerV1Impl implements HealthControllerV1 {

    private final CustomHealthIndicator healthIndicator;

    @Override
    @GetMapping("/health")
    @CheckHealthDoc
    public ResponseEntity<Health> checkHealth() {

        return ResponseEntity
                .ok(healthIndicator.health());
    }
}
