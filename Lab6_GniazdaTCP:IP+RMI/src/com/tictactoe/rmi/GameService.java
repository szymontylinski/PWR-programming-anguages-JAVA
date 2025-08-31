package com.tictactoe.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameService extends Remote {
    String createRoom(String playerName) throws RemoteException;
    String joinRoom(String roomId, String playerName) throws RemoteException;
    String makeMove(String roomId, String playerName, int position) throws RemoteException;
    String getGameState(String roomId) throws RemoteException;

    void registerObserver(String roomId, GameObserver observer) throws RemoteException;

    // Metody do obsługi IP/Port
    String getOpponentIP(String roomId, String playerName) throws RemoteException;
    int getOpponentPort(String roomId, String playerName) throws RemoteException;
    void registerPlayerIPAndPort(String roomId, String playerName, String ipAddress, int port) throws RemoteException;
}
