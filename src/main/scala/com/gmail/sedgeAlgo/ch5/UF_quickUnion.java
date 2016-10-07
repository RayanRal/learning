package com.gmail.sedgeAlgo.ch5;

/**
 * Created by rayanral on 6/21/16.
 */
public class UF_quickUnion {

    private int[] id; //component identifiers (indexed by nodes)
    private int count;

    public UF_quickUnion(int n) {
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);

        if(rootP == rootQ) return;

        id[rootP] = rootQ;

        count--;
    }

    public int find(int p) {
        while(p != id[p]) p = id[p];
        return p;
    }

    public boolean connected(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        return rootP == rootQ;
    }

    public int count() {
        return 0;
    }

}
