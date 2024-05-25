import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private static BreadthFirstDirectedPaths bfssource;
    private static BreadthFirstDirectedPaths bfsdistination;
    private final Digraph graph;

    // constructor takes a digraph
    public SAP(Digraph G) {
        this.graph = G;
    }

    private boolean isValid(int v) {
        if (v < 0 || v >= graph.V()) {
            return false;
        }
        return true;
    }

    private boolean isValid(Iterable<Integer> v) {
        for (int i : v) {
            if (i < 0 && i >= this.graph.V()) return false;
        }
        return true;
    }

    public int length(int v, int w) {
        if (!isValid(v) || !isValid(w)) {
            throw new IllegalArgumentException();
        }
        int ancestorVal = ancestor(v, w);
        if (ancestorVal == -1) return -1;
        return bfsdistination.distTo(ancestorVal) + bfssource.distTo(ancestorVal);
    }

    public int ancestor(int v, int w) {
        if (!isValid(v) || !isValid(w)) throw new IllegalArgumentException("Invalid vertice");
        bfssource = new BreadthFirstDirectedPaths(graph, v);
        bfsdistination = new BreadthFirstDirectedPaths(graph, w);

        int ancestorVal = -1;
        int length = Integer.MAX_VALUE;
        int candidateLength;

        for (int vertice = 0; vertice < this.graph.V(); vertice++) {
            if (bfssource.hasPathTo(vertice) && bfsdistination.hasPathTo(vertice)) {
                candidateLength = bfssource.distTo(vertice) + bfsdistination.distTo(vertice);
                if (candidateLength < length) {
                    length = candidateLength;
                    ancestorVal = vertice;
                }
            }
        }
        return ancestorVal;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null arguments are not allowed.");
        int ancstorVal = ancestor(v, w);
        if (ancstorVal == -1) return -1;
        return bfsdistination.distTo(ancstorVal) + bfssource.distTo(ancstorVal);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null arguments are not allowed.");
        if (!isValid(v) || !isValid(w)) throw new IllegalArgumentException("Invalid vertice");
        bfssource = new BreadthFirstDirectedPaths(this.graph, v);
        bfsdistination = new BreadthFirstDirectedPaths(this.graph, w);

        int ancestorVal = -1;
        int ancestorLength = Integer.MAX_VALUE;
        int candidateLength;

        for (int vertice = 0; vertice < this.graph.V(); vertice++) {
            if (bfssource.hasPathTo(vertice) && bfsdistination.hasPathTo(vertice)) {
                candidateLength = bfssource.distTo(vertice) + bfsdistination.distTo(vertice);
                if (candidateLength < ancestorLength) {
                    ancestorVal = vertice;
                    ancestorLength = candidateLength;
                }
            }
        }
        return ancestorVal;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}