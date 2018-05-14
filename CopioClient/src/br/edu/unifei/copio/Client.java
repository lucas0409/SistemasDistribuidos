/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

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

/**
 *
 * @author lucas
 */
public class Client {
    public static final int MAXSIZE = 1024;
    public static final int PORT = 7000;
    
    private static void connect(){
        try{
            Scanner scan = new Scanner(System.in); //Scanner para ler entradas do teclado
            DatagramSocket dgSocket = new DatagramSocket(PORT); //Socket UDP para ler pacotes UDP na porta PORT
            DatagramPacket dgPacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE); //pacote UDP para receber a mensagem de broadcast do servidor
            dgSocket.receive(dgPacket); //método que coloca o pacote que está no socket criado na porta PORT em dgPacket
            Socket socket = new Socket(dgPacket.getAddress().toString().replace("/", ""), PORT); //criação do socket para conexão com o servidor a partir da mensagem obtida
            System.out.println("Conectado em: " + dgPacket.getAddress().toString().replace("/", ""));
            dgSocket.close(); //fecha socket UDP
            
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            
            for(;;){
                byte[] byteBuffer = scan.nextLine().getBytes();
                out.write(byteBuffer); // Send the encoded string to the server
            }
            
        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public static void main(String[] args){
        connect();
    }    
}
