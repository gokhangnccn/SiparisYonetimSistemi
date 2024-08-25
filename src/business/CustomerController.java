package business;

import core.Helper;
import dao.CustomerDao;
import dao.UserDao;
import entity.Customer;
import entity.User;

import java.util.ArrayList;

public class CustomerController {

    private final CustomerDao customerDao = new CustomerDao();

    public ArrayList<Customer> findAll(){
        return this.customerDao.findAll();
    }
}
