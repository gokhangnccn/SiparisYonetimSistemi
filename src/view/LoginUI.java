package view;

import business.UserController;
import core.Helper;
import entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private JPanel container;
    private JPanel panelTop;
    private JLabel lblTitle;
    private JPanel panelBottom;
    private JTextField txtKullanici;
    private JButton btnGiris;
    private JLabel lblKullanici;
    private JLabel lblSifre;
    private JPasswordField txtSifre;
    private UserController userController;

    public LoginUI(){
        this.userController = new UserController();

        this.add(container);
        this.setTitle("Customer Managment System");
        this.setSize(600,600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        btnGiris.addActionListener(e -> {
            JTextField[] checkList = {this.txtKullanici,this.txtSifre};
            if(!Helper.isEmailValid(this.txtKullanici.getText())){
                Helper.showMsg("Geçerli bir e-posta giriniz!");
            } else if(Helper.isTextListEmpty(checkList))
                Helper.showMsg("fill");
            else{
                User user = this.userController.findByLogin(this.txtKullanici.getText(),this.txtSifre.getText());
                if(user == null){
                    Helper.showMsg("Girdiğiniz bilgilere göre kullanıcı bulunamadı!");
                } else{
                    this.dispose();
                    DashboardUI dashboardUI = new DashboardUI(user);

                }
            }

        });
    }







}
