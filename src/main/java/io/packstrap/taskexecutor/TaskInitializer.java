package io.packstrap.taskexecutor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.command.CommandCatalog;
import org.springframework.stereotype.Service;

/**
 * The service is responsible for scanning the writers and readers and register their commands to spring-shell.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskInitializer {
    private final ApplicationContext applicationContext;
    private final CommandCatalog commandCatalog;

    @PostConstruct
    public void initialize() {
        log.info("Initializing tasks.");
        applicationContext.getBeansOfType(Discoverable.class)
                .values()
                .stream()
                .peek(discoverable -> log.debug("Registering: " + discoverable.getClass()))
                .forEach(discoverable -> commandCatalog.register(discoverable.command()));
    }
}
