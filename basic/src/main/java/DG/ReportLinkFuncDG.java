package DG;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 2024/4/28
 */
public class ReportLinkFuncDG {

    private int v; // 顶点的个数
    private LinkedList<Integer> adj[]; // 邻接表

    // 记录一次 traverse 递归经过的节点
    private boolean[] onPath;
    // 记录遍历过的节点，防止走回头路
    private boolean[] visited;
    // 记录图中是否有环
    private boolean hasCycle = false;
    // 记录后序遍历结果
    private List<Integer> postorder = new ArrayList<>();

    private Stack<Integer> trace = new Stack<>();
    private List<List<Integer>> rings = new ArrayList<>();

    public ReportLinkFuncDG(int v) {
        this.v = v;
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
        onPath = new boolean[v];
        visited = new boolean[v];
        for (int i = 0; i < v; i++) {
            // 遍历图中的所有节点
            traverseByDFS(this.adj, i);
        }
        return hasCycle;
    }

    public boolean searchAllRings() {
        onPath = new boolean[v];
        visited = new boolean[v];
        Set<Integer> set = new HashSet<>();

        for (int i = 0; i < v; i++) {
            postorder = new ArrayList<>();
            // 节点没被遍历过，即独立一副图 才重复出发
            if (!set.contains(i)) {
                traverseByDFS2(this.adj, i);
                set.addAll(postorder);
            }
        }
        return hasCycle;
    }

    /**
     * 拓扑排序
     *
     * @return
     */
    public List topologicalSortingByDFS() {
        // 进行 DFS 遍历
        onPath = new boolean[v];
        visited = new boolean[v];
        postorder = new ArrayList<>(v);

        for (int i = 0; i < v; i++) {
            traverseByDFS(this.adj, i);
        }
        // 先保证图中无环
        if (hasCycle) {
            return Collections.emptyList();
        }
        // 将后序遍历结果反转
        Collections.reverse(postorder);
        return postorder;
    }

    private void addRing (List ring) {
        List<List<Integer>> collect = rings.stream().filter(list -> list.size() == ring.size()).collect(Collectors.toList());
        if (collect.stream().noneMatch(list -> list.containsAll(ring))) {
            rings.add(ring);
        }
    }

    /**
     * 找到所有环
     *
     * @param adj
     * @param s
     */
    private void traverseByDFS2(List<Integer>[] adj, int s) {

        if (onPath[s]) {
            // 出现环, 路径回退到onPath节点即为环链路
            hasCycle = true;
            Integer temp;
            int i = trace.size() - 1;
            List ring = new ArrayList();
            do {
                temp = trace.get(i--);
                ring.add(temp);
            } while (temp != s && i >= 0);
            Collections.reverse(ring);
            addRing(ring);

            return;
        }

        // 虽然访问过，但只要当前路径不存在就继续遍历
        if (!trace.contains(s) && visited[s]) {
            visited[s] = false;
        }
        if (visited[s]) {
            return;
        }

        // 前序遍历代码位置
        visited[s] = true;
        onPath[s] = true;
        trace.add(s);
        for (int t : adj[s]) {
            traverseByDFS2(adj, t);
        }
        // 后序遍历代码位置
        postorder.add(s);
        onPath[s] = false;
        trace.pop();
    }

    /**
     * 图遍历框架 找到首个环
     *
     * @param adj
     * @param s
     */
    private void traverseByDFS(List<Integer>[] adj, int s) {
        if (onPath[s]) {
            // 出现环, 路径回退到onPath节点即为环链路
            hasCycle = true;
            Integer temp;
            List ring = new ArrayList();
            int i = trace.size() - 1;
            do {
                temp = trace.get(i--);
                ring.add(temp);
            } while (temp != s && i >= 0);
            Collections.reverse(ring);
            rings.add(ring);
        }

        if (visited[s] || hasCycle) {
            // 如果已经找到了环，也不用再遍历了
            return;
        }
        // 前序遍历代码位置
        visited[s] = true;
        onPath[s] = true;
        trace.add(s);
        for (int t : adj[s]) {
            traverseByDFS(adj, t);
        }
        // 后序遍历代码位置
        postorder.add(s);
        onPath[s] = false;
        trace.pop();
    }

    public static void main(String[] args) {
        ReportLinkFuncDG ringGraph1 = ReportLinkFuncDG.createRingGraph1();
        ringGraph1.print();
        if (ringGraph1.searchAllRings()) {
            ringGraph1.rings.forEach(t -> System.out.print(t + "->"));
            System.out.println();
        }
        System.out.println("-------------------------------");

        ReportLinkFuncDG ringGraph2 = ReportLinkFuncDG.createRingGraph2();
        ringGraph2.print();
        if (ringGraph2.searchAllRings()) {
            ringGraph2.rings.forEach(t -> System.out.print(t + "->"));
            System.out.println();
        }
        System.out.println("-------------------------------");


        ReportLinkFuncDG ringGraph3 = ReportLinkFuncDG.createRingGraph3();
        ringGraph3.print();
        if (ringGraph3.searchAllRings()) {
            ringGraph3.rings.forEach(t -> System.out.print(t + "->"));
            System.out.println();
        }
        System.out.println("-------------------------------");

        ReportLinkFuncDG ringGraph4 = ReportLinkFuncDG.createRingGraph4();
        ringGraph4.print();
        if (ringGraph4.searchAllRings()) {
            ringGraph4.rings.forEach(t -> System.out.print(t + "->"));
            System.out.println();
        }
        System.out.println("-------------------------------");

        ReportLinkFuncDG dag1 = ReportLinkFuncDG.createDAG1();
        dag1.print();
        if (dag1.searchAllRings()) {
            dag1.rings.forEach(t -> System.out.print(t + "->"));
            System.out.println();
        } else {
            dag1.topologicalSortingByDFS().forEach(t -> System.out.print(t + "-"));
        }
        System.out.println("-------------------------------");
    }

    public static ReportLinkFuncDG createRingGraph1() {
        ReportLinkFuncDG dg = new ReportLinkFuncDG(7);
        dg.addEdge(0, 1);
        dg.addEdge(0, 2);
        dg.addEdge(1, 3);
        dg.addEdge(2, 5);
        dg.addEdge(3, 4);
        dg.addEdge(4, 2);
        dg.addEdge(5, 4);
        dg.addEdge(5, 6);
        dg.addEdge(6, 2);
        dg.addEdge(6, 0);
        return dg;
    }

    public static ReportLinkFuncDG createRingGraph2() {
        ReportLinkFuncDG dg = new ReportLinkFuncDG(7);
        dg.addEdge(0, 1);
        dg.addEdge(0, 5);
        dg.addEdge(1, 2);
        dg.addEdge(2, 3);
        dg.addEdge(2, 4);
        dg.addEdge(3, 0);
        dg.addEdge(5, 6);
        dg.addEdge(6, 0);
        return dg;
    }

    public static ReportLinkFuncDG createRingGraph3() {
        ReportLinkFuncDG dg = new ReportLinkFuncDG(10);
        dg.addEdge(0, 4);
        dg.addEdge(4, 5);
        dg.addEdge(5, 9);
        dg.addEdge(9, 8);
        dg.addEdge(8, 7);
        dg.addEdge(7, 6);
        dg.addEdge(6, 5);
        return dg;
    }

    public static ReportLinkFuncDG createRingGraph4() {
        ReportLinkFuncDG dg = new ReportLinkFuncDG(5);
        dg.addEdge(0, 1);
        dg.addEdge(1, 2);
        dg.addEdge(2, 3);
        dg.addEdge(3, 4);
        dg.addEdge(2, 4);
        dg.addEdge(4, 0);
        return dg;
    }

    public static ReportLinkFuncDG createDAG1() {
        ReportLinkFuncDG ring1 = new ReportLinkFuncDG(5);
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
