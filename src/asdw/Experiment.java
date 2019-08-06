package asdw;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
class CommunicationServer extends JFrame implements Runnable {
    JPanel contentPane;
    JLabel jLabel1 = new JLabel();     //创建图形用户界面
    JLabel jLabel2 = new JLabel();
    TextField jTextField1 = new TextField();
    TextArea jTextArea1 = new TextArea("你好! 我是服务器。   ", 100, 50);
    Thread s;
    private DatagramSocket sendSocket,  receiveSocket;
    private DatagramPacket sendPacket,  receivePacket;
    private String name;
    public CommunicationServer() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
        jLabel1.setText("通信记录:");
        jLabel1.setBounds(new Rectangle(4, 4, 67, 29));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(null);
        this.setSize(new Dimension(400, 200));
        this.setTitle("UDPServer");
        jLabel2.setText("输入通信内容:");
        jLabel2.setBounds(new Rectangle(11, 124, 93, 32));
        jTextField1.setText("");
        jTextField1.setBounds(new Rectangle(118, 125, 244, 30));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTextField1_actionPerformed(e);
            }
        });
        jTextArea1.setBounds(new Rectangle(0, 35, 380, 90));
        jTextArea1.setEditable(false);
        jTextField1.setEditable(true);
        contentPane.add(jLabel1, null);
        contentPane.add(jTextArea1, null);
        contentPane.add(jTextField1, null);
        contentPane.add(jLabel2, null);
        try {
            sendSocket = new DatagramSocket();   //创建接收用数据报
            receiveSocket = new DatagramSocket(8000);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        s = new Thread(this);  //创建线程
        s.start();
    }

    public void run() {
        while (true) {
            try {
                byte buf[] = new byte[100];
                receivePacket = new DatagramPacket(buf, buf.length);
                receiveSocket.receive(receivePacket);
                name = receivePacket.getAddress().toString().trim();
                jTextArea1.append("\n来自主机:" + name + "\n端口:" + receivePacket.getPort());
                jTextArea1.append("\n客户端: ");
                byte[] data = receivePacket.getData();
                String receivedString = new String(data, 0);
                jTextArea1.append(receivedString);
            } catch (IOException e) {
                jTextArea1.append("网络通信出现错误,问题在于" + e.toString());
            }
        }
    }
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }
    void jTextField1_actionPerformed(ActionEvent e) {
        try {
            jTextArea1.append("\n服务器: ");
            String string = jTextField1.getText().trim();
            jTextArea1.append(string);
            byte[] databyte = new byte[100];
            string.getBytes(0, string.length(), databyte, 0);
            DatagramPacket sendPacket = new DatagramPacket(databyte, string.length(), InetAddress.getByName(name.substring(1)), 8001);
            sendSocket.send(sendPacket);
        } catch (IOException ioe) {
            jTextArea1.append("网络通信出现错误，问题在于" + e.toString());
        }
    }
}
public class Experiment {
    public static void main(String[] args) {
        CommunicationServer frame1 = new CommunicationServer();
        frame1.setVisible(true);
    }
}

