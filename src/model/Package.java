package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.MessageController;


public class Package extends NodeInf {
	private String name;
	private String type;
	private String username;
	private String password;
	private String connection;
	private Connection conn=null;
	ArrayList<Relacija> relations;

	public Package(String name, String type, String username, String password, String connection)
	{
		super(name);
		this.name = name;
		this.type = type;
		this.username = username;
		this.connection = connection;
		this.password = password;
		relations = new ArrayList<Relacija>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<Relacija> getRelations() {
		return relations;
	}
	
	public void setRelations(ArrayList<Relacija> relations) {
		this.relations = relations;
	}
	
	public void addRelation(Relacija r) {
		relations.add(r);
	}
	
	public void removeRelation(Relacija r) {
		this.relations.remove(r);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getConnection() {
		return connection;
	}
	
	public boolean openConnection(String serverName, String databaseName, String userName, String password)
	{
		boolean result = false;
		try
		{
			Class.forName("net.sourceforge.jtds.jdbc.Driver");
			String url = "jdbc:jtds:sqlserver://" + serverName + "/" + databaseName;
			
			System.out.println("DBG "+url);
			
			conn = DriverManager.getConnection(url, userName, password);

			result = true;

		}
		catch (Exception e)
		{
			MessageController.errorMessage(e.getMessage());
		}
		return result;

	}

	public Connection getConn()
	{
		return conn;
	}

	public void setConn(Connection conn)
	{
		this.conn = conn;
	}
}