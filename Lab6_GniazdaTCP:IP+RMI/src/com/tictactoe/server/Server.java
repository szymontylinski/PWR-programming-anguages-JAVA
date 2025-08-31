package com.tictactoe.server;

import com.tictactoe.rmi.GameObserver;
import com.tictactoe.rmi.GameService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class Server implements GameService {

    private final Map<String, GameRoom> rooms = new HashMap<>();

    public static void main(String[] args) {
        try {
            Server server = new Server();
            GameService stub = (GameService) UnicastRemoteObject.exportObject(server, 0);

            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("GameService", stub);

            System.out.println("Serwer RMI uruchomiony na porcie 1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized String createRoom(String playerName) throws RemoteException {
        String roomId = String.valueOf(rooms.size() + 1);
        rooms.put(roomId, new GameRoom(roomId, playerName));
        System.out.println("Utworzono pokój: " + roomId + " przez gracza " + playerName);
        return roomId;
    }

    @Override
    public synchronized String joinRoom(String roomId, String playerName) throws RemoteException {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            return "Pokój nie istnieje.";
        }
        if (room.isFull()) {
            return "Pokój jest pełny.";
        }
        room.addPlayer(playerName);
        System.out.println(playerName + " dołączył do pokoju " + roomId);

        room.notifyObservers("Gracz " + playerName + " dołączył do pokoju " + roomId + "!");

        return "Dołączono do pokoju: " + roomId;
    }

    @Override
    public synchronized String makeMove(String roomId, String playerName, int position) throws RemoteException {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            return "Pokój nie istnieje.";
        }
        return room.makeMove(playerName, position);
    }

    @Override
    public synchronized String getGameState(String roomId) throws RemoteException {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            return "Pokój nie istnieje.";
        }
        return room.getGameState();
    }

    @Override
    public synchronized void registerObserver(String roomId, GameObserver observer) throws RemoteException {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            room.addObserver(observer);
        }
    }

    @Override
    public synchronized String getOpponentIP(String roomId, String playerName) throws RemoteException {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            return null;
        }
        return room.getOpponentIP(playerName);
    }

    @Override
    public synchronized int getOpponentPort(String roomId, String playerName) throws RemoteException {
        GameRoom room = rooms.get(roomId);
        if (room == null) {
            return -1;
        }
        return room.getOpponentPort(playerName);
    }

    @Override
    public synchronized void registerPlayerIPAndPort(String roomId, String playerName, String ipAddress, int port) throws RemoteException {
        GameRoom room = rooms.get(roomId);
        if (room != null) {
            room.setPlayerIPAndPort(playerName, ipAddress, port);
            System.out.println("Zarejestrowano IP i port dla gracza " + playerName
                    + ": " + ipAddress + ":" + port);
        }
    }
}
