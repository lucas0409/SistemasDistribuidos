/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lucas
 */
public class ConnectionProtocol implements Runnable {

    private Socket s;

    public ConnectionProtocol(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        System.out.println("olaola");
        int recvMsgSize;  // Size of receive message
        byte[] byteBuffer = new byte[1024];  // Receive buffer

        System.out.println("Handling client at "
                + s.getInetAddress().getHostAddress() + " on port "
                + s.getPort());

        InputStream in;
        try {
            in = s.getInputStream();
            OutputStream out = s.getOutputStream();
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                System.out.write(byteBuffer, 0, recvMsgSize);
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
