package io.packstrap.taskexecutor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.command.CommandCatalog;

import static org.junit.jupiter.api.Assertions.*;

public class TaskInitializerTest {

    private final ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
    private final CommandCatalog commandCatalog = Mockito.mock(CommandCatalog.class);
    private final TaskInitializer taskInitializer = new TaskInitializer(applicationContext, commandCatalog);

    @Test
    public void should_discover_beans() {

    }
}