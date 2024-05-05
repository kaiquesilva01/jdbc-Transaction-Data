package application;

import db.DB;
import db.DbException;
import db.DbIntegrityException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {

        Connection conn = null;
        Statement st = null;

        try {
            conn = DB.getConnection();

            //só concluir a mudança , se o commit tiver no final
            conn.setAutoCommit(false);

            st = conn.createStatement();

            int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2080 WHERE DepartmentId = 1");

            //forçar erro
            /*int x = 1;
            if (x < 2) {
                throw  new SQLException("Fake error");
            };*/

            int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 4090 WHERE DepartmentId = 2");

            System.out.println("rows1 " + rows1);
            System.out.println("rows1 " + rows2);

            conn.commit();
        }
        catch (SQLException e) {
            try {
                //caso haja um erro , fará a restauração
                conn.rollback();
                throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
            } catch (SQLException ex) {
                throw new DbException("Error trying to rollback! Caused by: " + ex.getMessage());
            }
        }
        finally {
            DB.closeStatement(st);
            DB.closeConnection();
        }
    }
}