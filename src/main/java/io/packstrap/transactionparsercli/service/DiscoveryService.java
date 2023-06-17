package io.packstrap.transactionparsercli.service;

import io.packstrap.transactionparsercli.Discoverable;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.command.CommandCatalog;
import org.springframework.shell.command.CommandOption;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The service is responsible for scanning the writers and readers and register their commands to spring-shell.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DiscoveryService {
    private final ApplicationContext applicationContext;
    private final CommandCatalog commandCatalog;

    @PostConstruct
    public void initialize() {
        List<CommandOption> commands = new ArrayList<>();

        commands.add(CommandOption.of(new String[]{"foo"}, new Character[]{'f'}, "this is foo description"));
        commands.add(CommandOption.of(new String[]{"bar"}, new Character[]{'b'}, "this is bar description"));

        commandCatalog.getRegistrations().get("read").getOptions()
                .addAll(commands);


//        applicationContext.getBeansOfType(Discoverable.class)
//                .values()
//                .stream()
//                .peek(discoverable -> log.debug("Registering: " + discoverable.name()))
//                .forEach(discoverable -> commandCatalog.register(discoverable.command()));
    }
}
