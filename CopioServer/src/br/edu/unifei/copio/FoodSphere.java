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
public class FoodSphere extends UnicastRemoteObject implements FoodSphereInterface {
    private final int margin = 10;
    private Point position;
    private int mass;
            
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
    
    public FoodSphere() throws RemoteException{
        position = new Point();
        position.x = (new Random()).nextInt(800 - margin) + margin;
        position.y = (new Random()).nextInt(600 - margin) + margin;
        mass = (new Random()).nextInt(15) + 5;
    }    
}
