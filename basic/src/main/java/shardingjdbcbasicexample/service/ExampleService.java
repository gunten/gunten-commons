package shardingjdbcbasicexample.service;

import java.sql.SQLException;

/**
 **/
public interface ExampleService {
    /**
     * 初始化表结构
     *
     * @throws SQLException SQL exception
     */
    void initEnvironment() throws SQLException;

    /**
     * 执行成功
     *
     * @throws SQLException SQL exception
     */
    void processSuccess() throws SQLException;
}
