package algorithm.linkedlist;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/4/10
 */
public class LinkedListAlgoTest {


    @Test
    public void isPalindromeTest(){
        LinkedListAlgo algo = new LinkedListAlgo();

        //0个节点
        ListNode head = null;
        Assert.assertTrue(algo.isPalindrome(head));
        //1个节点
        head = new ListNode(1);
        Assert.assertTrue(algo.isPalindrome(head));
        //2个节点
        head.next = new ListNode(1);
        Assert.assertTrue(algo.isPalindrome(head));
        head = new ListNode(1, new ListNode(2));
        Assert.assertFalse(algo.isPalindrome(head));

        //奇数个节点
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(2,new ListNode(1)))));
        Assert.assertTrue(algo.isPalindrome(head));
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(4,new ListNode(1)))));
        Assert.assertFalse(algo.isPalindrome(head));

        //偶数个节点
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(3,new ListNode(2,
                new ListNode(1))))));
        Assert.assertTrue(algo.isPalindrome(head));
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(3,new ListNode(2,
                new ListNode(5))))));
        Assert.assertFalse(algo.isPalindrome(head));

        LinkedListAlgo.printList(head);
    }

    @Test
    public void isPalindrome2Test(){
        LinkedListAlgo algo = new LinkedListAlgo();

        //0个节点
        ListNode head = null;
        Assert.assertTrue(algo.isPalindrome2(head));
        //1个节点
        head = new ListNode(1);
        Assert.assertTrue(algo.isPalindrome2(head));
        //2个节点
        head.next = new ListNode(1);
        Assert.assertTrue(algo.isPalindrome2(head));
        head = new ListNode(1, new ListNode(2));
        Assert.assertFalse(algo.isPalindrome2(head));

        //奇数个节点
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(2,new ListNode(1)))));
        Assert.assertTrue(algo.isPalindrome2(head));
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(4,new ListNode(1)))));
        Assert.assertFalse(algo.isPalindrome2(head));

        //偶数个节点
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(3,new ListNode(2,
                new ListNode(1))))));
        Assert.assertTrue(algo.isPalindrome2(head));
        head = new ListNode(1, new ListNode(2, new ListNode(3,new ListNode(3,new ListNode(2,
                new ListNode(5))))));
        Assert.assertFalse(algo.isPalindrome2(head));

        LinkedListAlgo.printList(head);
    }
}
