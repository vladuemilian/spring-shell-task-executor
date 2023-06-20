package io.packstrap.taskexecutor;

import org.springframework.shell.command.CommandRegistration;

public interface Discoverable {
    /**
     * Registers the command to spring-shell.
     *
     * @return
     */
    CommandRegistration command();
}
