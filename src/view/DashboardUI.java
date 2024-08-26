package view;

import business.BasketController;
import business.CustomerController;
import business.ProductController;
import core.Helper;
import core.Item;
import entity.Basket;
import entity.Customer;
import entity.Product;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class DashboardUI extends JFrame {
    private JPanel container;
    private JLabel lblWelcome;
    private JButton btnLogout;
    private JTabbedPane tabMenu;
    private JPanel panelCustomer;
    private JTable table_customer;
    private JScrollPane scrl_customer;
    private JPanel pnl_customer_filter;
    private JTextField txt_f_customer_name;
    private JComboBox<Customer.TYPE> cbox_f_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private JPanel panelProduct;
    private JScrollPane scrl_product;
    private JTable table_product;
    private JPanel pnl_product_filter;
    private JTextField txt_f_product_name;
    private JTextField txt_f_product_code;
    private JComboBox<Item> cbox_f_product_stock;
    private JComboBox<Item> cbox_basket_customer;

    private JButton btn_product_filter;
    private JButton btn_product_filter_reset;
    private JButton btn_product_new;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private JPanel panelBasket;
    private JPanel pnl_basket_top;
    private JScrollPane scrl_basket;
    private JButton btn_basket_reset;
    private JButton btn_basket_new;
    private JLabel lbl_basket_price;
    private JLabel lbl_basket_count;
    private JTable table_basket;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private BasketController basketController;
    private DefaultTableModel mdl_customer_table = new DefaultTableModel();
    private DefaultTableModel mdl_product_table = new DefaultTableModel();
    private DefaultTableModel mdl_basket_table = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();

    public DashboardUI(User user){
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
        this.basketController = new BasketController();
        if(user == null){
            Helper.showMsg("error");
            this.dispose();
        }

        this.add(container);
        this.setTitle("Müşteri Yönetim Sistemi");
        this.setSize(1000,500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        this.lblWelcome.setText("Hoşgeldin: " + this.user.getName());

        this.btnLogout.addActionListener(e -> {
            dispose();
            LoginUI loginUI = new LoginUI();
        });


        //CUSTOMER TAB
        loadCustomerTable(null);
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();
        this.cbox_f_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));
        this.cbox_f_customer_type.setSelectedItem(null);

        //PRODUCT TAB
        loadProductTable(null);
        loadProductPopupMenu();
        loadProductButtonEvent();

        this.cbox_f_product_stock.addItem(new Item(1,"Stokta Var"));
        this.cbox_f_product_stock.addItem(new Item(2,"Stokta Yok"));
        this.cbox_f_product_stock.setSelectedItem(null);

        //BASKET TAB
        loadBasketTable();
        loadBasketButtonEvent();
        loadBasketCustomerCombo();

    }


    //PRODUCT
    private void loadProductTable(ArrayList<Product> products){
        Object[] columnProduct = {"ID", "Ürün Adı", "Ürün Kodu", "Fiyat", "Stok"};

        if(products == null){
            products = this.productController.findAll();
        }

        //tabloyu sıfırlamak için
        DefaultTableModel clearModel = (DefaultTableModel) this.table_product.getModel();
        clearModel.setRowCount(0);

        this.mdl_product_table.setColumnIdentifiers(columnProduct); //modelin columnlarını atadık.


        for(Product product : products){
            Object[] rowObject = {
                    product.getId(),
                    product.getName(),
                    product.getCode(),
                    product.getPrice(),
                    product.getStock(),
            };
            this.mdl_product_table.addRow(rowObject);
        }


        this.table_product.setModel(mdl_product_table);
        this.table_product.getTableHeader().setReorderingAllowed(false);
        this.table_product.getColumnModel().getColumn(0).setMaxWidth(50);
        this.table_product.setEnabled(false);

    }

    private void loadProductButtonEvent(){
        this.btn_product_new.addActionListener(e -> {
            ProductUI productUI = new ProductUI(new Product());
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });

        this.btn_product_filter.addActionListener(e -> {
            ArrayList<Product> filteredProducts = this.productController.filter(
                    this.txt_f_product_name.getText(),
                    this.txt_f_product_code.getText(),
                    (Item) this.cbox_f_product_stock.getSelectedItem()
            );
            loadProductTable(filteredProducts);

        });

        this.btn_product_filter_reset.addActionListener(e -> {
            this.txt_f_product_code.setText(null);
            this.txt_f_product_name.setText(null);
            this.cbox_f_product_stock.setSelectedItem(null);
            loadProductTable(null);

        });
    }

    private void loadProductPopupMenu() {
        this.table_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectRow = table_product.rowAtPoint(e.getPoint());
                table_product.setRowSelectionInterval(selectRow, selectRow);
            }
        });

        this.popup_product.add("Sepete Ekle").addActionListener(e -> {
            int selectId = (int) table_product.getValueAt(table_product.getSelectedRow(), 0);
            Product basketProduct = this.productController.getById(selectId);

            if(basketProduct.getStock() <= 0){
                Helper.showMsg("Bu ürün stokta yoktur!");
            } else {
                Basket basket = new Basket(basketProduct.getId());

                if (this.basketController.save(basket)) {
                    Helper.showMsg("done");
                    loadBasketTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.popup_product.add("Güncelle").addActionListener(e -> {
            int selectId = (int) table_product.getValueAt(table_product.getSelectedRow(), 0);
            ProductUI productUI = new ProductUI(this.productController.getById(selectId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                    loadBasketTable();
                }
            });
        });
        popup_product.add("Sil").addActionListener(e -> {
            int selectId = (int) table_product.getValueAt(table_product.getSelectedRow(), 0);

            if (Helper.confirm("sure")) {
                if (this.productController.delete(selectId)) {
                    Helper.showMsg("done");
                    loadProductTable(null);
                    loadBasketTable();
                } else {
                    Helper.showMsg("error");

                }
            }
        });

        this.table_product.setComponentPopupMenu(this.popup_product);
    }



    //CUSTOMER
    private void loadCustomerTable(ArrayList<Customer> customers){
        Object[] columnCustomer = {"ID", "Müşteri Adı", "Tipi", "Telefon", "E-posta", "Adres"};

        if(customers == null){
            customers = this.customerController.findAll();
        }

        //tabloyu sıfırlamak için
        DefaultTableModel clearModel = (DefaultTableModel) this.table_customer.getModel();
        clearModel.setRowCount(0);
        this.mdl_customer_table.setColumnIdentifiers(columnCustomer); //modelin columnlarını atadık.


        for(Customer customer : customers){
            Object[] rowObject = {
                    customer.getId(),
                    customer.getName(),
                    customer.getType(),
                    customer.getPhone(),
                    customer.getMail(),
                    customer.getAddress()
            };
            this.mdl_customer_table.addRow(rowObject);
        }


        this.table_customer.setModel(mdl_customer_table);
        this.table_customer.getTableHeader().setReorderingAllowed(false);
        this.table_customer.getColumnModel().getColumn(0).setMaxWidth(50);
        this.table_customer.setEnabled(false);

    }

    private void loadCustomerButtonEvent(){
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });

        this.btn_customer_filter.addActionListener(e -> {
            ArrayList<Customer> filteredCustomers = this.customerController.filter(
                    this.txt_f_customer_name.getText(),
                    (Customer.TYPE) this.cbox_f_customer_type.getSelectedItem()
            );

            loadCustomerTable(filteredCustomers);
        });

        btn_customer_filter_reset.addActionListener(e -> {
            loadCustomerTable(null);
            this.txt_f_customer_name.setText(null);
            this.cbox_f_customer_type.setSelectedItem(null);

        });
    }

    private void loadCustomerPopupMenu(){
        this.table_customer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = table_customer.rowAtPoint(e.getPoint());
                table_customer.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        this.popup_customer.add("Güncelle").addActionListener(e -> {
            int selectId = (int) table_customer.getValueAt(table_customer.getSelectedRow(), 0);
            Customer editedCustomer = this.customerController.getById(selectId);
            CustomerUI customerUI = new CustomerUI(this.customerController.getById(selectId));
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }
            });
        });
        this.popup_customer.add("Sil").addActionListener(e -> {
            int selectId = (int) table_customer.getValueAt(table_customer.getSelectedRow(), 0);
            if(Helper.confirm("sure")){
                if(this.customerController.delete(selectId)){
                    Helper.showMsg("done");
                    loadCustomerTable(null);
                    loadBasketCustomerCombo();
                }else{
                    Helper.showMsg("error");
                }
            }
        });


        this.table_customer.setComponentPopupMenu(this.popup_customer);

    }



    //BASKET
    private void loadBasketTable(){
        Object[] columnBasket = {"ID", "Ürün Adı", "Ürün Kodu", "Fiyat", "Stok"};
        ArrayList<Basket> baskets = this.basketController.findAll();


        //tabloyu sıfırlamak için
        DefaultTableModel clearModel = (DefaultTableModel) this.table_basket.getModel();
        clearModel.setRowCount(0);

        this.mdl_basket_table.setColumnIdentifiers(columnBasket); //modelin columnlarını atadık.
        int totalPrice = 0;

        for(Basket basket : baskets){
            Object[] rowObject = {
                    basket.getId(),
                    basket.getProduct().getName(),
                    basket.getProduct().getCode(),
                    basket.getProduct().getPrice(),
                    basket.getProduct().getStock()
            };
            this.mdl_basket_table.addRow(rowObject);

            totalPrice += basket.getProduct().getPrice();
        }


        this.lbl_basket_price.setText(totalPrice + " TL");
        this.lbl_basket_count.setText(baskets.size() + " Adet");


        this.table_basket.setModel(mdl_basket_table);
        this.table_basket.getTableHeader().setReorderingAllowed(false);
        this.table_basket.getColumnModel().getColumn(0).setMaxWidth(50);
        this.table_basket.setEnabled(false);

    }

    private void loadBasketButtonEvent(){

        this.btn_basket_reset.addActionListener(e -> {
            if(this.basketController.clear()){
                Helper.showMsg("done");
                loadBasketTable();
            } else {
                Helper.showMsg("error");
            }
        });
    }

    private void loadBasketCustomerCombo(){
        ArrayList<Customer> customers = this.customerController.findAll();
        this.cbox_basket_customer.removeAllItems();
        for(Customer customer : customers){
            int comboKey = customer.getId();
            String comboValue = customer.getName();
            this.cbox_basket_customer.addItem(new Item(comboKey,comboValue));
        }

        this.cbox_basket_customer.setSelectedItem(null);
    }

}
