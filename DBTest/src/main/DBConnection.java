package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DBConnection {

		
	 private static final String DB_TABLE = "comments"; 
	 
	 private final String name; 
	 private Connection connect;
	 private Statement statement;
	 private ResultSet resultSet;  
	 private PreparedStatement preparedStatement;
	 
	 
	 public DBConnection(String name){
		 this.name = name;
		 
	 }
	 
	
	 /**
	  * Abre la conexion a la db. 
	  * @throws Exception
	  */
	 public boolean connect() throws Exception {
		
        try {
        	
            // Cargar el driver MYSQL
            Class.forName("com.mysql.jdbc.Driver");
            // jdbc:mysql://ip database // database ? 
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/"+ name +"?"
                            + "user=root&password=poodb");

            // Statements allow to issue SQL queries to the database
            statement = connect.createStatement();
           
        } catch (Exception e) {
        	close(); 
            throw e;
        } 
        
        return statement!=null; 
    }
	
	 
	 /**
	  *      INSERT INTO tabla values (default, ?, ?, ?, ? , ?, ? )
	  * 
	  * 
	  * @param user
	  * @param email
	  * @param sumary
	  * @param comment
	 * @return 
	 * @throws SQLException 
	  */
	 public int insert(String user, String email, String webpage,String sumary, String comment)
			 	throws SQLException {
		 
		  int  lastInsertedId=-1; 
		  String strSQL = "insert into  "+ DB_TABLE +" values (default, ?, ?, ?, ? , ?, ?)";
	         
			try {
				preparedStatement = connect
						.prepareStatement(strSQL,Statement.RETURN_GENERATED_KEYS);
				
				 preparedStatement.setString(1,user);
				 preparedStatement.setString(2,email);
				 preparedStatement.setString(3,webpage);
				 preparedStatement.setDate(4,new java.sql.Date(System.currentTimeMillis()));
				 preparedStatement.setString(5,sumary);
				 preparedStatement.setString(6,comment);
				 
				    
		        preparedStatement.executeUpdate();	        
		        ResultSet rs = preparedStatement.getGeneratedKeys();
		        
		        if(rs.next())
		        	   lastInsertedId = rs.getInt(1);
			
			} catch (SQLException e) {
				close(); 
	            throw e;
			} 
		
	
			return lastInsertedId; 
	 }
	 
	 /**
	  * Elimina todos los reigistro de la tabla y reinicia la cuenta de ids
	  * @throws SQLException
	  */
	 public void deleteAll() throws SQLException{			
			try{
				preparedStatement = connect
				        .prepareStatement("truncate "+DB_TABLE);
		         preparedStatement.executeUpdate();		
		    } catch (SQLException e) {
		    	close(); 
	            throw e;
			}	         
	}
	 
	 /**
	  * Elimina un registro por su ID
	  * @param id registro a eliminar
	  * @throws SQLException
	  */
	 public void delete(int id) throws SQLException{		
			try {
				preparedStatement = connect
				        .prepareStatement("delete from "+DB_TABLE+"  where id= ? ; ");
				 preparedStatement.setInt(1, id);
		         preparedStatement.executeUpdate();
			
		    } catch (SQLException e) {
		    	close(); 
	            throw e;
			}
	}
	 

	/**
	 * SELECT columna FROM tabla WHERE key = value;
	 * @return 
	 * @throws SQLException 
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<HashMap> select(int id) throws SQLException { 
		String selectSQL = "SELECT id, myuser, email, webpage, summary,datum,comments FROM "+
							DB_TABLE+" WHERE id = ?";
		
		ArrayList<HashMap> map = null; 
		
		try {
			preparedStatement = connect
			        .prepareStatement(selectSQL);
			
			preparedStatement.setInt(1,id);
			
			resultSet = preparedStatement.executeQuery();
			
			 map= resultSetToCollection(resultSet); 
			 
		} catch (SQLException e) {
			close(); 
            throw e;
		} 
		
		return  map;  
	}
	
	
	/**
	 * help String strSQL = "UPDATE tabla set myuser = ?  where id = ?";
	 * 
	 * num_point
	 * 
	 */
	public void update(){
		
	}
 
	
	 // You need to close the resultSet
    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet=null; 
            }

            if (statement != null) {
                statement.close();
                statement = null; 
            }

            if (connect != null) {
                connect.close();
                connect = null; 
            }
            
        } catch (Exception e) {

        }
    }
    
    
    
	 @SuppressWarnings("rawtypes")
    private static ArrayList<HashMap> resultSetToCollection(ResultSet resultSet)
    		throws SQLException { 
        // ResultSet is initially before the first data set

		ArrayList<HashMap> list = new ArrayList<HashMap>(); 
    	
        while (resultSet.next()) {
        	
        	// lee el resultado i 
        	int id = resultSet.getInt("id");
            String user = resultSet.getString("myuser");
            String email = resultSet.getString("email");
            String website = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comment = resultSet.getString("comments");
       
            HashMap<String,String> hashMap = new HashMap<String,String>(); 
            hashMap.put("id",String.valueOf(id));
            hashMap.put("user",user);
            hashMap.put("email",email);
            hashMap.put("webpage",website);
            hashMap.put("summary",summary);
            hashMap.put("date",date.toString());
            hashMap.put("comments",comment);
            // adiciono el nuevo hashMap a el ArrayList<HashMap>
            list.add(hashMap);  
        }
        
        return list; 
    }

    
    
    public static void writeResultSet(HashMap<String,String> hashMap ){
            System.out.print("\tid: " +        hashMap.get("id"));
            System.out.print("\tUser: " +     hashMap.get("user"));
            System.out.print("\tWebsite: " +  hashMap.get("webpage"));
            System.out.print("\tsummary: " +  hashMap.get("summary"));
            System.out.print("\tDate: " +     hashMap.get("date"));
            System.out.print("\tComment: " +  hashMap.get("comments"));
            System.out.print("\t\n");
            
        }
   

}
