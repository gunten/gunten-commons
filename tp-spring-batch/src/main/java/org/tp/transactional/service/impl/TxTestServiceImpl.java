package org.tp.transactional.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.tp.transactional.entity.UserEntity;
import org.tp.transactional.mapper.UserMapper;
import org.tp.transactional.service.TxTestService;


@Service
@Slf4j
public class TxTestServiceImpl implements TxTestService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void callMethod() {
        for (int i = 1; i <= 3; i++) {
            userMapper.insert(new UserEntity((long) i, null,null,null));
        }

        int innerRet = 0;
        for (int i = 4; i <= 6; i++) {
            System.out.println("isAopProxy:"+ AopUtils.isAopProxy(this));
            System.out.println("isCglibProxy:"+ AopUtils.isCglibProxy(this));
            System.out.println("isJdkDynamicProxy:"+ AopUtils.isJdkDynamicProxy(this));

            if (AopUtils.isCglibProxy(AopContext.currentProxy())) {
                System.out.println("cglibProxy");
            }
            if (AopUtils.isJdkDynamicProxy(AopContext.currentProxy())) {
                System.out.println("JdkDynamicProxy");
            }
            try {

                if (AopUtils.isAopProxy(AopContext.currentProxy())) {
                    innerRet = ((TxTestService) AopContext.currentProxy()).innerMethod(i);
                } else
                    innerRet = this.innerMethod(i);
            } catch (Exception e) {

            }
            System.out.println("innerRet = " + innerRet);

        }
        System.out.println("callMethod End !!!!!!!!");
        //测试NEST 和 REQUIRES_NEW 的区别
//        int i = 2 /0;
    }

    @Override
//    @Transactional(propagation  = Propagation.REQUIRES_NEW)
    @Transactional(propagation  = Propagation.NESTED)
    public int innerMethod(int id) throws Exception {
        try {
            if( id == 5) // double insert
                userMapper.insert(new UserEntity((long) id, null,null,null));
            return userMapper.insert(new UserEntity((long) id, null,null,null));
        } catch (Exception e) {
            log.error("innerMethod err : {}" , e.getMessage());
//            尝试注释产生的效果
//            throw new Exception("haha"); //不回滚 ,需要指定rollbackFor
//            throw new RuntimeException("my exception");//回滚
            //既能有返回值 又能回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }

    }
}
