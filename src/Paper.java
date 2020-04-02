import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Iterator;


class Paper extends JPanel {
    byte buf[] = null;
    InetAddress ip;
    DatagramSocket socket;
    int sendAdress;
    int myAdress;
    Paper thisPaper = this;
    String host;
    Receiver receiver = new Receiver();


    private HashSet hs = new HashSet();

    class Receiver extends Thread {
        @Override
        public void run() {

            try {
                DatagramSocket var1 = new DatagramSocket(myAdress);
                System.out.println(myAdress);

                while (true) {
                    System.out.println("sending");
                    byte[] var2 = new byte[8192];
                    DatagramPacket var3 = new DatagramPacket(var2, var2.length);
                    var1.receive(var3);
                    String var4 = new String(var3.getData(), 0, var3.getLength());
                    thisPaper.addPointString(var4);
                }
            } catch (Exception var5) {
                System.out.println("Här är felet");
                System.out.println(var5);
            }


        }
    }

    public Paper(int myAdress, String host, int sendAdress) {


        setBackground(Color.white);
        addMouseListener(new L1());
        addMouseMotionListener(new L2());
        this.myAdress = myAdress;
        this.sendAdress = sendAdress;
        this.host = host;
        System.out.println(sendAdress);
        try {
            ip = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        receiver.start();





    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.black);
        Iterator i = hs.iterator();

        System.out.println("pc");

        while (i.hasNext()) {
            Point p = (Point) i.next();
            g.fillOval(p.x, p.y, 2, 2);
            System.out.println("Paintitrewtrewng " + p);


        }


    }

    public void send(String msg) {
        try {
            byte[] var2 = msg.getBytes();
            InetAddress var3 = InetAddress.getByName(host);
            DatagramPacket var4 = new DatagramPacket(var2, var2.length, var3, this.sendAdress);
            DatagramSocket var5 = new DatagramSocket();
            var5.send(var4);
        } catch (Exception var6) {
            System.out.println(var6);
        }

    }

    private void addPoint(Point p) {
        hs.add(p);
        repaint();
        String message = Integer.toString(p.x) + " " + Integer.toString(p.y);
        send(message);
    }

    public void addPointString(String message) {
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
