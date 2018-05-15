/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author lucas
 */
public class Server {
    private static int conexoes; //número de conexões ao servidor
    public static final int PORT = 7000; //Define a porta como 7000
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT); //cria o servidor na porta PORT definida anteriormente
        Thread sendIP = new Thread(new ServerIPBroadcastProtocol()); //cria Thread que envia IP do servidor por broadcast
        sendIP.start(); //inicia Thread de descoberta de servidor
        
        for(;;){
            Socket cliente = server.accept(); //espera um cliente conectar
            conexoes++; //incrementa o número de clientes que conectaram ao servidor
            System.out.println("Conexões: " + String.valueOf(conexoes)); //mostra o número de clientes que já conectaram
            Thread conexao = new Thread(new ConnectionProtocol(cliente)); //joga cliente para uma Thread separada para tratar a conexão
            conexao.start(); //inicia Thread
        }
    }
}
