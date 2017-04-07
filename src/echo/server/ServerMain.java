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
	ServerSocket server;	//접속감지용 소켓
	Thread thread;	//서버 가동용 쓰레드.. 메인쓰레드는 운영해야하니까 
	BufferedReader buffr;
	BufferedWriter buffw;
	
	
	public ServerMain() {
		p_north = new JPanel();
		t_port = new JTextField(Integer.toString(port),15);		//처음부터 포트번호 넣어두기. JTextField에는 String형만 와야하기 때문에 int형인 port를 변환 
		bt_start = new JButton("접속");
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
	
	//서버 생성 및 가동
	//예외의 종류 - checked Exception 예외처리 강요한다.
	//				   runtime Exception 예외처리 강요하지 않는다  //강요하지 않는다고 처리를 따로하지 않는다면 비정상 종료된다.
	public void startServer(){
		bt_start.setEnabled(false);	//버튼 비활성화.. 중복 클릭을 방지하기 위해
		
		try {
			port = Integer.parseInt(t_port.getText());		//port는 int이므로 정수로 변환   블럭 후 shift+alt+z  그리고 첫번째
			area.append("서버 준비 완료 \n");
			server = new ServerSocket(port);
			
			//가동
			//실행부라 불리는 메인쓰레드는 절대 무한루프나 대기, 지연 상태에 빠지게 해서는 안된다.
			//실행부는 유저들의 이벤트를 감지하거 프로그램을 운영해야 하므로 무한루프나 대기에 빠지면 본연의 업무를 할 수 없다.
			//스마트폰 개발분야에서는 이와같은 코드는 이미 컴파일 타임부터 에러발생함
			Socket socket = server.accept();		//쓰레드를 따로 빼놓지 않으면 여기서 메인쓰레드가 무한대기에 빠진다.
			area.append("서버 가동");
			
			//클라이언트는 대화를 하기 위해 접속한 것이므로
			//접속이 되는 순간 스트림을 얻어놓자.
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
			String data;
			while(true){
				data = buffr.readLine();	//클라이언트의 메세지 받기
				area.append("클라이언트의 말 : "+data+"\n");	
				buffw.write(data+"\n");	//클라이언트의 메세지 보내기		버퍼의 끝임을 알게하기 위해 마지막에 \n 추가
				buffw.flush();					//버퍼 비우기
			}
	
		} catch (NumberFormatException e) {		//컴파일시 발생할 에러를 방지하는 것이 아니라 런타임시 발생하는 에러 방지를 위한 것. 런타임 익셉션. 강요하지 않는 예외.
			JOptionPane.showMessageDialog(this, "포트는 숫자로 넣어주세요");
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
