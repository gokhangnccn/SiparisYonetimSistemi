package core;

import javax.swing.*;

public class Helper {
    public static void setTheme(){
        UIManager.getInstalledLookAndFeels();
        for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
            if(info.getName().equals("Nimbus")){
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static boolean isTextEmpty(JTextField field){
        return field.getText().trim().isEmpty();
    }

    public static boolean isTextListEmpty(JTextField[] fields){
        for(JTextField field: fields){
            if(isTextEmpty(field)) return true;
        }
        return false;
    }

    public static boolean isEmailValid(String mail){
        if(mail == null || mail.trim().isEmpty()) return false;
        if(!mail.contains("@")) return false;

        String[] parts = mail.split("@");
        if(parts.length != 2) return false;
        if(parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) return false;
        if(!parts[1].contains(".")) return false;

        return true;

    }



    public static void optionPaneDialog(){
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }

    public static void showMsg(String message){
        String msg;
        String title;

        optionPaneDialog();
        switch (message) {
            case "fill" -> {
                msg = "Lütfen tüm alanları doldurunuz!";
                title = "HATA!";
            }
            case "done" -> {
                msg = "İşlem Başarılı!";
                title = "Sonuç";
            }
            case "error" -> {
                msg = "Bir Hata Oluştu!";
                title = "HATA!";
            }
            default -> {
                msg = message;
                title = "Bilgi";
            }
        };
        JOptionPane.showMessageDialog(null, msg,title,JOptionPane.INFORMATION_MESSAGE);

    }

    public static boolean confirm(String str){
        optionPaneDialog();
        String msg;

        if(str.equals("sure")){
            msg = "Bu işlemi gerçekleştirmek istediğinize emin misiniz?";
        }else{
            msg = str;
        }

        return  JOptionPane.showConfirmDialog(null,msg,"Emin misin?",JOptionPane.YES_NO_OPTION) == 0;
    }
}
