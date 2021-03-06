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
import java.rmi.NotBoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gianetti
 */
public class ClientCommunicationThread implements Runnable {

    private Socket socket;
    private BufferedReader reader;
    private ClientJPanel clienteJ;

    ClientCommunicationThread(Socket socket, ClientJPanel clienteJ) {
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
                    if (newMsg != "") {
                        switch (newMsg.substring(0, 5)) {
                            case ("-cnt-"):
                                clienteJ.setNumJogadores(Integer.valueOf(newMsg.substring(5)));
                                System.out.println(newMsg);
                                newMsg = "";
                                clienteJ.updatePlayerList();
                                clienteJ.repaint();                               
                                break;
                            case ("-dct-"):
                                clienteJ.setNumJogadores(Integer.valueOf(newMsg.substring(5)));
                                newMsg = "";
                                clienteJ.repaint();
                                break;
                        }
                    }
                }
            }
        } catch (IOException | NotBoundException ex) {
            Logger.getLogger(ClientCommunicationThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
