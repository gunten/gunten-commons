package org.tp.mix.redis.client;

/**
 **/
public class MainClient {
    public static void main(String[] args) {
        CustomerRedisClient customerRedisClient=new CustomerRedisClient("localhost",6379);

        System.out.println(customerRedisClient.set("customer","testValue"));
        System.out.println(customerRedisClient.get("customer"));
    }
}
