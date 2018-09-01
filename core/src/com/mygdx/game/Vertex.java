package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Vertex {
    // Graphics
    public BitmapFont font;
    public float x, y;
    public int r; //Position and radius
    // Logic
    public int id;
    public static int allVertex = 0;
    public boolean isSelected = false;
    public boolean isDrag = false;
    public ArrayList<Edge> adjacencyList = new ArrayList<Edge>();


    public Vertex(BitmapFont font, int x, int y, int radius ) {
        this.x = x;
        this.y = y;
        this.r = radius;
        this.id = allVertex;
        this.font = font;
        allVertex++;

        //Gui
    }

    public Vertex(){

    }

    @Override
    public String toString(){
        return "V : " + id;
    }

    public void draw(Batch batch, ShapeRenderer shaper){
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        if(!isSelected)
            shaper.setColor(1.0f,1.0f,1.0f,1);
        else
            shaper.setColor(1.0f,0.0f,0.0f, 1);
        // Draw out circle
        shaper.circle(x,y,r+1.0f);
        shaper.setColor(0,0,1,1);
        // Draw inner circle
        shaper.circle(x,y,r);
        // Drawing edges
        for ( Edge e : adjacencyList){
           e.draw(batch,shaper);
        }
        shaper.end();

        batch.begin();
        font.draw(batch, String.valueOf(id),x - 5.0f, y + 5.0f);
        batch.end();
    }
}
