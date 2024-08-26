package view;

import business.ProductController;
import core.Helper;
import entity.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductUI extends JFrame {
    private JPanel container;
    private JLabel lbl_title;
    private JTextField txt_product_name;
    private JTextField txt_product_code;
    private JTextField txt_product_price;
    private JTextField txt_product_stock;
    private JButton btn_product;
    private JLabel lbl_product_name;
    private JLabel lbl_product_code;
    private JLabel lbl_product_price;
    private JLabel lbl_product_stock;
    private Product product;
    private ProductController productController;


    public ProductUI(Product product){
        this.product = product;
        this.productController = new ProductController();

        this.add(container);
        this.setTitle("Ürün Ekle/Düzenle");
        this.setSize(400,500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        if (this.product.getId() == 0) {
            this.lbl_title.setText("Ürün Ekle");

        } else {
            this.lbl_title.setText("Ürün Düzenle");
            this.txt_product_name.setText(this.product.getName());
            this.txt_product_code.setText(this.product.getCode());
            this.txt_product_price.setText(String.valueOf(this.product.getPrice()));
            this.txt_product_stock.setText(String.valueOf(this.product.getStock()));
        }


        btn_product.addActionListener(e -> {
            JTextField[] checkList = {
                    this.txt_product_name,
                    this.txt_product_code,
                    this.txt_product_price,
                    this.txt_product_stock
            };
            if (Helper.isTextListEmpty(checkList)) {
                Helper.showMsg("fill");

            } else {
                this.product.setName(this.txt_product_name.getText());
                this.product.setCode(this.txt_product_code.getText());
                this.product.setPrice(Integer.parseInt(this.txt_product_price.getText()));
                this.product.setStock(Integer.parseInt(this.txt_product_stock.getText()));

                boolean result;
                if (this.product.getId() == 0) {
                    result = this.productController.save(this.product);

                } else {
                    result = this.productController.update(this.product);
                }

                if (result) {
                    Helper.showMsg("done");
                    dispose();

                } else {
                    Helper.showMsg("error");
                }
            }
        });
    }
}
