package com.tictactoe.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa do obsługi chatu TCP pomiędzy dwoma graczami.
 */
public class ChatHandler {
    private final int port;

    /**
     * Konstruktor – próbuje zarezerwować wskazany port.
     * Jeśli port to 0 -> pobiera losowy wolny port.
     * Jeśli jest zajęty -> również pobiera losowy.
     */
    public ChatHandler(int desiredPort) throws Exception {
        if (desiredPort == 0) {
            // Użytkownik wprost chce losowy port:
            this.port = new ServerSocket(0).getLocalPort();
        } else {
            // Spróbuj desiredPort – jeśli BindException, to weź losowy
            int temp;
            try (ServerSocket tempSocket = new ServerSocket(desiredPort)) {
                temp = tempSocket.getLocalPort();
            } catch (BindException e) {
                System.out.println("Port " + desiredPort + " zajęty. Przydzielam losowy port...");
                temp = new ServerSocket(0).getLocalPort();
            }
            this.port = temp;
        }
    }

    /**
     * Zwraca port, na którym faktycznie nasłuchujemy.
     */
    public int getPort() {
        return port;
    }

    /**
     * Uruchamia w osobnym wątku serwer nasłuchujący na połączenia
     * (odbiera pojedyncze wiadomości tekstowe i wyświetla je w konsoli).
     */
    public void startChatListener() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("[ChatHandler] Nasłuchiwanie na porcie: " + port);

                while (true) {
                    // Każde połączenie to jedna wiadomość (jedna linia)
                    try (Socket clientSocket = serverSocket.accept()) {
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                        String message = in.readLine();
                        if (message != null) {
                            System.out.println("Wiadomość od przeciwnika: " + message);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Wysyła wiadomość do przeciwnika na podane IP i port.
     * Jeśli IP == null lub port <= 0, wyświetla komunikat o błędzie.
     */
    public void sendMessage(String ip, int port, String message) {
        new Thread(() -> {
            if (ip == null || port <= 0) {
                System.err.println("[ChatHandler] Brak poprawnego IP lub portu przeciwnika.");
                return;
            }
            try (Socket socket = new Socket(ip, port)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
            } catch (Exception e) {
                System.err.println("[ChatHandler] Błąd: Nie można wysłać wiadomości do "
                        + ip + ":" + port);
                e.printStackTrace();
            }
        }).start();
    }
}
