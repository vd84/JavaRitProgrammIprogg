import java.awt.*;
import javax.swing.*;

public class Draw extends JFrame {


    private Paper p;


    public static void main(String[] args) {
        int sendSocket = Integer.parseInt(args[2]);
        int mySocket = Integer.parseInt(args[0]);
        String host = args[1];
        System.out.println(sendSocket);


        new Draw(mySocket, host, sendSocket);
    }

    public Draw(int mySocket, String host, int sendSocket) {
        this.p = new Paper(mySocket, host, sendSocket);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(p, BorderLayout.CENTER);
        setSize(640, 480);
        setVisible(true);
    }
}

