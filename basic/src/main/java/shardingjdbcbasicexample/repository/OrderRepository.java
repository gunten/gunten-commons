package shardingjdbcbasicexample.repository;


import shardingjdbcbasicexample.entity.Order;

import java.sql.SQLException;

/**
 **/
public interface OrderRepository {

    void createTableIfNotExists() throws SQLException;

    Long insert(final Order order) throws SQLException;
}
