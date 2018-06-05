/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lucas
 */
public class Servidor {
    protected static String nomeCliente;
    protected static List listaClientes = new ArrayList();
    protected static int conexoes; //número de conexões ao servidor
    public static final int PORT = 7000; //Define a porta como 7000
    
    public static boolean Lista_Users(Socket user, String nome){
       for (int i=0; i < listaClientes.size(); i++){
         if(listaClientes.get(i).equals(nome))
           return true;
       }
       listaClientes.add(user);
       listaClientes.add(nome);
       System.out.println("Adicionou " + user +" "+ nome);
       return false;
    }

    public void Remove_Users(String oldUser) {
       for (int i=0; i< listaClientes.size(); i++){
         if(listaClientes.get(i).equals(oldUser))
           listaClientes.remove(oldUser);
       }
    }
    
    public static void main(String[] args) throws IOException, RemoteException, NotBoundException{
        ServerSocket server = new ServerSocket(PORT); //cria o servidor na porta PORT definida anteriormente
        Thread sendIP = new Thread(new ServerIPBroadcastProtocol(server)); //cria Thread que envia IP do servidor por broadcast
        sendIP.start(); //inicia Thread de descoberta de servidor
        
        Registry gameRegistry = java.rmi.registry.LocateRegistry.createRegistry(1090);
        
        for (int i = 1; i <= 20; i++) {
            gameRegistry.rebind("FoodSphere" + i, new FoodSphere());
        }
        
        for(;;){
            Socket cliente = server.accept(); //espera um cliente conectar
            conexoes++; //incrementa o número de clientes que conectaram ao servidor
            
            System.out.println("Conexões: " + String.valueOf(conexoes)); //mostra o número de clientes que já conectaram
            Thread conexao = new Thread(new ConnectionProtocol(cliente)); //joga cliente para uma Thread separada para tratar a conexão
            conexao.start(); //inicia Thread
        }
    }
    
}
