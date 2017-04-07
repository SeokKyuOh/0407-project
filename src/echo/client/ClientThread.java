/*
	키보드 입력시 마다 서버에 메세지를 보내고 다시 받아오게 처리하면 생기는 문제점?
	-> 키보드를 치지 않으면 서버의 메세지를 실시간으로 받아올 수 없다.
	*해결책?
	이벤트 발생과 상관없이 언제나 무한루프를 돌면서 서버의 메세지를 청취할 수 있는 별도의 실행부(쓰레드)를 만들자
*/
package echo.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ClientThread extends Thread{
	boolean flag = true;
	Socket socket;
	BufferedReader buffr;
	BufferedWriter buffw;
	JTextArea area;
	
	public ClientThread(Socket socket, JTextArea area) {		//기존에 있는 area와 socket을 받아오기 위해
		this.socket = socket;
		this.area = area;	
		
		//대화를 나누기 전에 스트림 얻어놓기
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	//서버에 메세지 보내기(말하기)
	public void send(String msg){
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	//서버의 메세지 받아오기(듣기)
	public void listen(){
		String msg = null;
		try {
			msg = buffr.readLine();
			area.append(msg+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		while(flag){
			//듣자
			listen();
			
		}
	}
	
	
}
