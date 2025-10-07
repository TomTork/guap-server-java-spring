package ru.anotherworld.server.audit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationAuditListener {

    @EventListener
    public void handleSuccess(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        log.info("SUCCESSFUL LOGIN: user='{}', authorities={}", auth.getName(), auth.getAuthorities());
    }

    @EventListener
    public void handleFailure(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        String reason = event.getException().getMessage();
        log.warn("FAILED LOGIN: user='{}', reason='{}'", username, reason);
    }

    @EventListener
    public void handleLogout(LogoutSuccessEvent event) {
        log.info("LOGOUT: {}", event.getAuthentication().getName());
    }
}
