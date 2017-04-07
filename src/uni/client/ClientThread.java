/*
	실시간 청취를 위해, 메인쓰레드가 아닌 개발자 정의 쓰레드를 루프로 돌리자
*/
package uni.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientThread extends Thread{
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	JTextArea area;
	
	public ClientThread(Socket socket, JTextArea area) {
		this.socket = socket;
		this.area = area;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//말하기, 보내기
	public void send(String msg){		//매개변수로 받는 것이 더 유연하다
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//듣기, 받기
	public void listen(){
		
	}
	public void run() {
	
	}
}
