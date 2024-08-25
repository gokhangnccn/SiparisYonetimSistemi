package dao;

import core.DB;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDao {
    private Connection connection;

    public UserDao(){
        this.connection = DB.getInstance();
    }

    public User findByLogin(String mail, String password){
        User user = null;

        String query = "SELECT * FROM public.user WHERE mail = ? AND password = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, mail);
            pr.setString(2, password);
            ResultSet rs = pr.executeQuery();

            if(rs.next()){
                user = this.match(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public ArrayList<User> findAll(){
        ArrayList<User> users = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery("select * from public.user");
            while(rs.next()){
                users.add(this.match(rs));
            }

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
        return users;
    }

    public User match(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setMail(rs.getString("mail"));
        user.setPassword(rs.getString("password"));

       return user;
    }
}
