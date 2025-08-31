package com.tictactoe.server;

import com.tictactoe.rmi.GameObserver;

import java.util.*;


public class GameRoom {

    private final String roomId;
    private final String[] players = new String[2];
    private final String[] playerIPs = new String[2];
    private final int[] playerPorts = new int[2];

    private final char[][] board = new char[3][3];
    private final List<GameObserver> observers = new ArrayList<>();

    /** Indeks aktualnego gracza (0 lub 1). */
    private int currentPlayerIndex = 0;

    /**
     * Mapa statystyk dla każdego gracza w tej sesji.
     * Key: nazwa gracza (playerName), Value: obiekt Stats przechowujący wins/draws/losses.
     */
    private final Map<String, Stats> statsMap = new HashMap<>();

    /**
     * Prosta klasa wewnętrzna do przechowywania statystyk jednego gracza.
     */
    private static class Stats {
        int wins;
        int draws;
        int losses;

        @Override
        public String toString() {
            return "Wins=" + wins + ", Draws=" + draws + ", Losses=" + losses;
        }
    }

    /**
     * Konstruktor: tworzy nowy pokój gry i ustawia pierwszego gracza.
     * Inicjuje też statystyki dla tego gracza.
     */
    public GameRoom(String roomId, String player1) {
        this.roomId = roomId;
        this.players[0] = player1;

        // Inicjalizujemy planszę na '-'
        for (char[] row : board) {
            Arrays.fill(row, '-');
        }

        // Inicjujemy statystyki dla gracza player1:
        statsMap.put(player1, new Stats());
    }

    /**
     * Sprawdza, czy w pokoju są już 2 osoby (pełny pokój).
     */
    public boolean isFull() {
        return players[1] != null;
    }

    /**
     * Dodaje drugiego gracza (jeśli pokój nie jest pełny).
     * Inicjuje jego statystyki w `statsMap`.
     */
    public void addPlayer(String player2) {
        if (!isFull()) {
            players[1] = player2;
            statsMap.put(player2, new Stats()); // Inicjujemy statystyki gracza2
        }
    }

    /**
     * Rejestracja IP i portu danego gracza w polach playerIPs/playerPorts.
     */
    public void setPlayerIPAndPort(String playerName, String ipAddress, int port) {
        if (players[0] != null && players[0].equals(playerName)) {
            playerIPs[0] = ipAddress;
            playerPorts[0] = port;
        } else if (players[1] != null && players[1].equals(playerName)) {
            playerIPs[1] = ipAddress;
            playerPorts[1] = port;
        }
    }

    /**
     * Zwraca IP przeciwnika danego gracza.
     */
    public String getOpponentIP(String playerName) {
        if (players[0] != null && players[0].equals(playerName)) {
            return playerIPs[1];
        } else if (players[1] != null && players[1].equals(playerName)) {
            return playerIPs[0];
        }
        return null;
    }

    /**
     * Zwraca port przeciwnika danego gracza.
     */
    public int getOpponentPort(String playerName) {
        if (players[0] != null && players[0].equals(playerName)) {
            return playerPorts[1];
        } else if (players[1] != null && players[1].equals(playerName)) {
            return playerPorts[0];
        }
        return -1;
    }

    /**
     * Dodaje obserwatora RMI (klient), aby serwer mógł wywoływać jego metodę update(gameState).
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Powiadamia wszystkich obserwatorów o aktualnym stanie gry (np. po ruchu, wygranej).
     */
    public void notifyObservers(String gameState) {
        for (GameObserver obs : observers) {
            try {
                obs.update(gameState);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Główna metoda wykonywania ruchu przez danego gracza (playerName) na pozycji (1-9).
     * Zwraca tekstowy komunikat (wygrana, remis, aktualny stan, itp.).
     */
    public String makeMove(String playerName, int position) {
        // Sprawdź, czy to kolej właściwego gracza
        if (!playerName.equals(players[currentPlayerIndex])) {
            return "Nie Twoja kolej.";
        }

        // Wyliczamy wiersz/kolumnę
        int row = (position - 1) / 3;
        int col = (position - 1) % 3;

        // Sprawdź, czy pole jest wolne
        if (board[row][col] != '-') {
            return "Pole jest już zajęte.";
        }

        // Wstaw 'X' lub 'O'
        board[row][col] = (currentPlayerIndex == 0) ? 'X' : 'O';

        // Sprawdzamy, czy ktoś wygrał
        if (checkWinner()) {
            String winner = players[currentPlayerIndex];
            String loser  = players[1 - currentPlayerIndex];

            // Aktualizujemy statystyki
            statsMap.get(winner).wins++;
            statsMap.get(loser).losses++;

            // Log w konsoli serwera
            printStatsInConsole();

            // Powiadom klientów
            notifyObservers("Gracz " + winner
                    + " (" + (currentPlayerIndex == 0 ? 'X' : 'O') + ") wygrywa!\n"
                    + getGameState());

            resetBoard();
            return "Gracz " + winner + " wygrywa!";
        }

        // Sprawdzamy remis
        if (isBoardFull()) {
            // Obaj gracze +1 do remisów
            for (String p : players) {
                if (p != null) {
                    statsMap.get(p).draws++;
                }
            }

            printStatsInConsole();

            notifyObservers("Remis!\n" + getGameState());
            resetBoard();
            return "Remis!";
        }

        // Zmiana kolejki
        currentPlayerIndex = 1 - currentPlayerIndex;

        // Powiadom klientów o aktualnym stanie gry
        notifyObservers("Aktualny stan gry:\n" + getGameState());

        return "Ruch zaakceptowany. Aktualny stan gry:\n" + getGameState();
    }

    /**
     * Zwraca aktualny (tekstowy) stan planszy wraz z nazwami graczy.
     */
    public String getGameState() {
        StringBuilder sb = new StringBuilder();
        sb.append("Gracz 1: ").append(players[0]).append(" (X)\n");
        if (players[1] != null) {
            sb.append("Gracz 2: ").append(players[1]).append(" (O)\n");
        }
        sb.append("  1 | 2 | 3 \n");
        sb.append(" ---+---+---\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                sb.append(" ").append(board[i][j]).append(" ");
                if (j < board[i].length - 1) {
                    sb.append("|");
                }
            }
            sb.append("\n");
            if (i < board.length - 1) {
                sb.append(" ---+---+---\n");
            }
        }
        return sb.toString();
    }

    /**
     * Sprawdza, czy obecny stan tablicy oznacza zwycięstwo aktualnego gracza.
     */
    private boolean checkWinner() {
        // Sprawdź wiersze
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '-'
                    && board[i][0] == board[i][1]
                    && board[i][1] == board[i][2]) {
                return true;
            }
        }
        // Sprawdź kolumny
        for (int i = 0; i < 3; i++) {
            if (board[0][i] != '-'
                    && board[0][i] == board[1][i]
                    && board[1][i] == board[2][i]) {
                return true;
            }
        }
        // Sprawdź diagonale
        if (board[0][0] != '-'
                && board[0][0] == board[1][1]
                && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != '-'
                && board[0][2] == board[1][1]
                && board[1][1] == board[2][0]) {
            return true;
        }
        return false;
    }

    /**
     * Sprawdza, czy cała plansza została zapełniona (bez zwycięzcy => remis).
     */
    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Resetuje planszę do stanu początkowego.
     */
    private void resetBoard() {
        for (char[] row : board) {
            Arrays.fill(row, '-');
        }
        // Gracz 1 (index=0) zaczyna nową partię
        currentPlayerIndex = 0;
    }

    /**
     * Metoda pomocnicza: wypisuje w konsoli serwera (System.out)
     * aktualne statystyki obu graczy w pokoju "roomId".
     */
    private void printStatsInConsole() {
        System.out.println("[STATS - Room " + roomId + "]");
        for (String p : players) {
            if (p != null) {
                Stats s = statsMap.get(p);
                System.out.println("  " + p + " -> " + s);
            }
        }
    }
}
