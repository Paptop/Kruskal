package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

import java.io.*;
import java.util.*;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor , Input.TextInputListener {
    // For drawing numbers
    private BitmapFont fontNum;
    private BitmapFont fontVer;

	private SpriteBatch batch;
	private ShapeRenderer sr;
	private String weightText = "";

	private ArrayList<Vertex> guiGraph = new ArrayList<Vertex>();
	private ArrayList<Vertex> connectedVertex = new ArrayList<Vertex>();
	private Vertex selectedVertex;
    private Vertex dragVertex;

    // Input
    private EdgeInput edgeInput;
    private RandomGraphInput graphInput;
    private int allEdges = 0;

    // Output
    private PrintWriter writer;

    public void dumpInfoToFile(int size, int edge, long time){
        String line =  edge + " " + time;
        writer.println(line);

    }

    public PriorityQueue<Edge> createQueue(ArrayList<Vertex> graph){
        Comparator mycomp = new EdgeComparator();
        PriorityQueue<Edge> queue = new PriorityQueue<Edge>(mycomp);
        for(Vertex v : graph) {
            for(Edge e : v.adjacencyList) {
                queue.add(e);
            }
        }
        return queue;
    }

    public void KruskalAlgorithmWithDisJointSet(ArrayList<Vertex> graph){
        long startTime = System.currentTimeMillis();
        PriorityQueue<Edge> pq = createQueue(graph);
        SetOptimized set = new SetOptimized(graph.size());
        ArrayList<Edge> minspantree = new ArrayList<>();
        while(pq.size() != 0 && minspantree.size() != graph.size() - 1){
            Edge e = pq.poll();
            int vSet = set.find(e.fromVertex.id);
            int wSet = set.find(e.toVertex.id);
            if(vSet != wSet){
               e.isSpanTree = true;
               minspantree.add(e);
               set.union(vSet,wSet);
            }
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = (endTime - startTime);
        System.out.println(minspantree.toString());
        System.out.println("Time taken : " + elapsedTime);
        dumpInfoToFile(graph.size(),allEdges,elapsedTime);

    }

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
        this.allEdges = edges;
        for(int i = 0; i < size; i++){
            int x = MathUtils.random(5, Gdx.graphics.getWidth() - 10);
            int y = MathUtils.random(5, Gdx.graphics.getHeight() - 10);
           guiGraph.add(new Vertex(fontVer,x,y,20));
        }
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        int edgesCounter = 0;
        for(Vertex v : guiGraph){
            initGraphIndexes(indexes,v.id);
            while(indexes.size() != 0 && edgesCounter < edges) {
                int index = indexes.remove(0);
                Vertex toVertex = guiGraph.get(index);
                if (toVertex.id == v.id) continue;
                Edge edge = createEdge(v,toVertex,MathUtils.random(0,50));
                boolean alreadyPresent = false;
                for (Edge e : toVertex.adjacencyList) {
                    if (v.id == e.toVertex.id) {
                        alreadyPresent = true;
                    }
                }
                if (!alreadyPresent) {
                    v.adjacencyList.add(edge);
                   edgesCounter++;
                }
            }
            indexes.clear();
        }
    }

	public Vertex getSelectedVertex(float worldX, float worldY){
	    for(Vertex v : guiGraph){
	        float valX = v.x - worldX;
	        float valY = v.y - worldY;
	        float valX2 = (float)Math.pow(valX,2.0);
	        float valY2 = (float)Math.pow(valY,2.0);
	        if( valX2 + valY2 < v.r * v.r)
	           return v;
        }
        return null;
    }


    public void initPrinter(){
        try {
            writer = new PrintWriter(new FileOutputStream(new File("output"),true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		fontNum = new BitmapFont();
		fontNum.getData().scale(1.5f);
		fontVer = new BitmapFont();
        edgeInput = new EdgeInput(weightText,connectedVertex,fontNum);
        graphInput = new RandomGraphInput(fontVer,fontNum,guiGraph);
        initPrinter();
		Gdx.input.setInputProcessor(this);
	}

	public void tick(){ }

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.0f, 0, 0.0f, 0.25f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for( Vertex v : guiGraph)
		    v.draw(batch,sr);
		if(dragVertex != null) {
            dragVertex.x = Gdx.input.getX();
            dragVertex.y = Gdx.graphics.getHeight() - Gdx.input.getY();
        }


	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sr.dispose();
        writer.close();
	}

	// GUI

    public void testAlgo(){
        writer.println("Size  V from 0 to  : " + 1000);
        for(int i = 0; i <= 10000; i++) {
            refresh();
            allEdges = i * (i - 1) / 2;
            graphInput.generateRandomGraph(i, allEdges);
            KruskalAlgorithmWithDisJointSet(guiGraph);
        }
    }

    public void refresh(){
       guiGraph.clear();
       Vertex.allVertex = 0;
    }

    @Override
    public boolean keyDown(int keycode) {
	    if( keycode == Input.Keys.ENTER){
	        KruskalAlgorithmWithDisJointSet(this.guiGraph);
        }
        if( keycode == Input.Keys.R){
	        guiGraph.clear();
	        Vertex.allVertex = 0;
	        String text = "";
	        Gdx.input.getTextInput(graphInput, "Enter N and M", text, "Enter number of edges and vertices");
        }
        if (keycode == Input.Keys.T){
	        testAlgo();

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public void connectingTwoVertex(Vertex v ){
        v.isSelected = true;
        Gdx.input.getTextInput(edgeInput,"Enter weight",weightText,"Enter integer number");
        selectedVertex.isSelected = false;
        v.isSelected = false;
        selectedVertex = null;
        connectedVertex.add(v);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	    if(button == Input.Buttons.LEFT){
	        guiGraph.add(new Vertex(fontVer,screenX, Gdx.graphics.getHeight() - screenY,20));
	        return true;
        }
        if(button == Input.Buttons.RIGHT){
	        Vertex v = getSelectedVertex(screenX, Gdx.graphics.getHeight() - screenY);
	        if(v != null){
	            // if user selected 2 vertex
                if(selectedVertex != null){
                    connectingTwoVertex(v);
                    return true;
                }
                v.isSelected = true;
                selectedVertex = v;
                // Storing two vertices
                connectedVertex.add(selectedVertex);
            }
        }
        if(button == Input.Buttons.MIDDLE){
	       Vertex v = getSelectedVertex(screenX, Gdx.graphics.getHeight() - screenY);
	       // if it was touched for the second time
	       if(dragVertex != null) {
               if (v.id == dragVertex.id) {
                   v.isDrag = false;
                   dragVertex = null;
                   return true;
               }
           }
           // First time
	       if ( v != null){
	           v.isDrag = true;
	           dragVertex = v;
           }
        }
        return false;


    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void input(String text) {
/*
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
                connectedVertex.clear();
            }
        }else{
	        assert(false);
        }
        */
    }

    @Override
    public void canceled() {

    }
}
