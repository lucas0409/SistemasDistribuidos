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
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class ServerIPBroadcastProtocol implements Runnable {

    public static final int MAXSIZE = 1024;
    public static final int PORT = 7000;
    private final ServerSocket server;

    public ServerIPBroadcastProtocol(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            DatagramSocket dgSocket = new DatagramSocket(PORT); //Socket UDP para ler pacotes UDP na porta PORT
            dgSocket.setBroadcast(true);
            DatagramPacket dgReceivePacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE);
            DatagramPacket dgSendPacket = new DatagramPacket(InetAddress.getLocalHost().getAddress(), InetAddress.getLocalHost().getAddress().length, InetAddress.getByName("255.255.255.255"), PORT);
            for (;;) {
                dgReceivePacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE);
                dgSocket.receive(dgReceivePacket); //método que coloca o pacote que está no socket criado na porta PORT em dgPacket            
                if((new String(dgReceivePacket.getData())).contains("-rqt-")){
                    System.out.println("xD");
                    dgSocket.close();
                    dgSocket = new DatagramSocket(PORT);
                    dgSocket.setBroadcast(true);
                    dgSocket.send(dgSendPacket);
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(ServerIPBroadcastProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerIPBroadcastProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
