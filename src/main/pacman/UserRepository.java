package main.pacman;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserRepository {
    private JDBCUtil jdbcUtil;
    private List<User> userList;


    public  UserRepository(){
        userList = new ArrayList<>();
        this.jdbcUtil = new JDBCUtil(); }

    /**
     * Method that gets the users from the database and adds them to the in-memory list.
     * The list is ordered descending by score.
     */
    public void findAll()
    {
        try
        {
            this.userList = new ArrayList<>();
            PreparedStatement preparedStatement = jdbcUtil.getConnection().prepareStatement("select * from users");
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next())
            {
                User user = new User();
                user.setName(resultSet.getString("userName"));
                user.setScore(resultSet.getInt("score"));
                user.setResultDate(resultSet.getDate("resultDate"));
                this.userList.add(user);
            }
        }
        catch(Exception e)
        {
            this.userList = null;
        }

        Comparator<User> userComparator = Comparator.comparingInt(User::getScore);
        userList.sort(userComparator.reversed());
    }

    /**
     * Method that adds a user to the in-memory list.
     * If the user already exists in the database, the existent data will be updated.
     * @param user user to be added
     */
    public void addUserToList(User user)
    {
        User foundUser = findUserByName(user);
        if(foundUser == null)
        {
            this.userList.add(user);
        }
        else
        {
            int userPosition = this.userList.indexOf(foundUser);
            this.userList.set(userPosition, user);
        }
    }

    /**
     * Searches the user in in-memory list
     * @param u searched user
     * @return the user if it already exists, null otherwise
     */

    private User findUserByName(User u)
    {
        for(User user: this.userList)
            if(user.getName().equals(u.getName()))
                return user;

        return null;
    }

    /**
     * Method that adds all the users from the in-memory list to the database.
     */

    public void addUsersToDatabase()
    {
        String sqlDeleteStatement = "DELETE FROM users";
        String sqlStatement = "INSERT INTO users(userName,score,resultDate) VALUES (?, ?, ?)";

        try{

            PreparedStatement deleteStatement = jdbcUtil.getConnection().prepareStatement(sqlDeleteStatement);
            deleteStatement.execute();

            for(User u:this.userList)
            {
                PreparedStatement preparedStatement = jdbcUtil.getConnection().prepareStatement(sqlStatement);
                preparedStatement.setString(1,u.getName());
                preparedStatement.setInt(2,u.getScore());
                preparedStatement.setDate(3,  u.getResultDate());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e)
        {
            System.out.println("Error at introducing users into database!");
        }
    }

    /**
     * Getter for the userlist
     * @return list of users
     */

    public List<User> getUserList() {
        return userList;
    }

}
