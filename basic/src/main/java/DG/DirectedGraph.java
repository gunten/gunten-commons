package DG;

import java.util.LinkedList;
import java.util.List;

/**
 * 2024/4/28
 */
public class DirectedGraph {

    private int v; // 顶点的个数
    private LinkedList<Integer> adj[]; // 邻接表

    // 记录一次 traverse 递归经过的节点
    private boolean[] onPath;
    // 记录遍历过的节点，防止走回头路
    private boolean[] visited;
    // 记录图中是否有环
    private boolean hasCycle = false;

    public DirectedGraph(int v) {
        this.v = v;
        onPath = new boolean[v];
        visited = new boolean[v];
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i) {
            adj[i] = new LinkedList<>();
        }
    }

    public void addEdge(int s, int t) { // s 先于 t，边 s->t
        adj[s].add(t);
    }

    public void print() {
        int i = 0;
        for (LinkedList<Integer> vertexs : adj) {
            System.out.print(i++ + ": ");
            vertexs.forEach(vertex -> System.out.print(vertex + " "));
            System.out.println();
        }
    }


    public boolean hasRing() {
        for (int i = 0; i < v; i++) {
            // 遍历图中的所有节点
            traverse(this.adj, i);
        }
        return hasCycle;
    }

    /* 图遍历框架 */
    private void traverse(List<Integer>[] adj, int s) {
        if (onPath[s]) {
            // 出现环
            hasCycle = true;
        }

        if (visited[s] || hasCycle) {
            // 如果已经找到了环，也不用再遍历了
            return;
        }
        // 前序遍历代码位置
        visited[s] = true;
        onPath[s] = true;
        for (int t : adj[s]) {
            traverse(adj, t);
        }
        // 后序遍历代码位置
        onPath[s] = false;
    }

    public static void main(String[] args) {
        DirectedGraph ringGraph1 = DirectedGraph.createRingGraph1();
//        ringGraph1.print();
        System.out.println("has ring :" + ringGraph1.hasRing());
        System.out.println("-------------------------------");

        DirectedGraph ringGraph2 = DirectedGraph.createRingGraph2();
//        ringGraph2.print();
        System.out.println("has ring :" + ringGraph2.hasRing());
        System.out.println("-------------------------------");

        DirectedGraph dag1 = DirectedGraph.createDAG1();
//        dag1.print();
        System.out.println("has ring :" + dag1.hasRing());
        System.out.println("-------------------------------");
    }

    public static DirectedGraph createRingGraph1() {
        DirectedGraph ring1 = new DirectedGraph(7);
        ring1.addEdge(0, 1);
        ring1.addEdge(0, 2);
        ring1.addEdge(1, 3);
        ring1.addEdge(2, 5);
        ring1.addEdge(3, 4);
        ring1.addEdge(4, 2);
        ring1.addEdge(5, 4);
        ring1.addEdge(5, 6);
        ring1.addEdge(6, 2);
        ring1.addEdge(6, 0);
        return ring1;
    }

    public static DirectedGraph createRingGraph2() {
        DirectedGraph ring2 = new DirectedGraph(7);
        ring2.addEdge(0, 1);
        ring2.addEdge(0, 5);
        ring2.addEdge(1, 2);
        ring2.addEdge(2, 3);
        ring2.addEdge(2, 4);
        ring2.addEdge(3, 0);
        ring2.addEdge(5, 6);
        ring2.addEdge(6, 0);
        return ring2;
    }

    public static DirectedGraph createDAG1() {
        DirectedGraph ring1 = new DirectedGraph(5);
        ring1.addEdge(0, 1);
        ring1.addEdge(0, 3);
        ring1.addEdge(0, 4);
        ring1.addEdge(1, 4);
        ring1.addEdge(1, 2);
        ring1.addEdge(1, 3);
        ring1.addEdge(2, 3);
        ring1.addEdge(3, 4);
        return ring1;
    }
}
