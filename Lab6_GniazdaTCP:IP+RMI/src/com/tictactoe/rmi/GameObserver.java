package com.tictactoe.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameObserver extends Remote {
    void update(String gameState) throws RemoteException;
}
