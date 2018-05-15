/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Gianetti
 */
public class ClientCommunicationThread implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private JPanel clienteJ;

    ClientCommunicationThread(Socket socket, JPanel clienteJ) {
        this.socket = socket;
        this.clienteJ = clienteJ;
    }

    @Override
    public void run() {
        try {
            while (true) {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Caso receba uma mensagem do server, imprime na tela
                String newMsg;
                while ((newMsg = reader.readLine()) != null) {
                    if (newMsg.length() != 0) {
                        System.out.println(newMsg);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ClientCommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
