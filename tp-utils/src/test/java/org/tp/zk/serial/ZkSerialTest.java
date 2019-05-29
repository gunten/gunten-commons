package org.tp.zk.serial;

import org.junit.Test;

import java.util.stream.IntStream;

public class ZkSerialTest {


    @Test
    public  void zkSerialTest() {
        final Serial serial = new DistributedSerial("47.98.101.251:2181,47.98.101.251:2182,47.98.101.251:2183",3000);
        IntStream.rangeClosed(1, 10).forEach(n -> System.out.println(serial.getSerialNum("/project/limit/number/")));
    }
}
