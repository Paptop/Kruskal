package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Edge implements Comparable<Edge> {
    public Vertex toVertex;
    public Vertex fromVertex;
    public int weight;
    public BitmapFont font;
    public boolean isSpanTree = false;

    public Edge(BitmapFont font){
        this.font = font;
    }

    @Override
    public boolean equals(Object o) {
        if ( o == this){
            return true;
        }
        if( !(o instanceof Edge)){
            return false;
        }
        Edge e = (Edge) o;
        return Integer.compare(weight,e.weight) == 0;
    }
    @Override
    public int compareTo(Edge o) {
        return weight - o.weight;
    }

    @Override
    public String toString(){
        return "From" + fromVertex.toString() + " W :" + weight +  " To" + toVertex.toString() + "\n";
    }


    public void draw(Batch batch, ShapeRenderer shaper){
        if(!isSpanTree) {
            shaper.setColor(0.0f, 1.0f, 0.0f, 1);
            shaper.rectLine(fromVertex.x, fromVertex.y, toVertex.x, toVertex.y, 2);
        }else{

            shaper.setColor(1.0f, 0.0f, 0.0f, 1);
            shaper.rectLine(fromVertex.x, fromVertex.y, toVertex.x, toVertex.y, 2);
        }

        batch.begin();
        font.draw(batch, String.valueOf(weight), (fromVertex.x + toVertex.x) / 2, (fromVertex.y + toVertex.y) / 2);
        batch.end();

    }
}
