/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author lucas
 */
public interface FoodDiscInterface extends Remote {
    public int eatThis(Point p, double raio) throws RemoteException;
    public Point getPosition() throws RemoteException;
    public int getMass() throws RemoteException;
}
