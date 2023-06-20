package io.packstrap.taskexecutor;

import org.springframework.shell.command.CommandContext;

public interface Task {
    enum TaskType {
        READER,
        WRITER,
    }
    Object execute(CommandContext input);
}
