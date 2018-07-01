package br.edu.unifei.copio;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class RemoteClient extends UnicastRemoteObject implements RemoteClientInterface, Serializable {
    private Point position; //posição do cliente
    private int mass; //massa do cliente
    private Color color; //cor do cliente
    private final int margin = 10; //margem utilizada não instanciar um cliente fora da tela
    
    //Inicialização dos atributos do cliente
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
