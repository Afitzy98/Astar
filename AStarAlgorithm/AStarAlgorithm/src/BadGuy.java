import java.awt.*;
import java.util.*;

public class BadGuy {
	
	int frames = 0;
	Image myImage;
	int x=0,y=0;
	boolean hasPath=false;
	Node[][] nodes = new Node[40][40];
	int orthoganal = 10;
	int diagonal = 14;
	Node target = null;
	Stack<Node> path;
	boolean[][] pathMap = new boolean[40][40];
	
	
	public BadGuy( Image i ) {
		myImage=i;
		x = 30;
		y = 10;
		for(int j = 0; j < 40; j++) {
			for(int k = 0; k < 40; k++) {
				nodes[j][k] = new Node(j, k);
			}
		}
	}
	
	public void reCalcPath(boolean map[][],int targx, int targy) {
		hasPath=false;
		path = new Stack<Node>();
		Node start = nodes[x][y];
		LinkedList<Node> open = new LinkedList<Node>();
		LinkedList<Node> closed = new LinkedList<Node>();
		start.g = 0;
		open.add(start);
		Node current = null;
		
		// Get walkable nodes
		for(int i = 0; i < 40; i++) { 
			for(int j = 0; j < 40; j++) {
				if(map[i][j]) {
					closed.add(nodes[i][j]);
				}
				pathMap[i][j] = false;
				nodes[i][j].parent = null;
			}
		}
		while(!open.isEmpty()) {
			current = open.getLast();
			// Get node with lowest f score
			for(Node n: open) {
				n.getFscore(targx, targy);
				if(n.f <= current.f) {
					current = n;
				}
			}
			// Remove node from open list
			open.remove(current);
			closed.add(current);
			
			if(current.x == targx && current.y == targy) {
				while(current.parent != null) {
					pathMap[current.x][current.y] = true; 
					path.push(current);  
					current = current.parent;
				}
				path.push(current);
				hasPath = true;
				return;
			}
			
			// Check walkable neighbours of current node while while setting their parent node to current
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					if(!(i == 0 && j == 0)) {
						if(current.x+i > 0 && current.x+i < 40 && current.y+j < 40 && current.y+j > 0) {
							if(!closed.contains(nodes[current.x+i][current.y+j])) {
								if(nodes[current.x+i][current.y+j].parent == null || nodes[current.x+i][current.y+j].parent.g > current.g) {
									nodes[current.x+i][current.y+j].parent = current;
								}
								if(i == 0 || j == 0) {
									nodes[current.x+i][current.y+j].g = (nodes[current.x+i][current.y+j].parent) != null ? orthoganal + nodes[current.x+i][current.y+j].parent.g : orthoganal;
								}
								else {
									nodes[current.x+i][current.y+j].g = (nodes[current.x+i][current.y+j].parent) != null ? diagonal + nodes[current.x+i][current.y+j].parent.g : diagonal;
								}
								nodes[current.x+i][current.y+j].getFscore(targx, targy);
								if(!open.contains(nodes[current.x+i][current.y+j])) {
									open.add(nodes[current.x+i][current.y+j]);
								} 
								else {
									Node n = null;
									for(Node node : open) {
										if(node.x == current.x+i  && node.y == current.y+j) {
											n = node;
										}
									}
									if(nodes[current.x+i][current.y+j].g < n.g) {
											open.add(nodes[current.x+i][current.y+j]);
									}
								}	
							}
						}
					}
				}
			}	
		}
	}
	
	public void move(boolean map[][],int targx, int targy) {
		if (hasPath) {
			// TO DO: follow A* path, if we have one define
			if(!path.isEmpty()) {
				Node n = path.pop();
				if(!path.isEmpty()) {
					x = path.peek().x;
					y = path.peek().y;
				}	
			}	
		}
		else {
			// no path known, so just do a dumb 'run towards' behaviour
			int newx=x, newy=y;
			if (targx<x)
				newx--;
			else if (targx>x)
				newx++;
			if (targy<y)
				newy--;
			else if (targy>y)
				newy++;
			if (!map[newx][newy]) {
				x=newx;
				y=newy;
			}
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(myImage, x*20, y*20, null);
	}
	
}