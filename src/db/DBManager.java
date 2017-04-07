/*
	1. 정보를 한 곳에 두기
		얘를 만들어 둠으로써 데이터베이스 계정 정보를 중복해서 기재하지 않기 위함
		(DB연동을 하는 각각의 클래스에서..)
		클래스마다 이걸 넣을 경우 유지보수가 힘들어진다.
	
	2. 인스턴스의 갯수를 한개만 둬보기
		어플리케이션 가동 중 생성되는 Connection객체를 하나로 통일하기 위함
		
*/
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
	//생성자를 막아뒀기 때문에 얘를 가져올 수 있는 getter가 필요하다.
	static private DBManager instance;
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "batman";
	private String password = "1234";
	private Connection con;
	
	
	private DBManager(){
		/*
			1.드라이버 로드
			2.접속
			3.쿼리문 수행
			4.반납, 해제
		*/
		try {
			Class.forName(driver);  //여기서 class는 하나의 자료형. class에 대한 정보 class임.
			con = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	static public DBManager getInstance() {
		if(instance == null){
			instance = new DBManager();
		}
		return instance;
	}
	
	public Connection getConnection(){
		return con;
	}
	public void disConnection(Connection con){
		if(con!=null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
