package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SETTINGS_FILE = "src/main/resources/settings.txt";
    private static final String LOG_FILE = "src/main/resources/file.log";
    private String host;
    private int port;
    private String clientName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Logger logger;

    public Client() {
        setSettings(SETTINGS_FILE);
        logger = new Logger(LOG_FILE);
    }

    public Client(String settingsPath) {
        setSettings(settingsPath);
        logger = new Logger(LOG_FILE);
    }

    protected void setSettings(String settingsPath) {
        Settings settings = new Settings(settingsPath);
        this.host = settings.getHost();
        this.port = settings.getPort();
    }

    public void start() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name: ");
            this.clientName = scanner.nextLine();
            logger.log("You entered name: " + clientName);

            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(clientName);

            Thread thread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);
                        logger.log(serverMessage);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server");
                }
            });
            thread.start();

            String userInput;
            while (true) {
                userInput = scanner.nextLine();
                if ("/exit".equalsIgnoreCase(userInput)) {
                    out.println("/exit");
                    break;
                }
                out.println(userInput);
                logger.log("You: " + userInput);
            }
            logger.log("You left the chat");
            closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
