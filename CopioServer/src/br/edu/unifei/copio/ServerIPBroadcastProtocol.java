package br.edu.unifei.copio;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerIPBroadcastProtocol implements Runnable {
    public static final int MAXSIZE = 1024; //tamanho máximo de uma mensagem em um packet
    public static final int PORT_RECEIVE = 7002; //port do socket UDP do servidor
    public static final int PORT_SEND = 7001; //port do socket UDP do cliente
    private InetAddress broadcastAddress; //endereço de IP utilizado para enviar packets por broadcast
    
    public ServerIPBroadcastProtocol() {
    }

    @Override
    public void run() {
        try {
            DatagramSocket dgSocket = new DatagramSocket(PORT_RECEIVE);
            dgSocket.setBroadcast(true);
            broadcastAddress = getBroadcastAddress();
            DatagramPacket dgReceivePacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE);
            DatagramPacket dgSendPacket = new DatagramPacket(InetAddress.getLocalHost().getAddress(), 
                    InetAddress.getLocalHost().getAddress().length, broadcastAddress, PORT_SEND);
            for (;;) {
                dgSocket.receive(dgReceivePacket);            
                if((new String(dgReceivePacket.getData())).contains("-rqt-")){
                    dgSocket.close();
                    dgSocket = new DatagramSocket(PORT_RECEIVE);
                    dgSocket.setBroadcast(true);
                    dgSocket.send(dgSendPacket);
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(ServerIPBroadcastProtocol.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerIPBroadcastProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //<RESUMO> Retorna um IP de broadcast a partir da primeira interface encontrada
    public static InetAddress getBroadcastAddress() {
	try {
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();
			if (networkInterface.isLoopback()) continue;
			for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
				InetAddress broadcast = interfaceAddress.getBroadcast();
				if (broadcast != null) return broadcast;
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
    }

}
