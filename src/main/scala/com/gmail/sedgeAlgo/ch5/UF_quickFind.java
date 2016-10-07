package com.gmail.sedgeAlgo.ch5;

/**
 * Created by rayanral on 6/21/16.
 */
public class UF_quickFind {

    private int[] id; //component identifiers (indexed by nodes)
    private int count;

    public UF_quickFind(int n) {
        count = n;
        id = new int[n];
        for(int i = 0; i < n;  i++) id[i] = i;
    }

    public void union(int p, int q) {
        int newNode = find(p);
        int nodeToMerge = find(q);

        if(newNode == nodeToMerge) return;

        for(int i = 0; i < id.length; i ++) {
            if(id[i] == nodeToMerge) id[i] = newNode;
        }
        count--;
    }

    public int find(int p) {
        return id[p];
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public int count() {
        return count;
    }



}
