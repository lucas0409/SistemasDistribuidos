/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JOptionPane;

/**
 *
 * @author lucas
 */
public class FoodSphere extends UnicastRemoteObject implements FoodSphereInterface {

    @Override
    public void eatThis() throws RemoteException {
        System.out.println("COMEU MALUCO!!");
    }
    
    public FoodSphere() throws RemoteException{
        
    }
    
}
