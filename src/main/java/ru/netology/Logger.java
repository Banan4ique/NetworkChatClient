package ru.netology;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String TIME_PATTERN = "dd-MM-yyyy, kk:mm:ss";
    private String logFile;

    public Logger(String logFile) {
        this.logFile = logFile;
    }

    public void log(String message) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
            bw.write("[" +LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_PATTERN)) + "] " + message + "\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
