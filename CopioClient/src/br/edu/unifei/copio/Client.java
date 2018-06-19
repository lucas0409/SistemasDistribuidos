//André Felipe Rocha        - 30897
//Carla Grande de Freitas   - 30506
//Lucas Alves de Oliveira   - 30303
//Mateus Gianetti de Jesus  - 30870

package br.edu.unifei.copio;


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author lucas
 */
public class Client {
    public static final int MAXSIZE = 1024;
    public static final int PORT = 7000;
    
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException{
        JFrame frame = new JFrame("Cop.io");
        JPanel panel = new ClientJPanel(frame);
        panel.setBounds(0, 200, 300, 170);
        frame.setLayout(null);
        frame.getContentPane().add(panel);
        ImageIcon icon = new ImageIcon("src\\br\\edu\\unifei\\copio\\Logo.png");
        JLabel label = new JLabel(icon);
        label.setBounds(0, 0, 300, 200);
        frame.getContentPane().add(label);
        frame.setBounds(150,150, 300, 400 ); // set frame size
        frame.setResizable(false);
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setVisible( true ); // display frame
    }    
}
