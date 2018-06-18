/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 *
 * @author Gianetti
 */
public class RemoteClient extends UnicastRemoteObject implements RemoteClientInterface, Serializable {
    private Point position;
    private int mass;
    private Color color;
    private final int margin = 10;
    public RemoteClient() throws RemoteException {
        Random rand = new Random();
        position = new Point();
        position.x = rand.nextInt(800 - margin) + margin;
        position.y = rand.nextInt(600 - margin) + margin;
        color = new Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
        mass = 30;
    }
    @Override
    public int getMass() throws RemoteException { return mass; }
    @Override
    public void setMass(int newMass) throws RemoteException { mass = newMass; }
    @Override
    public Point getPosition() throws RemoteException { return position; }
    @Override
    public void setPosition(int x, int y) throws RemoteException {
        position.x = x;
        position.y = y;
    }
    @Override
    public Color getColor() throws RemoteException { return color; }
}
