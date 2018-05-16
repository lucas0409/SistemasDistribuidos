/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
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
        int recvMsgSize;  // Size of receive message
        byte[] byteBuffer = new byte[1024];  // Receive buffer

        System.out.println("Handling client at "
                + s.getInetAddress().getHostAddress() + " on port "
                + s.getPort());

        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            PrintStream saida = new PrintStream(this.s.getOutputStream());
            Servidor.nomeCliente = entrada.readLine();
            
            if (Servidor.Lista_Users(s, Servidor.nomeCliente)){
                saida.println("Não foi possível conectar! Nome ja existente");
                this.s.close();
                return;
            } else {
                System.out.println(Servidor.nomeCliente + " conectou-se!");
                System.out.println(Servidor.listaClientes.get(0).toString());
                System.out.println(Servidor.listaClientes.get(1).toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectionProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
