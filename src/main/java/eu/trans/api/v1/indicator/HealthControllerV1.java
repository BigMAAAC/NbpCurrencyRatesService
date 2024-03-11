package eu.trans.api.v1.indicator;

import eu.trans.api.v1.ControllerV1;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;


public interface HealthControllerV1 extends ControllerV1 {

    ResponseEntity<Health> checkHealth();
}
