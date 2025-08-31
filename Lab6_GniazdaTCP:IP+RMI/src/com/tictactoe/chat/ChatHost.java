package com.tictactoe.chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Klasa ChatHost – tworzy nasłuch (ServerSocket) na wskazanym porcie.
 * Gdy port = 0, system przydziela wolny ephemeral port.
 * Następnie czeka na JEDNO połączenie od przeciwnika.
 * Po nawiązaniu połączenia utrzymuje je w pętli, umożliwiając wymianę wiadomości.
 */
public class ChatHost {

    private int port;               // może być 0, jeśli chcemy ephemeral
    private ServerSocket serverSocket;
    private Socket connection;

    private BufferedReader inputFromOpponent;
    private OutputStream outputToOpponent;

    /**
     * Konstruktor – zapisuje port do pola.
     * Jeśli port = 0, system nada ephemeral w momencie tworzenia ServerSocket.
     */
    public ChatHost(int port) {
        this.port = port;
    }

    /**
     * Metoda uruchamiająca nasłuch – tworzy serverSocket,
     * ustawia faktyczny port i czeka na jedno połączenie (accept()).
     */
    public void startHost() throws Exception {
        // Tworzymy ServerSocket.
        // Jeśli port = 0 -> system przydzieli wolny, ephemeral port.
        serverSocket = new ServerSocket(port);

        // Odczytujemy faktyczny (rzeczywisty) port, na którym nasłuchujemy
        this.port = serverSocket.getLocalPort();

        System.out.println("[ChatHost] Nasłuchiwanie na porcie: " + this.port);

        // Blokujące czekanie na połączenie przeciwnika
        connection = serverSocket.accept();
        System.out.println("[ChatHost] Przeciwnik połączył się z: " + connection.getRemoteSocketAddress());

        // Strumienie do odbierania / wysyłania
        inputFromOpponent = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        outputToOpponent = connection.getOutputStream();

        // Uruchamiamy wątek do odbierania wiadomości (czytamy w kółko, dopóki połączenie trwa)
        new Thread(() -> {
            try {
                String line;
                while ((line = inputFromOpponent.readLine()) != null) {
                    System.out.println("Wiadomość od przeciwnika: " + line);
                }
            } catch (Exception e) {
                System.err.println("[ChatHost] Błąd w odbieraniu wiadomości.");
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Metoda do wysyłania wiadomości do przeciwnika po tym samym socketcie.
     */
    public void sendMessage(String msg) {
        try {
            if (outputToOpponent == null) {
                System.err.println("[ChatHost] Połączenie jeszcze nie jest gotowe!");
                return;
            }
            msg += "\n";  // readLine() po drugiej stronie czeka na koniec linii
            outputToOpponent.write(msg.getBytes());
            outputToOpponent.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Zwraca faktyczny port, na którym Host nasłuchuje (po wywołaniu startHost()).
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Metoda zamykająca sockety.
     */
    public void close() {
        try {
            if (connection != null) connection.close();
            if (serverSocket != null) serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
