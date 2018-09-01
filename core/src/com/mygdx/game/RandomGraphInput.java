package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomGraphInput implements Input.TextInputListener {

    private String del = "[-. ;,]";
    private BitmapFont fontNum;
    private BitmapFont fontVer;
    private ArrayList<Vertex> guiGraph;
    private Random rand;
    // Number of edges
    public int m;
    // Number of vertices
    public int n;

    public Edge createEdge(Vertex fromVertex, Vertex toVertex, int weight){
        Edge edge = new Edge(fontNum);
        edge.fromVertex = fromVertex;
        edge.toVertex = toVertex;
        edge.weight = weight;
        return edge;
    }

    public ArrayList<Integer> initGraphIndexes(ArrayList<Integer> index, int vertex){
        for(int j = 0; j < guiGraph.size(); j++){
            if( vertex == j)
                continue;
            index.add(j);
        }
        Collections.shuffle(index);
        return index;
    }

    public void generateRandomGraph(int size, int edges){
        for(int i = 0; i < size; i++){
            int x = MathUtils.random(5, Gdx.graphics.getWidth() - 10);
            int y = MathUtils.random(5, Gdx.graphics.getHeight() - 10);
            guiGraph.add(new Vertex(fontVer,x,y,20));
        }
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        float p = MathUtils.random(1f);
        int edgesCount = 0;
        for(Vertex v : guiGraph){
            initGraphIndexes(indexes,v.id);
            for(Integer i : indexes){
                Vertex ver = guiGraph.get(i);
                boolean conflict = false;
                for(Edge e : ver.adjacencyList){
                    if(e.toVertex.id  == v.id){
                       conflict = true;
                    }
                }

                if(!conflict) {
                    double g = Math.random();
                    if (g < p && edgesCount < edges ) {
                        Edge edge = createEdge(v,ver,MathUtils.random(0,1000));
                        v.adjacencyList.add(edge);
                        edgesCount++;
                    }
                }else{
                    continue;
                }
            }
            indexes.clear();
        }
    }

    public RandomGraphInput(BitmapFont fontVer,BitmapFont font, ArrayList<Vertex> guiGraph){
       this.fontNum = font;
       this.fontVer = fontVer;
       this.guiGraph = guiGraph;
       this.rand = new Random();
    }

    @Override
    public void input(String text) {
        String[] tokens = text.split(del, 2);
     //   if(tokens.length < 2 || tokens.length > 2){
      //      System.out.println("Expecting 2 numbers N and M");
       //     return;
       // }
        int n = Integer.parseInt(tokens[0]);
       // int m = Integer.parseInt(tokens[1]);
      // if( m <= n * (n - 1) && n > 0 && m > 0 ){
            this.m = m;
            this.n = n;
            generateRandomGraph(n,(n * (n-1)) / 2);
       // }else{
        //    System.out.println("Expecting M to be less than N * (N - 1)");
         //   return;
       // }

    }

    @Override
    public void canceled() {

    }
}
