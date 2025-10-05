package by.dshparko.gatewayservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackupController {

    @CircuitBreaker(name = "notificationService", fallbackMethod = "mailBackup")
    @RequestMapping("/backup/users")
    public ResponseEntity<String> userBackup() {
        return ResponseEntity.ok("user-service isn't available.");
    }

    @RequestMapping("/backup/mail")
    public ResponseEntity<String> mailBackup() {
        return ResponseEntity.ok("notification-service isn't available.");
    }
}
