package br.edu.unifei.copio;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author lucas
 */
public interface RemoteClientInterface extends Remote {
    public void eatThis() throws RemoteException;
    public Point getPosition() throws RemoteException;
    public int getMass() throws RemoteException;
    public void setMass() throws RemoteException;
    public void setPosition() throws RemoteException;
}