package jdbc;

import java.sql.*;


public class Main1 {
	public static void main(String args[]) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mit","root","");
		Statement st=con.createStatement();
		
		DriverManager.setLoginTimeout(5);            
		//max time a driver could wait to establish connection with db,if 0 is passed then driver has to wait infinitely
		System.out.println("Login timeout:"+DriverManager.getLoginTimeout());
		
		String query1="SELECT c.s_name,c.reg_no,d.dept,h.hostel,h.room_no "
					+ "FROM clg c,departments d,hostel h "
					+ "WHERE c.dept_id=d.dept_id AND c.reg_no=h.reg_no";
				
		
//		PreparedStatement st1=con.prepareStatement(query3);
//		prepared statement replaces with question mark
//		st1.setString(1,"swaminathank2001@gmail.com");
//		st1.setInt(2,4062);
//		st1.executeUpdate();
//		st1.executeUpdate(query1);
//		executeUpdate returns number of records affected
		
		
		ResultSet rs=st.executeQuery(query1);
		ResultSetMetaData rsmd=rs.getMetaData();
		
		int i=1;
		while(i<=rsmd.getColumnCount()) {
			System.out.print(rsmd.getColumnName(i)+'\t');
			if(i==2) {
				System.out.print('\t');
			}
			i++;
		}
		System.out.print('\n');
		
		while(rs.next()){
			System.out.println(rs.getString("s_name")+'\t'+rs.getInt(2)+'\t'+rs.getString(3)+'\t'+rs.getString(4)+'\t'+rs.getInt(5));
		}
	
		
		st.close();
		con.close();
	}
}
