package view;

import business.CustomerController;
import core.Helper;
import entity.Customer;
import entity.User;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class DashboardUI extends JFrame {
    private JPanel container;
    private JLabel lblWelcome;
    private JButton btnLogout;
    private JTabbedPane tabMenu;
    private JPanel panelCustomer;
    private JTable tableCustomer;
    private JScrollPane scrollCustomer;
    private JPanel pnl_customer_filter;
    private JTextField txt_f_customer_name;
    private JComboBox cbox_f_customer_type;
    private JButton btn_customer_filter;
    private JButton btn_customer_filter_reset;
    private JButton btn_customer_new;
    private JLabel lbl_f_customer_name;
    private JLabel lbl_f_customer_type;
    private User user;
    private CustomerController customerController;

    public DashboardUI(User user){
        this.user = user;
        this.customerController = new CustomerController();
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

        loadCustomerTable(null);
    }

    private void loadCustomerTable(ArrayList<Customer> customers){
        Object[] columnCustomer = {"ID", "Müşteri Adı", "Tipi", "Telefon", "E-posta", "Adres"};

    }
}
