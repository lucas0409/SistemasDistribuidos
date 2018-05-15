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
import java.net.Socket;
import java.net.SocketException;
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
public class ClientJPanel extends JPanel{
    JTextField txt_playerName = new JTextField(10);
    JButton btn_playGame = new JButton("Jogar!");
    JFrame frame;
    Socket socket;
    private int x;
    private int y;
    private int size;
    
    private void removeComponents(){
        this.remove(txt_playerName);
        this.remove(btn_playGame);
        frame.setSize(800,600);
        this.setSize(800, 600);
    }
    
    public ClientJPanel(JFrame frame){
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
                if(txt_playerName.getText() != null)
                {
                    msg = txt_playerName.getText();
                }else{
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
                
                repaint();
            }
        });
        t.start();
        
        this.add(txt_playerName);
        this.add(btn_playGame);
        
    }
    
    private void connect(String msg){
        try{
            DatagramSocket dgSocket = new DatagramSocket(PORT); //Socket UDP para ler pacotes UDP na porta PORT
            DatagramPacket dgPacket = new DatagramPacket(new byte[MAXSIZE], MAXSIZE); //pacote UDP para receber a mensagem de broadcast do servidor
            dgSocket.receive(dgPacket); //método que coloca o pacote que está no socket criado na porta PORT em dgPacket
            socket = new Socket(dgPacket.getAddress().toString().replace("/", ""), PORT); //criação do socket para conexão com o servidor a partir da mensagem obtida
            System.out.println("Conectado em: " + dgPacket.getAddress().toString().replace("/", ""));
            dgSocket.close(); //fecha socket UDP
            
            PrintStream writer = new PrintStream(socket.getOutputStream());
            writer.println(msg);
            
        } catch (SocketException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void paintComponent ( Graphics g ){
        super.paintComponent(g);
        g.fillOval(x, y, size, size);
    }
    
}
