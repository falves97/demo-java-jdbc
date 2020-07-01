package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Calendar;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("====== Test 1: findById ======");
        Seller seller = sellerDao.fideById(3);
        System.out.println(seller);

        System.out.println("\n====== Test 1: findByDepartment ======");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);

        list.forEach(System.out::println);
    }
}
