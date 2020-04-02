import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.*;
import java.util.HashSet;
import java.util.Iterator;


class Paper extends JPanel {
    private int sendAdress;
    private int myAdress;
    private Paper thisPaper = this;
    private String host;
    private Receiver receiver = new Receiver();


    private HashSet<Point> hs = new HashSet<>();

    class Receiver extends Thread {
        @Override
        public void run() {

            try {
                DatagramSocket datagramSocket = new DatagramSocket(myAdress);
                System.out.println(myAdress);

                while (true) {
                    System.out.println("sending");
                    byte[] bytes = new byte[8192];
                    DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                    datagramSocket.receive(datagramPacket);
                    String message = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    thisPaper.addPointString(message);
                }
            } catch (Exception e) {
                System.out.println(e);
            }


        }
    }

    Paper(int myAdress, String host, int sendAdress) {
        setBackground(Color.white);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
        this.myAdress = myAdress;
        this.sendAdress = sendAdress;
        this.host = host;
        System.out.println(sendAdress);
        receiver.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);


        for (Point p : hs) {
            g.fillOval(p.x, p.y, 2, 2);
        }
    }

    private void send(String msg) {
        try {
            byte[] bytes = msg.getBytes();
            InetAddress adress = InetAddress.getByName(host);
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length, adress, this.sendAdress);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void addPoint(Point p) {
        hs.add(p);
        repaint();
        String message = p.x + " " + p.y;
        send(message);
    }

    private void addPointString(String message) {
        String[] xy = message.split(" ");
        Point p = new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
        hs.add(p);
        repaint();
        System.out.println("Adding point");
    }

    class L1 extends MouseAdapter {
        public void mousePressed(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }

    class L2 extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent me) {
            addPoint(me.getPoint());
        }
    }
}
