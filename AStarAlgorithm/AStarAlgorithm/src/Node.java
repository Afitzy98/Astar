import java.util.*;

public class Node {
    Node parent;
    int f;
    int g;
    int h;
    int x;
    int y;
    boolean path = false;
  
    public Node(int x, int y) {
    	this.x = x;
    	this.y = y;
    	
    }
    
    public void getFscore(int targx, int targy) {
    	h = (Math.abs(x-targx) + Math.abs(y-targy)) * 10;
    	f = g+h;
    }
}