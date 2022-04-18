package shardingjdbcbasicexample.service.impl;


import shardingjdbcbasicexample.entity.Order;
import shardingjdbcbasicexample.repository.OrderRepository;
import shardingjdbcbasicexample.repository.impl.OrderRepositoryImpl;
import shardingjdbcbasicexample.service.ExampleService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 **/
public class OrderServiceImpl implements ExampleService {
    private final OrderRepository orderRepository;
    Random random=new Random();

    public OrderServiceImpl(final DataSource dataSource) {
        orderRepository=new OrderRepositoryImpl(dataSource);
    }

    @Override
    public void initEnvironment() throws SQLException {
        orderRepository.createTableIfNotExists();
    }

    @Override
    public void processSuccess() throws SQLException {
        System.out.println("-------------- Process Success Begin ---------------");
        List<Long> orderIds = insertData();
        System.out.println("-------------- Process Success Finish --------------");
    }
    private List<Long> insertData() throws SQLException {
        System.out.println("---------------------------- Insert Data ----------------------------");
        List<Long> result = new ArrayList<>(10);
        for (int i = 1; i <= 10; i++) {
            Order order = insertOrder(i);
            result.add(order.getOrderId());
        }
        return result;
    }
    private Order insertOrder(final int i) throws SQLException {
        Order order = new Order();
        order.setUserId(random.nextInt(10000));
        order.setAddressId(i);
        order.setStatus("INSERT_TEST");
        orderRepository.insert(order);
        return order;
    }
}
