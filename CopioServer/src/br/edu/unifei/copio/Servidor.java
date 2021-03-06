
//André Felipe Rocha        - 30897
//Carla Grande de Freitas   - 30506
//Lucas Alves de Oliveira   - 30303
//Mateus Gianetti de Jesus  - 30870

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

    public static boolean Lista_Users(Socket user, String nome) {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).equals(nome)) {
                return true;
            }
        }
        listaClientes.add(user);
        listaClientes.add(nome);
        System.out.println("Adicionou " + user + " " + nome);
        return false;
    }

    public void Remove_Users(String oldUser) {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).equals(oldUser)) {
                listaClientes.remove(oldUser);
            }
        }
    }

    public static void main(String[] args) throws IOException, RemoteException, NotBoundException {
        ServerSocket server = new ServerSocket(PORT); //cria o servidor na porta PORT definida anteriormente
        Thread sendIP = new Thread(new ServerIPBroadcastProtocol()); //cria Thread que envia IP do servidor por broadcast
        sendIP.start(); //inicia Thread de descoberta de servidor

        Registry foodRegistry = java.rmi.registry.LocateRegistry.createRegistry(1090);
        Registry clientRegistry = java.rmi.registry.LocateRegistry.createRegistry(1091);
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
