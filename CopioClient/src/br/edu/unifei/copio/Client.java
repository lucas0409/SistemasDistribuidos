/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author lucas
 */
public class Client {
    public static final int MAXSIZE = 1024;
    public static final int PORT = 7000;
    
    public static void main(String[] args){
        JFrame frame = new JFrame("Cop.io");
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.add(new ClientJPanel(frame));
        frame.setSize( 300, 100 ); // set frame size
        frame.setVisible( true ); // display frame
        frame.setResizable(false);
    }    
}
