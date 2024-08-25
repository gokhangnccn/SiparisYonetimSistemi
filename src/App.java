import business.UserController;
import core.Helper;
import entity.User;
import view.DashboardUI;

public class App {
    public static void main(String[] args) {
        //LoginUI loginUI = new LoginUI();
        Helper.setTheme();

       UserController userController = new UserController();
        User user = userController.findByLogin("ahmet@sys.com", "ahmet123");
        DashboardUI dashboardUI = new DashboardUI(user);


    }
}
