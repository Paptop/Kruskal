package com.mygdx.game;

public class SetOptimized {
    public int size;
    private int[] parent;
    private int[] rank;

    public SetOptimized(int size){
        this.size = size;
        parent = new int[size];
        rank = new int[size];
        for(int i = 0; i < size; i++){
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int i) {
        if (parent[i] == i) {
            return i;
        } else {
            int result = find(parent[i]);
            parent[i] = result;
            return result;
        }
    }

    public void union(int i, int j){
        int iRoot = find(i);
        int jRoot = find(j);
        int iRank = rank[iRoot];
        int jRank = rank[jRoot];

        if (iRoot == jRoot)
            return;

        if (iRank < jRank){
            parent[iRoot] = jRoot;
        }
        else if (jRank < iRank){
            parent[jRoot] = iRoot;
        }
        else{
            parent[iRoot] = jRoot;
            rank[jRoot]++;
        }
    }
}
