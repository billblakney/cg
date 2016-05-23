package CapGains.db;

import java.sql.*;

public class PostgreSQL_Loader {

	  Connection       db = null;        // A connection to the database
	  Statement        sql;       // Our statement to run queries with

	public void loadTrades() {

	    try
	    {
			connectToDB();
			sql = db.createStatement(); //create a statement that we can use later
			getAccounts();
			db.close();
	    }
	    catch (Exception ex)
	    {
	      System.out.println("***Exception:\n"+ex);
	      ex.printStackTrace();
	    }
	}

	void connectToDB() {
		System.out
				.println("Checking if Driver is registered with DriverManager.");

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Couldn't find the driver!");
			System.out.println("Let's print a stack trace, and exit.");
			cnfe.printStackTrace();
			System.exit(1);
		}

		System.out
				.println("Registered the driver ok, so let's make a connection.");

		try {
			// The second and third arguments are the username and password,
			// respectively. They should be whatever is necessary to connect
			// to the database.
			db = DriverManager.getConnection(
					"jdbc:postgresql://localhost/capgains", "postgres",
					"postgres");
		} catch (SQLException se) {
			System.out
					.println("Couldn't connect: print out a stack trace and exit.");
			se.printStackTrace();
			System.exit(1);
		}

		if (db != null)
			System.out.println("Hooray! We connected to the database!");
		else
			System.out.println("We should never get here.");

	}

	void getAccounts() {
		
		try {
		System.out.println("Now executing the command: "
				+ "select * from jdbc_demo");
		ResultSet results = sql.executeQuery("select * from accounts1");
		if (results != null) {
			while (results.next()) {
				System.out.println("id = " + results.getInt("id")
						+ "; broker = " + results.getString("broker")
						+ "; name = " + results.getString("name") + "\n");
			}
		}
		results.close();
		}
	    catch (Exception ex)
	    {
	      System.out.println("***Exception:\n"+ex);
	      ex.printStackTrace();
	    }

	}
}
