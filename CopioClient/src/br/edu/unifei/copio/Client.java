/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JFrame;

/**
 *
 * @author lucas
 */
public class Client {
    public static final int MAXSIZE = 1024;
    public static final int PORT = 7000;
    
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException{
        JFrame frame = new JFrame("Cop.io");
        frame.setLayout(null);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.add(new ClientJPanel(frame));
        frame.setBounds(500, 500, 400, 500);
        frame.setVisible( true ); // display frame
        frame.setResizable(false);
    }    
}
