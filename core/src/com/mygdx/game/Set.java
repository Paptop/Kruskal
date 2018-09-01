package com.mygdx.game;

public class Set {
    public int size;
    private int[] parent;

    public Set(int size){
        this.size = size;
        parent = new int[size];
        for(int i = 0; i < parent.length; i++){
            parent[i] = i;
        }
    }

    public int find(int i) {
        if (parent[i] == i) {
            return i;
        } else {
            return find(parent[i]);
        }
    }

    public void union(int i, int j){
        int iRoot = find(i);
        int jRoot = find(j);
        parent[iRoot] = jRoot;
    }
}
