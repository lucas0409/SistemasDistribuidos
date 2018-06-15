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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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
    String playerName;
    int playerIndex;
    JButton btn_playGame = new JButton("Jogar!");
    JFrame frame;
    Socket socket;
    private Timer t;
    private RemoteClientInterface thisPlayer = null;
    private InetAddress broadcastAddress;
    private int size = 0;
    private Point playerPosition = new Point();
    private int numJogadores;
    private boolean gameStarted;
    private String serverIP;
    int velocidade = 4;
    private foodInfo[] remoteFoods;
    private ArrayList<playerInfo> remoteClients = new ArrayList<playerInfo>();

    public void setNumJogadores(int numJogadores) {
        this.numJogadores = numJogadores;
    }

    protected void updatePlayerList() throws NotBoundException, RemoteException, MalformedURLException {
        RemoteClientInterface c;
        remoteClients.clear();
        if(thisPlayer == null){
            thisPlayer = (RemoteClientInterface) Naming.lookup("rmi://" + serverIP + ":1091/" +playerName);
            thisPlayer.setMass(size);
        }
        String[] boundNames = Naming.list("rmi://" + serverIP + ":1091");
        int i = 0;
        for (String boundName : boundNames) {
            c = (RemoteClientInterface) Naming.lookup("rmi:" + boundName);
            playerInfo p = new playerInfo();
            p.mass = c.getMass();
            p.position = c.getPosition();
            p.player = c;
            p.color = c.getColor();
            p.name = boundName.substring(19);
            remoteClients.add(p);
            i++;
        }
    }

    private void removeComponents() {
        this.remove(txt_playerName);
        this.remove(btn_playGame);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        //frame.setSize(1000,800);
        frame.setLocation(0, 0);
        frame.setBackground(Color.MAGENTA);
        this.setSize(1000, 800);
        gameStarted = true;
        t.start();
    }

    public ClientJPanel(JFrame frame) throws NotBoundException, MalformedURLException, RemoteException {
        this();
        gameStarted = false;
        this.frame = frame;
        remoteFoods = new foodInfo[20];
    }

    public ClientJPanel() {
        this.setBackground(Color.black);
        size = 50;
        playerPosition.x = playerPosition.y = 0;


        btn_playGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txt_playerName.getText() != null) {
                    playerName = txt_playerName.getText();
                } else {
                    playerName = "Convidado";
                }
                connect(playerName);
                foodInfo f;
                for (int i = 0; i < 20; i++) {
                    try {
                        f = new foodInfo();
                        FoodDiscInterface rf = (FoodDiscInterface) Naming.lookup("rmi://" + serverIP + ":1090" + "/FoodSphere" + (i + 1));
                        f.food = rf;
                        f.mass = rf.getMass();
                        f.position = rf.getPosition();
                        remoteFoods[i] = f;
                    } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                        Logger.getLogger(ClientJPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                removeComponents();
            }
        });
        
        t = new Timer(20, new ActionListener() {
            Point p = new Point();
            int massa = 0;
            @Override
            public void actionPerformed(ActionEvent e) {

                p = MouseInfo.getPointerInfo().getLocation();

                float dx = (p.x - playerPosition.x);
                float dy = (p.y - playerPosition.y);
                float d = (float) Math.sqrt((dx * dx) + (dy * dy));

                float Vx = (3 / d) * dx;
                float Vy = (3 / d) * dy;

                playerPosition.x += Vx;
                playerPosition.y += Vy;

                
                Point posCliente = new Point((int)playerPosition.x,(int)playerPosition.y);
                
                for (int i = 0; i < 20; i++) {
                    try {
                        if(remoteFoods[i].position.distance(playerPosition.x, playerPosition.y) < size/2){          
                            massa = remoteFoods[i].food.eatThis(posCliente, size/2);
                            remoteFoods[i].position = remoteFoods[i].food.getPosition();
                            size += massa;
                            if(size >= 100 && size < 200 && velocidade == 4){
                                velocidade --;
                            }else if (size >= 200 && size < 300  && velocidade == 3) {
                                velocidade --;
                            }else if(size >= 300  && velocidade == 2){
                                velocidade --;
                            }
                            break;
                        }
                    }catch (RemoteException ex) {
                        Logger.getLogger(ClientJPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(thisPlayer != null){
                    try {
                        thisPlayer.setPosition(playerPosition.x, playerPosition.y);
                    } catch (RemoteException ex) {
                        Logger.getLogger(ClientJPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                repaint();
            }
                    
        });

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

            serverIP = dgReceivePacket.getAddress().toString().replace("/", "");
            socket = new Socket(serverIP, PORT); //criação do socket para conexão com o servidor a partir da mensagem obtida
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

        /*for (int i = 0; i < numJogadores; i++) {
            g.setColor(Color.WHITE);
            g.fillOval((int) (playerPosition.x - (size / 2.0)), (int) (playerPosition.y - (size / 2.0)), size, size);
        }*/
        if (gameStarted) {
            for (int i = 0; i < remoteFoods.length; i++) {
                try {
                    g.setColor(Color.RED);
                    g.fillOval(remoteFoods[i].position.x, remoteFoods[i].position.y, remoteFoods[i].mass*4, remoteFoods[i].mass*4);

                    g.setColor(Color.white);
                    g.fillOval(remoteFoods[i].position.x, remoteFoods[i].position.y, remoteFoods[i].mass*4, remoteFoods[i].mass*4);

                } catch (Exception e) {
                }
            }
            for (playerInfo remoteClient : remoteClients) {
                Point p = null;
                int mass = 0;
                Color c = null;
                try {
                    c = remoteClient.color;
                    p = remoteClient.player.getPosition();
                    mass = remoteClient.player.getMass();
                } catch (RemoteException ex) {
                    Logger.getLogger(ClientJPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                g.setColor(c);
                g.fillOval((int) (p.x - (mass / 2.0)), (int) (p.y - (mass / 2.0)), mass, mass);
            }
        }
    }

}
                

class playerInfo {

    public RemoteClientInterface player;
    public Point position;
    public int mass;
    public String name;
    public Color color;
}

class foodInfo {

    public FoodDiscInterface food;
    public Point position;
    public int mass;
}
