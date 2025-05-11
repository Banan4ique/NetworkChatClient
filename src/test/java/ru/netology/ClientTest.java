package ru.netology;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientTest {

    private Client client;
    private Socket mockSocket;
    private PrintWriter mockWriter;
    private BufferedReader mockReader;
    private ByteArrayOutputStream testOutput;
    private PipedInputStream testInput;
    private PipedOutputStream userInputSource;
    private CountDownLatch latch;

    @BeforeEach
    void setUp() throws IOException {
        mockSocket = mock(Socket.class);
        mockWriter = mock(PrintWriter.class);
        mockReader = mock(BufferedReader.class);

        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream("".getBytes()));

        testOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOutput));

        userInputSource = new PipedOutputStream();
        testInput = new PipedInputStream(userInputSource);
        System.setIn(testInput);

        client = new Client() {
            protected Socket createSocket(String address, int port) throws IOException {
                return mockSocket;
            }

            protected PrintWriter createWriter(OutputStream os) {
                return mockWriter;
            }

            protected BufferedReader createReader(InputStream is) {
                return mockReader;
            }
        };
    }

    @AfterEach
    public void tearDown() {
        verifyNoMoreInteractions(mockWriter); // убеждаемся, что после теста не было неожиданных вызовов
        verifyNoMoreInteractions(mockReader);
        verifyNoMoreInteractions(mockSocket);
    }

    @Test
    public void testConstructorDefault() {
        assertEquals("127.0.0.1", client.getHost());
        assertEquals(80, client.getPort());
    }

    @Test
    public void testSetSettings() {
        File tempFile = new File("test_settings.txt");
        try (PrintWriter writer = new PrintWriter(tempFile)) {
            writer.println("host=1.1.1.1");
            writer.println("port=12345");
        } catch (IOException e) {
            fail("Failed to create test settings file");
        }

        client.setSettings("test_settings.txt");
        assertEquals(12345, client.getPort());
        assertEquals("1.1.1.1", client.getHost());

        tempFile.delete();
    }

    @Test
    public void testCloseConnection() throws IOException {
        client.closeConnection();
        verify(mockWriter, never()).close(); // метод close() для mockIn не вызывается!
        verify(mockReader, never()).close();
        verify(mockSocket, never()).close();
    }
}
