package br.edu.unifei.copio;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FoodDiscInterface extends Remote {
    public int eatThis(Point p, double raio) throws RemoteException;
    public Point getPosition() throws RemoteException;
    public int getMass() throws RemoteException;
}
