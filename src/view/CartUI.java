package view;

import business.BasketController;
import business.CartController;
import business.ProductController;
import core.Helper;
import entity.Basket;
import entity.Cart;
import entity.Customer;
import entity.Product;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartUI extends JFrame {
    private JPanel container;
    private JLabel lbl_title;
    private JLabel lbl_customer_name;
    private JTextField fld_cart_date;
    private JTextArea tarea_cart_note;
    private JButton btn_cart;
    private JLabel lbl_order_note;
    private JLabel lbl_order_date;
    private Customer customer;
    private BasketController basketController;
    private CartController cartController;
    private ProductController productController;
    public DashboardUI dashboardUI;



    public CartUI(Customer customer) {
        this.customer = customer;
        this.basketController = new BasketController();
        this.cartController = new CartController();
        this.productController = new ProductController(); // ProductController başlatılıyor

        this.add(container);
        this.setTitle("Sipariş Oluştur");
        this.setSize(300, 350);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        if (customer.getId() == 0) {
            Helper.showMsg("Lütfen Geçerli Bir Müşteri Giriniz !");
            dispose();
        }

        ArrayList<Basket> baskets = this.basketController.findAll();
        if (baskets.isEmpty()) {
            Helper.showMsg("Lütfen Sepete Ürün Ekleyiniz !!");
            dispose();
        }

        this.lbl_customer_name.setText("Müşteri : " + this.customer.getName());

        btn_cart.addActionListener(e -> {
            if (Helper.isTextEmpty(this.fld_cart_date)) {
                Helper.showMsg("fill");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                Map<Integer, Integer> productCountMap = new HashMap<>();

                for (Basket basket : baskets) {
                    if (basket.getProduct().getStock() <= 0) continue;
                    Cart cart = new Cart();
                    cart.setCustomerId(this.customer.getId());
                    cart.setProductId(basket.getProductId());
                    cart.setPrice(basket.getProduct().getPrice());
                    cart.setDate(LocalDate.parse(this.fld_cart_date.getText(), formatter));
                    cart.setNote(this.tarea_cart_note.getText());
                    this.cartController.save(cart);
                    Product unStockProduct = basket.getProduct();
                    unStockProduct.setStock(unStockProduct.getStock() - 1);
                    this.productController.update(unStockProduct);
                }
                this.basketController.clear();
                Helper.showMsg("done");

                dispose();
            }
        });

    }

    private void createUIComponents() throws ParseException {
        this.fld_cart_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fld_cart_date.setText(formatter.format(LocalDate.now()));
    }


}