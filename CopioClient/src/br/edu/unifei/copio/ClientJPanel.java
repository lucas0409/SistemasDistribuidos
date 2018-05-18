/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unifei.copio;

import static br.edu.unifei.copio.Client.MAXSIZE;
import static br.edu.unifei.copio.Client.PORT;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import java.util.Scanner;
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
    private int x;
    private int y;
    private int size;
    private int numJogadores;

    public void setNumJogadores(int numJogadores) {
        this.numJogadores = numJogadores;
    }

    private void removeComponents() {
        this.remove(txt_playerName);
        this.remove(btn_playGame);
        frame.setSize(800, 600);
        this.setSize(800, 600);
    }

    public ClientJPanel(JFrame frame) {
        this();
        this.frame = frame;
    }

    public ClientJPanel() {
        this.setBackground(Color.red);
        x = y = 0;
        size = 50;
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
        Timer t = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                x++;
                y++;

                //repaint();
            }
        });
        t.start();

        this.add(txt_playerName);
        this.add(btn_playGame);

    }

    private void connect(String msg) {
        try {
            String requestTest = "-rqt-";
            DatagramSocket dgSocket = new DatagramSocket(PORT);

            DatagramPacket dgSendPacket = new DatagramPacket(requestTest.getBytes(), requestTest.getBytes().length, InetAddress.getByName("255.255.255.255"), PORT);
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Random r = new Random();
        for (int i = 0; i < numJogadores; i++) {
            g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.fillOval(r.nextInt(this.getWidth() - size), r.nextInt(this.getHeight() - size), size, size);
        }

    }

}
