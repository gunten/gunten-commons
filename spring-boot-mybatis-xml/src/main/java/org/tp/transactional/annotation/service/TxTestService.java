package org.tp.transactional.annotation.service;

public interface TxTestService {

    void callMethod() throws Exception;
    int innerMethod(int id) throws Exception;

}
