package pl.coderslab.warsztat3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

// wzorzec active record dla tabeli user
//public User(){
//	
//	super();
//	this.long id;
//	this.String username;
//	this.String email;
//	this.String password;
//	this.int user_group_id;
//	
//}


public class User {

private long id;
private String username;
private String email;
private String password;
private int user_group_id;





public User(String username, String email, String password) {
	super();
	this.username = username;
	this.email = email;
	setPassword(password);
}


public String getUsername() {
	return username;
}


public void setUsername(String username) {
	this.username = username;
}


public String getEmail() {
	return email;
}


public void setEmail(String email) {
	this.email = email;
}


public String getPassword() {
	return password;
}



public void setPassword(String password) {
	this.password = BCrypt.hashpw(password, BCrypt.gensalt());
}

public void checkPassword(String password){
	BCrypt.checkpw(password, this.password);
}

public long getId() {
	return id;
}


public void setId(long id) {
	this.id = id;
}

public int getUser_group_id() {
	return user_group_id;
}


public void setUser_group_id(int user_group_id) {
	this.user_group_id = user_group_id;
}

public void save(Connection conn)throws SQLException {
	
	if (this.id == 0){
		String sql = "INSERT INTO users (username, email, password, user_group_id) "
				+"VALUES(?, ?, ?, ?);";
		String[] generatedColumns = {"ID"};//jednoelementowa tablica //zwróć mi bazo z tabeli id
		PreparedStatement ps = conn.prepareStatement(sql, generatedColumns);
		ps.setString(1, this.username);
		ps.setString(2, this.email);
		ps.setString(3, this.password);
		ps.setInt(4, this.user_group_id);
		ps.executeUpdate();//służy do tego żeby zmodyfikować dane a nieje pobrać
		//metoda do pobrania danych jest executeQuery
		ResultSet gk = ps.getGeneratedKeys(); //baza zwraca wyniki w obiekcie gk
		if (gk.next()){//jeżeli na następnej pozycji jest dana przestawiana next i zwraca true
			//tu nie ma iteracji dlatego tylko if
			this.id = gk.getLong(1);
		}
		gk.close();
		ps.close();
	}else{//przypadek kiedy id już mamy
		String sql = "UPDATE users SET username=?, email=?, password=?, user_group_id=?"
				+" WHERE id=? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, this.username);
		ps.setString(3, this.password);
		ps.setString(3, this.password);
		ps.setInt(4, this.user_group_id);
		ps.setLong(5, this.id);
		ps.executeUpdate();
		ps.close();
	}
	
}

//public static User getById(long id){
//	String sql = "";
//	//execute sql
//	User u = new User();  //domyślny konstruktor bezparametrowy
//}
			
	
}
