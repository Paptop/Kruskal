package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

public class EdgeInput implements Input.TextInputListener{

    private String weightText;
    private ArrayList<Vertex> connectedVertex;
    private BitmapFont fontNum;


    public EdgeInput(String weightText, ArrayList<Vertex> convert, BitmapFont font){
        this.weightText = weightText;
        this.connectedVertex = convert;
        this.fontNum = font;

    }

    @Override
    public void input(String text) {
        weightText = text;
        int weight = Integer.parseInt(weightText);
        weightText = "";
        Edge edge = new Edge(fontNum);
        if( connectedVertex.size() == 2) {
            Vertex parent = connectedVertex.get(0);
            Vertex child = connectedVertex.get(1);
            edge.toVertex = child;
            edge.fromVertex = parent;
            edge.weight = weight;
            boolean alreadyPresent = false;
            for(Edge e : child.adjacencyList){
                if(parent.id == e.toVertex.id){
                    alreadyPresent = true;
                }
            }
            if(!alreadyPresent) {
                parent.adjacencyList.add(edge);
                System.out.println("CONNECTED : " + parent.id + " " + child.id);
            }
            connectedVertex.clear();
        }else{
            assert(false);
        }
    }

    @Override
    public void canceled() {

    }
}
