package algorithm.linkedlist;

import java.util.Stack;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/4/10
 */
public class LinkedListAlgo {

    /**
     * 判断一个链表是不是回文的，这里要求O（n）时间复杂度和O（1）的空间复杂度
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {

        //0个节点或是1个节点
        if (head == null || head.next == null) {
            return true;
        }

        ListNode prev = null;
        ListNode slow = head;
        ListNode fast = head;

        //快慢指针在跑的同时，把前半截list逆序
        //1移动fast 2临时记录slow的下个节点 3当前节点指向前一个节点
        // 4 pre对象 指向当前节点 5 slow 指向下个节点
        while (fast != null && fast.next != null){
            fast = fast.next.next;
            ListNode next = slow.next;
            slow.next = prev;
            prev = slow;
            slow = next;
        }

        if (fast != null) { //链表长度为偶数
            slow = slow.next;
        }

        while (slow != null) {
            if (slow.val != prev.val) {
                return false;
            }
            slow = slow.next;
            prev = prev.next;
        }

        return true;
    }

    //用栈实现  速度快
    public static boolean isPalindrome2(ListNode head) {

        Stack<ListNode> stack=new Stack<>();
        ListNode slow=head;
        ListNode fast=head;

        if(fast==null||fast.next==null)//0个节点或是1个节点
            return true;

        stack.push(slow);
        while(fast.next!=null&&fast.next.next!=null)
        {
            fast=fast.next.next;
            slow=slow.next;
            stack.push(slow);
        }
        if(fast.next!=null)//链表长度为偶数
            slow=slow.next;

        ListNode cur=slow;
        while(cur!=null)
        {
            if(cur.val!=stack.pop().val)
                return false;
            cur=cur.next;
        }
        return true;
    }


    public static void printList(ListNode node){
        while( node!=null){
            System.out.println("["+ node.val +"]");
            node = node.next;
        }
    }

}
