package io.packstrap.transactionparsercli;

import org.springframework.shell.command.CommandRegistration;

public interface Discoverable {
    /**
     * Allows registering readers and writers that will be automatically discovered by the application.
     * @return String
     */
    String name();

    /**
     * Each reader and writer has its own option to register specific commands to the CLI, e.g: the CSV reader will
     * take as an argument a file argument whereas the S3 reader will take as an argument a bucket name and a file name.
     * Same approach applies for writers.
     * @return CommandRegistration
     */
    CommandRegistration command();
}
