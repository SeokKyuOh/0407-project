package echo.server;

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

public class ServerMain extends JFrame implements ActionListener {
	JPanel p_north;
	JTextField t_port;
	JButton bt_start;
	JTextArea area;
	JScrollPane scroll;
	int port = 7777;
	ServerSocket server;	//���Ӱ����� ����
	Thread thread;	//���� ������ ������.. ���ξ������ ��ؾ��ϴϱ� 
	BufferedReader buffr;
	BufferedWriter buffw;
	
	
	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port),15);		//ó������ ��Ʈ��ȣ �־�α�. JTextField���� String���� �;��ϱ� ������ int���� port�� ��ȯ 
		bt_start = new JButton("����");
		area = new JTextArea(15,20);
		scroll = new JScrollPane(area);
		
		p_north.add(t_port);
		p_north.add(bt_start);
		
		bt_start.addActionListener(this);
		
		add(p_north, BorderLayout.NORTH);
		add(scroll);
						
		setBounds(600, 100, 300, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	//���� ���� �� ����
	//������ ���� - checked Exception ����ó�� �����Ѵ�.
	//				   runtime Exception ����ó�� �������� �ʴ´�  //�������� �ʴ´ٰ� ó���� �������� �ʴ´ٸ� ������ ����ȴ�.
	public void startServer(){
		bt_start.setEnabled(false);	//��ư ��Ȱ��ȭ.. �ߺ� Ŭ���� �����ϱ� ����
		
		try {
			port = Integer.parseInt(t_port.getText());		//port�� int�̹Ƿ� ������ ��ȯ   �� �� shift+alt+z  �׸��� ù��°
			area.append("���� �غ� �Ϸ� \n");
			server = new ServerSocket(port);
			
			//����
			//����ζ� �Ҹ��� ���ξ������ ���� ���ѷ����� ���, ���� ���¿� ������ �ؼ��� �ȵȴ�.
			//����δ� �������� �̺�Ʈ�� �����ϰ� ���α׷��� ��ؾ� �ϹǷ� ���ѷ����� ��⿡ ������ ������ ������ �� �� ����.
			//����Ʈ�� ���ߺо߿����� �̿Ͱ��� �ڵ�� �̹� ������ Ÿ�Ӻ��� �����߻���
			Socket socket = server.accept();		//�����带 ���� ������ ������ ���⼭ ���ξ����尡 ���Ѵ�⿡ ������.
			area.append("���� ����");
			
			//Ŭ���̾�Ʈ�� ��ȭ�� �ϱ� ���� ������ ���̹Ƿ�
			//������ �Ǵ� ���� ��Ʈ���� ������.
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
			String data;
			while(true){
				data = buffr.readLine();	//Ŭ���̾�Ʈ�� �޼��� �ޱ�
				area.append("Ŭ���̾�Ʈ�� �� : "+data+"\n");	
				buffw.write(data+"\n");	//Ŭ���̾�Ʈ�� �޼��� ������		������ ������ �˰��ϱ� ���� �������� \n �߰�
				buffw.flush();					//���� ����
			}
	
		} catch (NumberFormatException e) {		//�����Ͻ� �߻��� ������ �����ϴ� ���� �ƴ϶� ��Ÿ�ӽ� �߻��ϴ� ���� ������ ���� ��. ��Ÿ�� �ͼ���. �������� �ʴ� ����.
			JOptionPane.showMessageDialog(this, "��Ʈ�� ���ڷ� �־��ּ���");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void actionPerformed(ActionEvent e) {
		thread = new Thread(){
			public void run() {
				startServer();
			}
		};
		thread.start();
	}
	
	public static void main(String[] args) {
		new ServerMain();

	}

}
