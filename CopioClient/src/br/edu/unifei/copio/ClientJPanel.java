/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import static br.edu.unifei.copio.Client.MAXSIZE;
import static br.edu.unifei.copio.Client.PORT;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author lucas
 */
public class ClientJPanel extends JPanel {

    JTextField txt_playerName = new JTextField(10);
    JButton btn_playGame = new JButton("Jogar!");
    JFrame frame;
    Socket socket;
    private InetAddress broadcastAddress;
    private float x;
    private float y;
    private int size;
    private int numJogadores;
    private FoodSphereInterface[] food;
    private boolean gameStarted;

    public void setNumJogadores(int numJogadores) {
        this.numJogadores = numJogadores;
    }

    private void removeComponents() {
        this.remove(txt_playerName);
        this.remove(btn_playGame);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        //frame.setSize(1000,800);
        frame.setLocation(0,0);
        frame.setBackground(Color.MAGENTA);
        this.setSize(1000, 800);
        

    }

    public ClientJPanel(JFrame frame) throws NotBoundException, MalformedURLException, RemoteException {
        this();
        gameStarted = false;
        this.frame = frame;
        food = new FoodSphereInterface[20];

        for (int i = 0; i < 20; i++) {

        food[i] = (FoodSphereInterface) Naming.lookup("rmi://192.168.0.9:1090/FoodSphere" + (i + 1));

        }
    }

    public ClientJPanel() {
        this.setBackground(Color.black);
        x = y = 0;
      
        size = 100;

        btn_playGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg;
                if (txt_playerName.getText() != null) {
                    msg = txt_playerName.getText();
                } else {
                    msg = "Convidado";
                }
                connect(msg);
                removeComponents();
            }
        });

        Timer t;
        t = new Timer(10, new ActionListener() {
            Point p  = new Point();
            @Override
            public void actionPerformed(ActionEvent e) {
                
                p = MouseInfo.getPointerInfo().getLocation();
                System.out.println("P = " + p);
                
                float dx = (p.x - x);
                float dy = (p.y - y);
                float d = (float) Math.sqrt((dx*dx)+(dy*dy));
                
                float Vx = (3/d)*dx;
                float Vy = (3/d)*dy;
                
                
                x += Vx;
                y += Vy;
              
                repaint();
            }
        });
        t.start();

        this.add(txt_playerName);
        this.add(btn_playGame);

    }

    public static InetAddress getBroadcastAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback()) {
                    continue;
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast != null) {
                        return broadcast;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void connect(String msg) {
        try {
            String requestTest = "-rqt-";
            DatagramSocket dgSocket = new DatagramSocket(PORT);
            broadcastAddress = getBroadcastAddress();

            DatagramPacket dgSendPacket = new DatagramPacket(requestTest.getBytes(), requestTest.getBytes().length, broadcastAddress, PORT);
            DatagramPacket dgReceivePacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE); //pacote UDP para receber a mensagem de broadcast do servidor
            dgSocket.setBroadcast(true);
            dgSocket.send(dgSendPacket);  //Cliente grita em broadcast por Datagrama com ip do servidor
            dgSocket.close();

            dgSocket = new DatagramSocket(PORT);
            dgSocket.setBroadcast(true);
            dgSocket.receive(dgReceivePacket); //método que coloca o pacote que está no socket criado na porta PORT em dgReceivePacket

            socket = new Socket(dgReceivePacket.getAddress().toString().replace("/", ""), PORT); //criação do socket para conexão com o servidor a partir da mensagem obtida
            Thread MsgReceive = new Thread(new ClientCommunicationThread(socket, this));
            MsgReceive.start();
            System.out.println("Conectado em: " + dgReceivePacket.getAddress().toString().replace("/", ""));
            dgSocket.close(); //fecha socket UDP*/

            PrintStream writer = new PrintStream(socket.getOutputStream());
            writer.println(msg);

        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Random r = new Random();
        
        for (int i = 0; i < numJogadores; i++) {
            
            g.setColor(Color.WHITE);
            g.fillOval((int) x-(size/2), (int) y-(size/2), size, size);
        
        }
        Point p = new Point();
        if (gameStarted) {
            for (FoodSphereInterface foodSphereInterface : food) {
                try {
                    p = foodSphereInterface.getPosition();
                    g.setColor(Color.white);
                    g.fillOval(p.x, p.y, foodSphereInterface.getMass(), foodSphereInterface.getMass());
                } catch (Exception e) {
                }
            }
        }
    }
    

}
