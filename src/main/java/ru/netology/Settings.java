package ru.netology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Settings {
    private int port;
    private String host;

    public Settings(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("port=")) {
                    port = Integer.parseInt(line.substring(5));
                }
                if (line.startsWith("host=")) {
                    host = line.substring(5);
                }
            }
        } catch (IOException e) {
            port = 80;
            host = "localhost";
        }
    }

    public int getPort() {
        return port;
    }
    public String getHost() {
        return host;
    }
}
