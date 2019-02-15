package main.pacman;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {
    private String className = "org.postgresql.Driver";
    private String url;
    private String user;
    private String password;
    private String databasename;
    private Connection connection;

    public JDBCUtil()
    {
        getPropertyValues();
    }

    /**
     * Method that gets the properties of the database
     */
    private void getPropertyValues() {
        InputStream inputStream;
        try{
            Properties properties = new Properties();
            String fileName = "main/ressources/application.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

            if(inputStream != null)
            {
                properties.load(inputStream);
            }
            else{
                throw new FileNotFoundException("Property file " + fileName + "not found!");
            }
            this.className = properties.getProperty("classname");
            this.user = properties.getProperty("username");
            this.password = properties.getProperty("password");
            this.databasename = properties.getProperty("databasename");
            this.url = properties.getProperty("url");
        }catch(Exception e)
        {
            System.out.println("Problem with database file!");
        }
    }

    /**
     * Method that connects the database to the application
     * @return connection to the database or null if the connection fails
     */
    public Connection getConnection()
    {
        try
        {
            Class.forName(this.className);
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("Classname not found!");
            System.exit(-1);
        }

        try
        {
            return DriverManager.getConnection(url + databasename, user, password);
        }
        catch(SQLException e)
        {
            System.out.println("Error getting connection: " + e.getMessage());
            System.exit(-1);
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
            System.exit(-1);
        }

        return null;

    }


}