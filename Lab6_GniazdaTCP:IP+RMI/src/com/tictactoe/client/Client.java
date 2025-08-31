package com.tictactoe.client;

import com.tictactoe.chat.ChatHost;
import com.tictactoe.chat.ChatClient;
import com.tictactoe.rmi.GameObserver;
import com.tictactoe.rmi.GameService;

import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import static java.lang.System.*;

public class Client extends UnicastRemoteObject implements GameObserver {

    private ChatHost chatHost;
    private ChatClient chatClient;

    protected Client() throws Exception {
        super(); // eksport do RMI
    }

    /**
     * Metoda wywoływana przez serwer, gdy zmienia się stan gry
     * (np. po ruchu innego gracza).
     */
    @Override
    public void update(String gameState) {
        out.println("\n[UPDATE] Zaktualizowany stan gry:\n" + gameState);
    }

    public static void main(String[] args) {
        try {
            // Połączenie z rejestrem RMI
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            GameService gameService = (GameService) registry.lookup("GameService");

            Scanner scanner = new Scanner(in);

            out.print("Podaj swoje imię: ");
            String playerName = scanner.nextLine();

            out.println("1. Utwórz pokój");
            out.println("2. Dołącz do pokoju");
            int choice = scanner.nextInt();
            scanner.nextLine(); // zjada Enter

            // Tworzymy obiekt klienta (RMI)
            Client client = new Client();

            String roomId;
            if (choice == 1) {
                // ================== HOST (tworzący pokój) ==================
                roomId = gameService.createRoom(playerName);
                out.println("Utworzono pokój: " + roomId);

                // Rejestrujemy obserwatora RMI
                gameService.registerObserver(roomId, client);

                // Tworzymy ChatHost na porcie 0 (ephemeral)
                client.chatHost = new ChatHost(0);

                // Uruchamiamy startHost() w osobnym wątku, żeby accept() nie blokował maina
                new Thread(() -> {
                    try {
                        client.chatHost.startHost();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

                // Dajemy krótką pauzę na zbindowanie portu
                Thread.sleep(500);

                // Rejestrujemy IP i port w serwerze
                String localIP = InetAddress.getLocalHost().getHostAddress();
                int localPort = client.chatHost.getPort();
                gameService.registerPlayerIPAndPort(roomId, playerName, localIP, localPort);

                // Oczekiwanie na przeciwnika
                out.println("Oczekiwanie na przeciwnika...");
                while (true) {
                    String oppIP = gameService.getOpponentIP(roomId, playerName);
                    int oppPort = gameService.getOpponentPort(roomId, playerName);
                    if (oppIP != null && oppPort > 0) {
                        out.println("Przeciwnik dołączył! IP: " + oppIP + " | Port: " + oppPort);
                        break;
                    }
                    Thread.sleep(2000);
                    break;
                }

            } else {
                // ================== KLIENT (dołączający do pokoju) ==================
                out.print("Podaj ID pokoju: ");
                roomId = scanner.nextLine();

                String response = gameService.joinRoom(roomId, playerName);
                out.println(response);

                // Rejestrujemy obserwatora RMI
                gameService.registerObserver(roomId, client);

                // Rejestrujemy własne IP i port=0 (nie hostujemy)
                String localIP = InetAddress.getLocalHost().getHostAddress();
                gameService.registerPlayerIPAndPort(roomId, playerName, localIP, 0);

                // Pętla pobierania IP i portu hosta
                while (true) {
                    String oppIP = gameService.getOpponentIP(roomId, playerName);
                    int oppPort = gameService.getOpponentPort(roomId, playerName);
                    if (oppIP != null && oppPort > 0) {
                        out.println("Znaleziono hosta czatu: " + oppIP + ":" + oppPort);

                        // Tworzymy ChatClient i łączymy się
                        client.chatClient = new ChatClient(oppIP, oppPort);
                        client.chatClient.connect();
                        break;
                    }
                    out.println("Czekam na rejestrację hosta...");
                    //Thread.sleep(1000);
                }
            }

            // --------------- Pętla główna: wczytywanie ruchów lub wiadomości ---------------
            out.println("\nMożesz teraz wpisywać ruch (1–9) lub wiadomość (cokolwiek innego).");
            while (true) {
                out.print(">> ");
                String input = scanner.nextLine();

                try {
                    int move = Integer.parseInt(input);
                    if (move >= 1 && move <= 9) {
                        // Wykonanie ruchu (metoda RMI na serwerze)
                        String result = gameService.makeMove(roomId, playerName, move);
                        out.println(result);
                    } else {
                        out.println("Niepoprawny ruch. Podaj liczbę 1-9.");
                    }
                } catch (NumberFormatException e) {
                    // Czatujemy
                    if (client.chatHost != null) {
                        // Host
                        client.chatHost.sendMessage(input);
                    } else if (client.chatClient != null) {
                        // Dołączający
                        client.chatClient.sendMessage(input);
                    } else {
                        err.println("Błąd: brak ChatHost ani ChatClient.");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
