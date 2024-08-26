package view;

import business.CustomerController;
import business.ProductController;
import core.Helper;
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
    private JComboBox cbox_product_stock;
    private JButton btn_product_filter;
    private JButton btn_product_filter_reset;
    private JButton btn_product_new;
    private JLabel lbl_f_product_name;
    private JLabel lbl_f_product_code;
    private User user;
    private CustomerController customerController;
    private ProductController productController;
    private DefaultTableModel mdl_customer_table = new DefaultTableModel();
    private DefaultTableModel mdl_product_table = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();
    private JPopupMenu popup_product = new JPopupMenu();

    public DashboardUI(User user){
        this.user = user;
        this.customerController = new CustomerController();
        this.productController = new ProductController();
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
    }


    private void loadProductPopupMenu() {
        this.table_product.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectRow = table_product.rowAtPoint(e.getPoint());
                table_product.setRowSelectionInterval(selectRow, selectRow);
            }
        });


        this.popup_product.add("Güncelle").addActionListener(e -> {
            int selectId = (int) table_product.getValueAt(table_product.getSelectedRow(), 0);
            ProductUI productUI = new ProductUI(this.productController.getById(selectId));
            productUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadProductTable(null);
                }
            });
        });
        popup_product.add("Sil").addActionListener(e -> {
            int selectId = (int) table_product.getValueAt(table_product.getSelectedRow(), 0);

            if (Helper.confirm("sure")) {
                if (this.productController.delete(selectId)) {
                    Helper.showMsg("done");
                    loadProductTable(null);
                } else {
                    Helper.showMsg("error");

                }
            }
        });

        this.table_product.setComponentPopupMenu(this.popup_product);
    }
    private void loadCustomerButtonEvent(){
        this.btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
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

        this.btn_customer_filter_reset.addActionListener(e -> {
            loadCustomerTable(null);
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
                }
            });
        });
        this.popup_customer.add("Sil").addActionListener(e -> {
            int selectId = (int) table_customer.getValueAt(table_customer.getSelectedRow(), 0);
            if(Helper.confirm("sure")){
                if(this.customerController.delete(selectId)){
                    Helper.showMsg("done");
                    loadCustomerTable(null);
                }else{
                    Helper.showMsg("error");
                }
            }
        });


        this.table_customer.setComponentPopupMenu(this.popup_customer);

    }

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
}
