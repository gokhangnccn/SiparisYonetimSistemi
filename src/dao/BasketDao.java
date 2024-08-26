package dao;

import core.DB;
import entity.Basket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BasketDao {
    private Connection connection;
    private ProductDao productDao;

    public BasketDao() {
        this.connection = DB.getInstance();
        this.productDao = new ProductDao();
    }


    public ArrayList<Basket> findAll() {
        ArrayList<Basket> baskets = new ArrayList<>();

        try {
            ResultSet rs = this.connection.createStatement().executeQuery("SELECT * FROM public.basket");
            while (rs.next()) {
                baskets.add(this.match(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return baskets;

    }

    public boolean save(Basket basket) {
        String query = "INSERT INTO public.basket (product_id) VALUES (?)";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);

            pr.setInt(1, basket.getProductId());

            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean clear() {
        String query = "DELETE FROM public.basket";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            return pr.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Basket match(ResultSet rs) throws SQLException {
        Basket basket = new Basket();
        basket.setId(rs.getInt("id"));
        basket.setProductId(rs.getInt("product_id"));
        basket.setProduct(this.productDao.getById(rs.getInt("product_id")));

        return basket;
    }
}
