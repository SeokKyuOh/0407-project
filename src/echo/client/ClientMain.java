package echo.client;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import db.DBManager;

public class ClientMain extends JFrame implements ItemListener, ActionListener{	//���̽� â���� �����Ҷ��ϱ� ���콺 �����ʰ� �ƴ϶� ������ ������
	JPanel p_north;
	Choice choice;
	JTextField t_port, t_input;
	JButton bt_connect;
	JTextArea area;
	JScrollPane scroll;
	int port = 7777;
	DBManager manager;
	ArrayList<Chat> list = new ArrayList<Chat>();
	Socket socket; //��ȭ�� ����. ���� ��Ʈ���� �̾Ƴ�����
	String ip;
	ClientThread ct ;	//������ ��
	
	
	public ClientMain() {
		p_north = new JPanel();
		choice = new Choice();
		t_port = new JTextField(Integer.toString(port), 10);
		t_input = new JTextField(10);
		bt_connect = new JButton("����");
		area = new JTextArea();
		scroll = new JScrollPane(area);
		manager = DBManager.getInstance();
		
		p_north.add(choice);
		p_north.add(t_port);
		p_north.add(bt_connect);
		
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		add(t_input, BorderLayout.SOUTH);
		
		loadIP();		//������â�� �㶧 ����
		
		for(int i=0;i<list.size();i++){ //���̽��� �̸� ä���
			choice.add(list.get(i).getName());
		}
		
		//�����ʿ� ���̽� ����
		choice.addItemListener(this);
		bt_connect.addActionListener(this);
		t_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				int key = e.getKeyCode();
				
				if(key == KeyEvent.VK_ENTER){
					String msg = t_input.getText();
					ct.send(msg);	//������
					t_input.setText("");//�Է��� �۾� �����
				}
			}
		});
		
		setBounds(300, 100, 300, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	//�����ͺ��̽� ��������
	public void loadIP(){
		Connection con = manager.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from chat order by chat_id asc";
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			//rs�� ��� �����͸� dto�� �ű�� ����
			while(rs.next()){
				Chat dto = new Chat();
				dto.setChat_id(rs.getInt("chat_id"));
				dto.setName(rs.getString("name"));
				dto.setIp(rs.getString("ip"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			manager.disConnection(con);
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		Choice ch = (Choice)e.getSource();		//���̽��� �ٲ�����?? ���̽����� ���� ������ �ε������� �ֱ� ����
		int index = ch.getSelectedIndex();
		Chat chat = list.get(index);
		
		this.setTitle(chat.getIp());
		
		ip = chat.getIp();		//����������� ����
	}
	
	//������ ������ �õ�����
	public void connect(){
		//���ϻ����� ������ �߻���
		try {
			port=Integer.parseInt(t_port.getText());
			socket = new Socket(ip, port);
			
			//�ǽð����� ������ �޼����� û���ϱ� ����, �����带 �����Ͽ� ��ȭ������ �� �ðܹ�����.
			//���� ������&���� �����ڴ� ����(������)
			ct = new ClientThread(socket, area);
			ct.start();
			
			
			ct.send("�ȳ�?");
			
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public void actionPerformed(ActionEvent e) {
		connect();
	}
	
	public static void main(String[] args) {
		new ClientMain();
	}

	

}