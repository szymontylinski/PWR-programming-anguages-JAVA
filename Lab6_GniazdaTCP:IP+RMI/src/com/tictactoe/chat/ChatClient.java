package com.tictactoe.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * ChatClient – łączy się do hosta (ChatHost) na IP:port,
 * utrzymuje połączenie i w osobnym wątku odbiera wiadomości.
 */
public class ChatClient {

    private final String hostIP;
    private final int hostPort;

    private Socket socket;
    private BufferedReader inputFromOpponent;
    private OutputStream outputToOpponent;

    public ChatClient(String hostIP, int hostPort) {
        this.hostIP = hostIP;
        this.hostPort = hostPort;
    }

    /**
     * Nawiązanie połączenia z hostem.
     */
    public void connect() throws Exception {
        socket = new Socket(hostIP, hostPort);
        System.out.println("[ChatClient] Połączono do hosta " + hostIP + ":" + hostPort);

        inputFromOpponent = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputToOpponent = socket.getOutputStream();

        // Wątek stale czytający wiadomości od "hosta"
        new Thread(() -> {
            try {
                String line;
                while ((line = inputFromOpponent.readLine()) != null) {
                    System.out.println("Wiadomość od przeciwnika: " + line);
                }
            } catch (Exception e) {
                System.err.println("[ChatClient] Błąd w wątku odbierającym.");
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Wysyłanie wiadomości do przeciwnika (hosta).
     */
    public void sendMessage(String msg) {
        try {
            if (outputToOpponent == null) {
                System.err.println("[ChatClient] Połączenie nie jest gotowe!");
                return;
            }
            msg += "\n";
            outputToOpponent.write(msg.getBytes());
            outputToOpponent.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

