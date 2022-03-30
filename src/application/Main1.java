package application;

import java.sql.*;
import java.util.Scanner;
import java.util.*;

public class Main1 {
	private static int getEmployeeId(Statement st,String employeeName) throws Exception {
		String query3="SELECT emp_id from PERSONAL_DETAILS "
				+ "WHERE emp_name=\""+employeeName+"\"";
		ResultSet rs=st.executeQuery(query3);
		rs.next();
		int employeeId=rs.getInt(1);
		rs.close();
		return (employeeId);
	}
	
	private static String getEmployeeName(Statement st,int employeeId) throws Exception {
		String query3="SELECT emp_name from PERSONAL_DETAILS "
				+ "WHERE emp_id=\""+employeeId+"\"";
		ResultSet rs=st.executeQuery(query3);
		rs.next();
		String employeeName=rs.getString(1);
		rs.close();
		return (employeeName);
	}
	
	public static void main(String args[]) throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://127.0.0.1/records","root","");
		Statement st=con.createStatement();
		
		
		System.out.println("---------------------------------------------------------------THIS IS MENU--------------------------------------------------------------------------------------------------"
				+ "\n1.PRINT ALL RECORDS IN DATABASE"
				+"\n2.PRINT RECORDS BASED ON QUERIES"
				+"\n3.PRINT EMPLOYEES REPORTING TO A MANAGER"
				+"\n4.REPORTING TREE FOR EMPLOYEE"
				+"\n5.SUMMARY REPORTS");
		System.out.print("Enter the operation to be perfromed:");
		Scanner sc=new Scanner(System.in);
		int operation=sc.nextInt();
		
		
//		--------------------------------------------PRINT ALL RECORDS IN DATABASE---------------------------------------------------------------------
		if(operation==1) {
			String query1="SELECT p.emp_id,p.emp_name,p.age,DEPARTMENT_DETAILS.dept_name,DESIGNATION_DETAILS.desig_name,p1.emp_name FROM PERSONAL_DETAILS p\n"
					+ "INNER JOIN DEPARTMENT_DETAILS ON DEPARTMENT_DETAILS.dept_id=p.dept_id\n"
					+ "INNER JOIN DESIGNATION_DETAILS ON DESIGNATION_DETAILS.desig_id=p.desig_id\n"
					+ "LEFT JOIN PERSONAL_DETAILS p1 ON p.reporting_to=p1.emp_id";
			ResultSet rs=st.executeQuery(query1);
			ResultSetMetaData rsmd=rs.getMetaData();
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getInt(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6));
			}
			rs.close();
		}
		
		
//		---------------------------------------------PRINT RECORDS BASED ON QUERIES----------------------------------------------------------------------
		else if(operation==2) {
			HashMap<Integer,String> h=new HashMap<Integer,String>();
			h.put(1, " = ");
			h.put(2, " != ");
			h.put(3, " LIKE ");
			h.put(4, " LIKE ");
			h.put(5, " LIKE");
			h.put(6, " NOT LIKE ");
			h.put(7, " < ");
			h.put(8, " > ");
			
			
			String query2="SELECT p.emp_id,p.emp_name,p.age,DEPARTMENT_DETAILS.dept_name,DESIGNATION_DETAILS.desig_name,p1.emp_name FROM PERSONAL_DETAILS p\n"
						+ "INNER JOIN DEPARTMENT_DETAILS ON DEPARTMENT_DETAILS.dept_id=p.dept_id\n"
						+ "INNER JOIN DESIGNATION_DETAILS ON DESIGNATION_DETAILS.desig_id=p.desig_id\n"
						+ "LEFT JOIN PERSONAL_DETAILS p1 ON p.reporting_to=p1.emp_id WHERE ";
			int moreQuery=1;
			int queryCount=1;
			
			
			while(moreQuery==1) {
				System.out.println("Search fields are:\n1.employee name,\n2.age,\n3.department,\n4.designation,\n5.reporting_to \nEnter the field name:");
				int field_name=sc.nextInt();
				
				if(field_name==2) {
					System.out.print("possible criteria are:\n1.equal \n2.not equals \n7.less than \n8.greater than \nEnter the operation: ");
					int op=sc.nextInt();
					
					System.out.print("Enter the value for operation: ");
					int val=sc.nextInt();
					
					if(queryCount>1) {
						query2+=" AND ";
					}
					
					if(op==1 || op==2 || op==7 || op==8) {
						query2=query2+" p.age"+" "+h.get(op)+" "+val;
					}
				}
				
				
				else {
					System.out.print("possible criteria are:\n1.equal \n2.not equals \n3.starts with \n4.ends with \n5.contains \n6.not contains \nEnter the operation: ");
					int op=sc.nextInt();
					
					System.out.print("Enter the value for operation: ");
					String val=sc.next();
					
					if(queryCount>1) {
						query2+=" AND ";
					}
					
					if(field_name==1) {
						query2+="p.emp_name "+h.get(op)+" ";
					}
					else if(field_name==3) {
						query2+="DEPARTMENT_DETAILS.dept_name "+h.get(op)+" ";
					}
					else if(field_name==4) {
						query2+="DESIGNATION_DETAILS.desig_name "+h.get(op)+" ";
					}
					else {
						query2+="p1.emp_name "+h.get(op)+" ";
					}
					
					if(op==1 || op==2) {
						query2+="\""+val+"\"";
					}
					else if(op==3) {
						query2+="\""+val+"%\"";
					}
					else if(op==4) {
						query2+="\"%"+val+"\"";
					}
					else {
						query2+="\"%"+val+"%\"";
					}
				}
				
				System.out.print("Do you want to enter more queries(1/0):");
				moreQuery=sc.nextInt();
				queryCount+=moreQuery;
			}
			
			ResultSet rs=st.executeQuery(query2);
			System.out.println(query2);
			while(rs.next()) {
				System.out.println(rs.getInt(1)+"\t"+rs.getString(2)+"\t"+rs.getInt(3)+"\t"+rs.getString(4)+"\t"+rs.getString(5)+"\t"+rs.getString(6));
			}
			rs.close();
		}
		
		
//		---------------------------------------------------PRINT EMPLOYEES REPORTING TO A MANAGER-------------------------------------------------------------------
		else if(operation==3) {
			System.out.print("Enter manager name:");
			String managerName=sc.next();
			int currentReportingTo=getEmployeeId(st,managerName);
			String query3;
			ArrayList<Integer> employees=new ArrayList<Integer>();
			ListIterator<Integer> employee=employees.listIterator();
			
			
			while(true){
				query3="SELECT emp_id FROM PERSONAL_DETAILS "
						+"WHERE reporting_to=\""+currentReportingTo+"\"";
				ResultSet rs1=st.executeQuery(query3);
				while(rs1.next()) {
					employee.add(rs1.getInt(1));			
				}
				if(employee.hasPrevious()) {
					currentReportingTo=employee.previous();
				}
				else {
					query3="SELECT emp_name FROM PERSONAL_DETAILS "
						  +"WHERE emp_id IN "+employees;
					query3=query3.replace('[','(');
					query3=query3.replace(']',')');
					ResultSet rs2=st.executeQuery(query3);
					System.out.print("Employees under manager "+managerName+" : ");
					while(rs2.next()) {
						System.out.print(rs2.getString(1)+"  ");
					}
					rs2.close();
					break;
				}
				rs1.close();
			}
		}
		
		
		
//		------------------------------------------------------------REPORTING TREE FOR EMPLOYEE----------------------------------------------------------------
		else if(operation==4) {
			System.out.print("Enter the employee name:");
			sc.nextLine();
			String employeeName=sc.nextLine();
			int employeeId=getEmployeeId(st,employeeName);;
			ArrayList<Integer> arr=new ArrayList<Integer>();
			arr.add(employeeId);
			String query4;
			Boolean flag=true;
			while(flag) {
				flag=false;
				query4="SELECT emp_id FROM PERSONAL_DETAILS p1\n"
						+ "WHERE emp_id=(\n"
						+ "SELECT reporting_to FROM PERSONAL_DETAILS p\n"
						+ "WHERE emp_id=\""+employeeId+"\")";
				ResultSet rs=st.executeQuery(query4);
                while(rs.next()) {
    				employeeId=rs.getInt(1);
    				arr.add(employeeId);
    				flag=true;;
                }
                rs.close();
			}
			int i=0;
			while(i<arr.size()) {
				if(i==0) {
					System.out.print(getEmployeeName(st,arr.get(i)));
				}
				else {
					System.out.print("---->"+getEmployeeName(st,arr.get(i)));
				}
				i++;
			}
			
		}
		
		
//		------------------------------------------------------SUMMARY REPORTS-----------------------------------------------------------------------------
		else {
			System.out.print("Summaries available:\n1.Departments summary \n2.Designation summary \n3.Manager summary \nEnter the operation:");
			int op=sc.nextInt();
			String query5="";
			if(op==1) {
				query5+="SELECT d.dept_name,COUNT(p.emp_id) AS count FROM PERSONAL_DETAILS p\n"
						+ "INNER JOIN DEPARTMENT_DETAILS d\n"
						+ "ON d.dept_id=p.dept_id\n"
						+ "GROUP BY p.dept_id";
				ResultSet rs=st.executeQuery(query5);
				System.out.println("Department name\tCount");
				while(rs.next()) {
					System.out.println(rs.getString(1)+'\t'+rs.getInt(2));
				}
				rs.close();
			}
			else if(op==2) {
				query5+="SELECT d.desig_name,COUNT(p.emp_id) AS count FROM PERSONAL_DETAILS p\n"
						+ "INNER JOIN DESIGNATION_DETAILS d\n"
						+ "ON d.desig_id=p.desig_id\n"
						+ "GROUP BY p.desig_id";
				ResultSet rs=st.executeQuery(query5);
				System.out.println("Designation name\tCount");
				while(rs.next()) {
					System.out.println(rs.getString(1)+'\t'+rs.getInt(2));
				}
				rs.close();
			}
			else {
				String findManager="SELECT emp_id,emp_name FROM PERSONAL_DETAILS \n"
							+ "WHERE desig_id in(\n"
							+ "SELECT desig_id FROM DESIGNATION_DETAILS \n"
							+ "WHERE desig_name LIKE \"%manager%\")";
				ResultSet rs=st.executeQuery(findManager);
				
				ArrayList<Integer> managersId=new ArrayList<Integer>();
				ArrayList<String> managersName=new ArrayList<String>();
				while(rs.next()) {
					managersId.add(rs.getInt(1));
					managersName.add(rs.getString(2));
				}
				
				System.out.println(managersId);
				
				int[] c=new int[managersId.size()];
				Arrays.fill(c,0);
				
				for(int i=0;i<managersId.size();i++) {
					int currentReportingTo=managersId.get(i);
					
					ArrayList<Integer> employees=new ArrayList<Integer>();
					ListIterator<Integer> employee=employees.listIterator();
			

					while(true){
						query5="SELECT emp_id FROM PERSONAL_DETAILS "
								+"WHERE reporting_to=\""+currentReportingTo+"\"";
						ResultSet rs1=st.executeQuery(query5);
						while(rs1.next()) {
							employee.add(rs1.getInt(1));
							c[i]++;			
						}
						if(employee.hasPrevious()) {
							currentReportingTo=employee.previous();
						}
						else {
							System.out.println(employees);
							break;
						}
					}
				}
				
				System.out.println("MANAGER\tCOUNT");
				for(int i=0;i<managersName.size();i++) {
					System.out.println(managersName.get(i)+"\t"+c[i]+'\n');
				}
				rs.close();
			}
		}
		
		sc.close();
		con.close();
		st.close();
	}
}
