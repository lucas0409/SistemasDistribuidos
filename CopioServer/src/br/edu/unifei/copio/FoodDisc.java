/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas
 */
public class FoodDisc extends UnicastRemoteObject implements FoodDiscInterface {
    private final int margin = 10;
    private Point position;
    private int mass;
    private final int screenWidth = 800;
    private final int screenHeight = 800;
            
    @Override
    public void eatThis() throws RemoteException {
        System.out.println("COMEU MALUCO!!");
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
        position.x = (new Random()).nextInt(screenWidth - margin) + margin;
        position.y = (new Random()).nextInt(screenHeight - margin) + margin;
        mass = (new Random()).nextInt(50) + 10;
    }    
}
