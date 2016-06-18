package cg.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager
{
   /** Singleton of this class. */
   static private ConnectionManager _mgr = null;

   /** Full path to the database. */
   private String _dbUrl = null;

   /** Database user. */
   private String _dbUser = "SA";

   /** Database user. */
   private String _dbPwd = "";

   /** Database connection. */
   private Connection _db = null;
   
   /**
    * Constructor
    * @param aDbUrl
    */
   private ConnectionManager()
   {
   }

   /**
    * Get the singleton instance.
    * @return
    */
   public static ConnectionManager getInstance()
   {
      if (_mgr == null)
      {
         _mgr = new ConnectionManager();
      }
      return _mgr;
   }
   
   /**
    * Set the database URL.
    * Need to call this method to make the database operational (from the
    * perspective of this process).
    */
   public void setDbUrl(String aDbUrl)
   {
      _dbUrl = aDbUrl;
   }
   
   //TODO determine what methods in this class are really needed/used.
   public Connection getConnection()
   {
      return connectToDB();
   }
   
   public void closeConnection(Connection aConn)
   {
      try
      {
         aConn.close();
      }
      catch (Exception ex)
      {
         System.out.println("Failed to close connection:\n" + ex);
      }
   }

   /**
    * Obtain a connection to the database.
    */
   private Connection connectToDB()
   {
      Connection tConn = null;
      
      /*
       * Verify that the driver is registered.
       */
      try
      {
         Class.forName("org.hsqldb.jdbcDriver");
      }
      catch (ClassNotFoundException ex)
      {
         System.out.println("Couldn't find the driver!\n" + ex);
         System.exit(1);
      }

      try
      {
         // The second and third arguments are the username and password,
         // respectively. They should be whatever is necessary to connect
         // to the database.
         tConn = DriverManager.getConnection(_dbUrl,_dbUser,_dbPwd);
      }
      catch (SQLException ex)
      {
         System.out.println("Couldn't connect:\n" + ex);
         System.exit(1);
      }

      if (tConn != null)
      {
         System.out.println("Connected to the database.");
      }
      else
      {
         System.out.println("ERROR: null database connection");
         System.exit(1);
      }

      return tConn;
   }
}
