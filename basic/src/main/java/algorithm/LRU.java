package algorithm;

import lombok.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;
@Data
public class LRU {

    @Data
    static
    class Node implements Comparable<Node>{
        private String key;
        private Date lastTime;

        public Node(String key, Date date) {
            this.key = key;
            this.lastTime = date;
        }

        @Override
        public int compareTo(Node o) {
            return this.lastTime.compareTo(o.lastTime);
        }
    }

    private TreeMap<Node,String> nodes = new TreeMap<>();

    public void add(Node node){
        if(nodes == null){
            nodes = new TreeMap<>();
        }
        nodes.put(node, node.key);
    }

    public Node getLruNode(){
        if(nodes.size() == 0 )
            return null;
        else
            return nodes.firstEntry().getKey();
    }

    public static void main(String[] args) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Node node1 = new Node("one",new Date());
        Node node2 = new Node("two",dateFormat.parse("2022-01-06"));
        Node node3 = new Node("three",dateFormat.parse("2023-01-06"));


        LRU lru = new LRU();
        lru.add(node1);
        lru.add(node2);
        lru.add(node3);

        System.out.println(lru.getLruNode());
    }


}
