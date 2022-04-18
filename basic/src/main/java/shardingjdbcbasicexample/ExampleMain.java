package shardingjdbcbasicexample;

import shardingjdbcbasicexample.service.ExampleService;
import shardingjdbcbasicexample.service.impl.OrderServiceImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 **/
public class ExampleMain {
    public static void main(String[] args) throws SQLException {
        //被Sharding-JDBC代理的datasource
        DataSource dataSource=ShardingDatabaseAndTableConfiguration.getDatasource();
        ExampleService exampleService=new OrderServiceImpl(dataSource);
        exampleService.initEnvironment();
        exampleService.processSuccess();

    }
}
