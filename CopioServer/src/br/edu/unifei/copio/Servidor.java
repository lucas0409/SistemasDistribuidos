package br.edu.unifei.copio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    protected static String nomeCliente;
    protected static List listaClientes = new ArrayList();
    protected static int conexoes; //número de clientes conectados ao servidor
    private static final int PORT_SERVER = 7000; //Define a porta do servidor como 7000
    
    //<RESUMO> Tenta adicionar um cliente na lista de clientes
    //<PARÂMETROS> Socket user -> socket TCP da conexão do servidor com o cliente
    //             String nome -> nome do cliente
    //<RETORNO> true se conseguiu adicionar o cliente na lista
    //          false se não conseguiu adicionar o cliente na lista
    public static boolean addClient(Socket user, String nome) {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).equals(nome)) {
                return false;
            }
        }
        listaClientes.add(user);
        listaClientes.add(nome);
        System.out.println("Adicionou " + user + " " + nome);
        return true;
    }

    public static void main(String[] args) throws IOException, RemoteException, NotBoundException {
        ServerSocket server = new ServerSocket(PORT_SERVER); //cria o servidor na porta PORT definida anteriormente
        Thread sendIP = new Thread(new ServerIPBroadcastProtocol()); //cria Thread que envia IP do servidor por broadcast
        sendIP.start(); //inicia Thread de descoberta de servidor

        Registry foodRegistry = java.rmi.registry.LocateRegistry.createRegistry(1090); //cria um rmiregistry na porta 1090 para os discos de comida
        Registry clientRegistry = java.rmi.registry.LocateRegistry.createRegistry(1091); //cria um rmiregistry na porta 1091
        for (int i = 0; i < 10; i++) {
            foodRegistry.rebind("FoodDisc" + i, new FoodDisc());
        }

        for (;;) {
            Socket cliente = server.accept(); //espera um cliente conectar
            conexoes++; //incrementa o número de clientes que conectaram ao servidor
            System.out.println("Conexões: " + String.valueOf(conexoes)); //mostra o número de clientes que já conectaram
            Thread conexao = new Thread(new ConnectionProtocol(cliente)); //joga cliente para uma Thread separada para tratar a conexão
            conexao.start(); //inicia Thread
        }
    }

}
