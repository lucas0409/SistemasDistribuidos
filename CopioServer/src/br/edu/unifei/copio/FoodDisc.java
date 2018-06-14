/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 *
 * @author lucas
 */
public class FoodDisc extends UnicastRemoteObject implements FoodDiscInterface {
    private final int margin = 10;
    private Point position;
    private int mass;
    private int screenWidth;
    private int screenHeight;
    
    public void setScreen (int width, int height){
        screenHeight = height;
        screenWidth = width;
        
    }
    
    
    public int eatThis(Point p, double raio) throws RemoteException {
        if (position.distance(p) < raio){
            position.x = (new Random()).nextInt(screenWidth) - margin;
            position.y = (new Random()).nextInt(screenHeight) - margin;
        }else{
            return -1;
        }
            
        return mass;
    }
    
    @Override
    public Point getPosition() throws RemoteException {
        return position;
    }
    
    @Override
    public int getMass() throws RemoteException {
        return mass;
    }
    
    public FoodDisc() throws RemoteException{
        position = new Point();
        position.x = (new Random()).nextInt(screenWidth)-margin;
        
        position.y = (new Random()).nextInt(screenHeight)-margin;
        System.out.println("X Y " + position.x + position.y);
        mass = (new Random()).nextInt(5) + 2;
    }    
}
