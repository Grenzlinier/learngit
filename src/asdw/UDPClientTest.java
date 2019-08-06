package asdw;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
class CommunicationClient extends JFrame implements Runnable {
 JPanel contentPane;
 JLabel jLabel1 = new JLabel();
 TextArea jTextArea1 = new TextArea("你好! 我是客户端。  ", 100, 50);
 JLabel jLabel2 = new JLabel();
 JTextField jTextField1 = new JTextField();
 Thread c;
 private DatagramSocket sendSocket,  receiveSocket;
 private DatagramPacket sendPacket,  receivePacket;
 private String name;
 public CommunicationClient() {
     try {
         jbInit();
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
 private void jbInit() throws Exception {
     contentPane = (JPanel) this.getContentPane();
     contentPane.setLayout(null);
     this.setSize(new Dimension(400, 200));
     this.setLocation(500, 0);
     this.setTitle("UDPCLient");
     jLabel1.setText("通信记录:");
     jLabel1.setBounds(new Rectangle(16, 9, 68, 27));
     contentPane.setLayout(null);
     jTextArea1.setBounds(new Rectangle(0, 35, 380, 90));
     jTextArea1.setEditable(false);
     jLabel2.setText("输入通信内容:");
     jLabel2.setBounds(new Rectangle(17, 125, 92, 37));  //创建输入内容区域
     jTextField1.setText("");
     jTextField1.setBounds(new Rectangle(127, 129, 244, 31));
     jTextField1.setEditable(true);
     jTextField1.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(ActionEvent e) {
             jTextField1_actionPerformed(e);
         }
     });
     contentPane.add(jLabel1, null);
     contentPane.add(jTextArea1, null);
     contentPane.add(jTextField1, null);
     contentPane.add(jLabel2, null);
     try {
         sendSocket = new DatagramSocket();
         receiveSocket = new DatagramSocket(8001);
     } catch (SocketException e) {
         jTextArea1.append("不能打开数据报Socket,或者数据报Socket无法与指定端口连接！");
     }
     c = new Thread(this);  //创建一个线程
     c.start();
 }
 public void run() {
     while (true) {
         try {
             byte buf[] = new byte[100];
             receivePacket = new DatagramPacket(buf, buf.length);
             receiveSocket.receive(receivePacket);
             name = receivePacket.getAddress().toString().trim();
             jTextArea1.append("\n来自主机:" + name + "\n端口:" + receivePacket.getPort());
             jTextArea1.append("\n服务器：");
             byte[] data = receivePacket.getData();
             String receiveString = new String(data);
             jTextArea1.append(receiveString);
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
         jTextArea1.append("\n客户端: ");
         String string1 = jTextField1.getText().trim();
         jTextArea1.append(string1);
         byte[] databyte = new byte[100];
         string1.getBytes(0, string1.length(), databyte, 0);
         DatagramPacket sendPacket = new DatagramPacket(databyte, string1.length(), InetAddress.getByName("127.0.0.1"), 8000);
         sendSocket.send(sendPacket);
     } catch (IOException ioe) {
         jTextArea1.append("网络通信出现错误,问题在于" + e.toString());
     }
 }
}
public class UDPClientTest {
 public static void main(String[] args) {
     CommunicationClient frame1 = new CommunicationClient();  //创建一个实例对象
     frame1.setVisible(true);
 }
}
