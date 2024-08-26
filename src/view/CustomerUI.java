package view;

import business.CustomerController;
import core.Helper;
import entity.Customer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerUI extends JFrame {
    private JPanel container;
    private JLabel lbl_title;
    private JLabel lbl_name;
    private JTextField txt_customer_name;
    private JLabel lbl_customer_type;
    private JComboBox<Customer.TYPE> cbox_customer_type;
    private JLabel lbl_customer_phone;
    private JTextField txt_customer_phone;
    private JLabel lbl_customer_mail;
    private JTextField txt_customer_mail;
    private JLabel lbl_customer_address;
    private JTextArea tarea_customer_adress;
    private JButton btn_customer_save;
    private Customer customer;
    private CustomerController customerController;

    public CustomerUI(Customer customer){
        this.customer = customer;
        this.customerController = new CustomerController();

        this.add(container);
        this.setTitle("Müşteri Ekle/Düzenle");
        this.setSize(400,500);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        //combo box'a enumları atadık.
        this.cbox_customer_type.setModel(new DefaultComboBoxModel<>(Customer.TYPE.values()));

        if(this.customer.getId() == 0){
            this.lbl_title.setText("Müşteri Ekle");
        } else{
            this.lbl_title.setText("Müşteri Düzenle");
            this.txt_customer_name.setText(this.customer.getName());
            this.txt_customer_mail.setText(this.customer.getMail());
            this.txt_customer_phone.setText(this.customer.getPhone());
            this.tarea_customer_adress.setText(this.customer.getAddress());
            this.cbox_customer_type.getModel().setSelectedItem(this.customer.getType());


        }

        this.btn_customer_save.addActionListener(e -> {
            JTextField[] checkList = {this.txt_customer_name,this.txt_customer_phone};
            if (Helper.isTextListEmpty(checkList)) {
                Helper.showMsg("fill");
            } else if (!Helper.isTextEmpty(this.txt_customer_mail) && !Helper.isEmailValid(this.txt_customer_mail.getText())) {
                Helper.showMsg("Lütfen geçerli bir e-posta adresi giriniz!");
            } else{
                boolean result = false;
                this.customer.setName(this.txt_customer_name.getText());
                this.customer.setPhone(this.txt_customer_phone.getText());
                this.customer.setMail(this.txt_customer_mail.getText());
                this.customer.setAddress(this.tarea_customer_adress.getText());
                this.customer.setType((Customer.TYPE) this.cbox_customer_type.getSelectedItem());

                if (this.customer.getId() == 0) {
                    result = this.customerController.save(this.customer);

                } else {
                    result = this.customerController.update(this.customer);

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
