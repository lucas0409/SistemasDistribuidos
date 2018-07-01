package br.edu.unifei.copio;

import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class FoodDisc extends UnicastRemoteObject implements FoodDiscInterface {
    private final int margin = 10;
    private Point position; //posição do disco
    private int mass; //massa do disco
    private int screenWidth = 800; //largura da janela do jogo
    private int screenHeight = 600; //altura da janela do jogo
    
    //método para o cliente tentar comer o disco
    public synchronized int eatThis(Point p, double raio) throws RemoteException {
        if (position.distance(p) < raio){
            position.x = (new Random()).nextInt(screenWidth - mass) + mass;
            position.y = (new Random()).nextInt(screenHeight - mass) + mass;
        }else return -1;
        return mass;
    }
    @Override
    public Point getPosition() throws RemoteException { return position; }
    
    @Override
    public int getMass() throws RemoteException { return mass; }
    
    //inicialização do disco
    public FoodDisc() throws RemoteException{
        position = new Point();
        position.x = (new Random()).nextInt(screenWidth - margin) + margin;   
        position.y = (new Random()).nextInt(screenHeight - margin) + margin;
        mass = (new Random()).nextInt(5) + 2;
    }
}
