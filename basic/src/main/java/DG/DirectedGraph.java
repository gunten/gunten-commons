package DG;

import lombok.Data;

import java.util.LinkedList;

/**
 * 2024/4/28
 */
public class DirectedGraph {

    // graph[x] 存储 x 的所有邻居节点
//    private List<Integer>[] graph;

    private int v; // 顶点的个数
    private LinkedList<Integer> adj[]; // 邻接表

    public DirectedGraph(int v) {
        this.v = v;
        adj = new LinkedList[v];
        for (int i=0; i<v; ++i) {
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

    // 记录被遍历过的节点
    private boolean[] visited;
    // 记录从起点到当前节点的路径
    private boolean[] onPath;

    /* 图遍历框架 */
    void traverse(DirectedGraph graph, int s) {
        if (visited[s]) return;
        // 经过节点 s，标记为已遍历
        visited[s] = true;
        // 做选择：标记节点 s 在路径上
        onPath[s] = true;
        for (int neighbor : graph.adj[s]) {
            traverse(graph, neighbor);
        }
        // 撤销选择：节点 s 离开路径
        onPath[s] = false;
    }

    public static void main(String[] args) {
        DirectedGraph ringGraph1 = DirectedGraph.createRingGraph1();
        ringGraph1.print();
        System.out.println("-------------------------------");

        DirectedGraph ringGraph2 = DirectedGraph.createRingGraph2();
        ringGraph2.print();
        System.out.println("-------------------------------");

        DirectedGraph dag1 = DirectedGraph.createDAG1();
        dag1.print();
        System.out.println("-------------------------------");
    }

    public static DirectedGraph createRingGraph1() {
        DirectedGraph ring1 = new DirectedGraph(7);
        ring1.addEdge(0,1);
        ring1.addEdge(0,2);
        ring1.addEdge(1,3);
        ring1.addEdge(2,5);
        ring1.addEdge(3,4);
        ring1.addEdge(4,2);
        ring1.addEdge(5,4);
        ring1.addEdge(5,6);
        ring1.addEdge(6,2);
        ring1.addEdge(6,0);
        return ring1;
    }

    public static DirectedGraph createRingGraph2() {
        DirectedGraph ring2 = new DirectedGraph(7);
        ring2.addEdge(0,1);
        ring2.addEdge(0,5);
        ring2.addEdge(1,2);
        ring2.addEdge(2,3);
        ring2.addEdge(2,4);
        ring2.addEdge(3,0);
        ring2.addEdge(5,6);
        ring2.addEdge(6,0);
        return ring2;
    }

    public static DirectedGraph createDAG1() {
        DirectedGraph ring1 = new DirectedGraph(5);
        ring1.addEdge(0,1);
        ring1.addEdge(0,3);
        ring1.addEdge(0,4);
        ring1.addEdge(1,4);
        ring1.addEdge(1,2);
        ring1.addEdge(1,3);
        ring1.addEdge(2,3);
        ring1.addEdge(3,4);
        return ring1;
    }
}
