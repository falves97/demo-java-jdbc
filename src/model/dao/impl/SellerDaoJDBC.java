package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SellerDaoJDBC implements SellerDao {
    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller fideById(Integer id) {
        Statement statement = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            //seleciona o banco de dados correto
            statement = conn.createStatement();
            statement.execute("USE coursejdbc");

            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?"
            );

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Department dep = instantiateDepartment(rs);
                Seller seller = instantiateSeller(rs, dep);
                return seller;
            }
            return null;
        } catch (SQLException throwables) {
            throw new DbException(throwables.getMessage());
        }
        finally {
            DB.closeResultset(rs);
            DB.closeStatement(st);
            DB.closeStatement(statement);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        Calendar cal = new Calendar.Builder().setInstant(rs.getDate("BirthDate")).build();
        seller.setBirthDate(cal);
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(dep);
        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        Statement statement = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            //seleciona o banco de dados correto
            statement = conn.createStatement();
            statement.execute("USE coursejdbc");

            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE department.Id = ? "
                    + "ORDER BY Name"
            );

            st.setInt(1, department.getId());
            rs = st.executeQuery();

            List<Seller> list = new ArrayList<>();
            Department dep = null;
            if (rs.next()) {
                 dep = instantiateDepartment(rs);
            }
            do {
                list.add(instantiateSeller(rs, dep));
            }while (rs.next());

            return list;
        } catch (SQLException throwables) {
            throw new DbException(throwables.getMessage());
        }
        finally {
            DB.closeResultset(rs);
            DB.closeStatement(st);
            DB.closeStatement(statement);
        }
    }
}
