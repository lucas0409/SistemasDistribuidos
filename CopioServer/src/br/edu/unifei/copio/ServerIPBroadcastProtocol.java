/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class ServerIPBroadcastProtocol implements Runnable{
    public static final int MAXSIZE = 1024;
    public static final int PORT = 7000;
    
    public ServerIPBroadcastProtocol(){ }
    @Override
    public void run() {
        try{
            DatagramSocket dgSocket = new DatagramSocket();
            DatagramPacket dgPacket = new DatagramPacket(InetAddress.getLocalHost().getAddress(), InetAddress.getLocalHost().getAddress().length, InetAddress.getByName("255.255.255.255"), PORT);
            dgSocket.setBroadcast(true);
            for(;;){
                dgSocket.send(dgPacket);
            }
        } catch (SocketException ex) {
            Logger.getLogger(ServerIPBroadcastProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerIPBroadcastProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
