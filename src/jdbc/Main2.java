package jdbc;

import java.sql.*;

public class Main2 {

	public static void main(String[] args) throws Exception{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mit","root","");
		
		CallableStatement cstmt = con.prepareCall("call details(?)");
		cstmt.setString(1,"swami");
		
		ResultSet rs=cstmt.executeQuery();
		ResultSetMetaData rsmd=rs.getMetaData();
		System.out.println("Column count: "+rsmd.getColumnCount());
		
		while(rs.next()) {
			System.out.println(rs.getString(1)+'\t');
		}
		

	}

}
