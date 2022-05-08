package org.tp.mix.redis.client;

/**
 **/
public class MainClient {
    public static void main(String[] args) {
        CustomerRedisClient customerRedisClient=new CustomerRedisClient("192.168.33.10",6379);

        System.out.println(customerRedisClient.set("customer","testValue"));
        System.out.println(customerRedisClient.get("customer"));
    }
}
