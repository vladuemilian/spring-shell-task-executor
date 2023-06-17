package io.packstrap.transactionparsercli.controller;

import io.packstrap.transactionparsercli.TransactionParserCliApplication;
import io.packstrap.transactionparsercli.service.TransactionParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Arrays;

import static org.springframework.shell.command.invocation.InvocableShellMethod.log;

@ShellComponent
@RequiredArgsConstructor
public class CommandLineController {
    private TransactionParserService transactionParserService;

    private static final String SUCCESS_RESPONSE = "Operation performed successfully";
    @ShellMethod(key = "read")
    public String read(
            @ShellOption(defaultValue = "args") String[] args
    ) {
//        ApplicationHome applicationHome = new ApplicationHome(TransactionParserCliApplication.class);
//        String jarFolder = applicationHome.getDir().getAbsolutePath();
//        System.out.println("FOLDER: " + jarFolder);
        //transactionParserService.read()
        log.info(String.join(" ", args));
        return "Hello world ";
    }

    @ShellMethod(key = "write")
    public String write(
            @ShellOption(defaultValue = "spring") String arg
    ) {
        return "Hello world " + arg;
    }

    @ShellMethod(key = "read-write")
    public String readWrite(
            @ShellOption(defaultValue = "spring") String arg
    ) {
        return "Hello world " + arg;
    }
}
