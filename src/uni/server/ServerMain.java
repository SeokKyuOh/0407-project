package uni.server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ServerMain extends JFrame implements ActionListener{
	JPanel p_north; 
	JTextField t_port;
	JButton bt_start;
	JTextArea area;
	JScrollPane scroll;
	int port=7777;
	ServerSocket server;
	Thread thread; 		//서버운영 쓰레드
	
	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port) ,10);
		bt_start = new JButton("가동");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		p_north.add(t_port);
		p_north.add(bt_start);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		bt_start.addActionListener(this);
		
		setBounds(600,100,300,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//서버를 가동한다.
	public void startServer(){
		bt_start.setEnabled(false);
		
		try {
			port = Integer.parseInt(t_port.getText());
			server = new ServerSocket(port);
			area.append("서버 생성 \n");
			
			thread = new Thread(){
				public void run() {
					try {
						while(true){//여러명의 접속자를 받아들이기 위해 무한루프
							Socket socket = server.accept();
							String ip=socket.getInetAddress().getHostAddress();	//ip 구하기
							area.append(ip+" 접속자 발견\n");
							
							//접속자마다 아바타 생성해주고 대화를 나눌 수 있도록 해주자
							Avatar av = new Avatar(socket, area);
							av.start();
							
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			};
			thread.start();		//쓰레드 동작.시작.
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		startServer();
	}
	
	public static void main(String[] args) {
		new ServerMain();
	}
}