package org.tp.transactional.annotation.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import org.tp.transactional.annotation.entity.UserEntity;
import org.tp.transactional.annotation.service.TxTestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTransactionalAnnotationTest {

    @Rule
    public TestName testName = new TestName();

    @Autowired
    private UserMapper UserMapper;

    @Autowired
    private TxTestService txTestService;

    @Before
    public void printTestName() {
        System.err.println("TestName: " + testName.getMethodName());
    }

    @Test
//    @Transactional
    @Rollback(false)
    public void testTransactionFailure() throws Exception {
        System.out.println("isAopProxy:"+ AopUtils.isAopProxy(txTestService));
        System.out.println("isCglibProxy:"+ AopUtils.isCglibProxy(txTestService));
        System.out.println("isJdkDynamicProxy:"+ AopUtils.isJdkDynamicProxy(txTestService));

        txTestService.callMethod();
    }

    @Test
    public void testInsert() throws Exception {
//        UserMapper.insert(new UserEntity("aa", "a123456", UserSexEnum.MAN));
//        UserMapper.insert(new UserEntity("bb", "b123456", UserSexEnum.WOMAN));
//        UserMapper.insert(new UserEntity("cc", "b123456", UserSexEnum.WOMAN));

        Assert.assertEquals(3, UserMapper.getAll().size());
    }

    @Test
    public void testQuery() throws Exception {
        List<UserEntity> users = UserMapper.getAll();
        if (users == null || users.size() == 0) {
            System.out.println("is null");
        } else {
            System.out.println(users.toString());
        }
    }


    @Test
    public void testUpdate() throws Exception {
        UserEntity user = UserMapper.getOne(6l);
        System.out.println(user.toString());
        user.setNickName("neo");
        UserMapper.update(user);
        Assert.assertTrue(("neo".equals(UserMapper.getOne(6l).getNickName())));
    }

}