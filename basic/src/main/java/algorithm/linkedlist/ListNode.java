package algorithm.linkedlist;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2019/4/10
 */
public class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
    ListNode(int x, ListNode next){
        val = x;
        this.next = next;
    }
}
