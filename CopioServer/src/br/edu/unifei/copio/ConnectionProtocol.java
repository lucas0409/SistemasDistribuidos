/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.rmi.registry.Registry;
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
        System.out.println("Handling client at "+ s.getInetAddress().getHostAddress()+" on port "+s.getPort());
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
            PrintStream saida = new PrintStream(this.s.getOutputStream());
            Servidor.nomeCliente = entrada.readLine();
            Registry clientRegistry = java.rmi.registry.LocateRegistry.getRegistry(1091);
            clientRegistry.rebind(Servidor.nomeCliente, new RemoteClient());
            if (!Servidor.addClient(s, Servidor.nomeCliente)) {
                saida.println("Não foi possível conectar! Nome ja existente");
                Servidor.conexoes--;
                this.s.close();
                return;
            } else {
                System.out.println(Servidor.nomeCliente + " conectou-se!");
                for (int i = 0; i < Servidor.listaClientes.size(); i = i + 2) {
                    Socket sock = (Socket) Servidor.listaClientes.get(i);
                    PrintStream socket_out = new PrintStream(sock.getOutputStream());
                    socket_out.println("-cnt-" + Servidor.conexoes);
                }
            }
        } catch (IOException ex) {
            if (ex instanceof EOFException) {
                Logger.getLogger(ConnectionProtocol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
