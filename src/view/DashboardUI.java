package view;

import business.CustomerController;
import core.Helper;
import entity.Customer;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
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
    private DefaultTableModel mdl_customer_table = new DefaultTableModel();
    private JPopupMenu popup_customer = new JPopupMenu();

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
        loadCustomerPopupMenu();
        loadCustomerButtonEvent();

    }

    private void loadCustomerButtonEvent(){
        btn_customer_new.addActionListener(e -> {
            CustomerUI customerUI = new CustomerUI(new Customer());
            customerUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCustomerTable(null);
                }
            });


        });
    }

    private void loadCustomerPopupMenu(){
        this.tableCustomer.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selectedRow = tableCustomer.rowAtPoint(e.getPoint());
                tableCustomer.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        this.popup_customer.add("Güncelle").addActionListener(e -> {
            int selectId = (int) tableCustomer.getValueAt(tableCustomer.getSelectedRow(), 0);
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
            int selectId = (int) tableCustomer.getValueAt(tableCustomer.getSelectedRow(), 0);
            if(Helper.confirm("sure")){
                if(this.customerController.delete(selectId)){
                    Helper.showMsg("done");
                    loadCustomerTable(null);
                }else{
                    Helper.showMsg("error");
                }
            }
        });


        this.tableCustomer.setComponentPopupMenu(this.popup_customer);

    }

    private void loadCustomerTable(ArrayList<Customer> customers){
        Object[] columnCustomer = {"ID", "Müşteri Adı", "Tipi", "Telefon", "E-posta", "Adres"};

        if(customers == null){
            customers = this.customerController.findAll();
        }

        //tabloyu sıfırlamak için
        DefaultTableModel clearModel = (DefaultTableModel) this.tableCustomer.getModel();
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


        this.tableCustomer.setModel(mdl_customer_table);
        this.tableCustomer.getTableHeader().setReorderingAllowed(false);
        this.tableCustomer.getColumnModel().getColumn(0).setMaxWidth(50);
        this.tableCustomer.setEnabled(false);

    }

}
