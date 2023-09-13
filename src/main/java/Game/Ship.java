package Game;

import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import java.awt.*;

public class Ship {
	int size;
	Image img;
	boolean choose = false;
	Rectangle box;
	int pixel = 50;

	public Ship(int x, int y, int width, int height) {
		this.size = height;
		box = new Rectangle(x, y, width*50, height*50);
	}
	
	public void setDefault() {
		if (box.width != 50) {
			int t = box.width;
			box.width = box.height;
			box.height = t;
		}
		box.x = 500+50*(size-2);
		box.y = 0;
	}
	
	public void draw(Graphics2D g2, Point cursor, int distanceX, int distanceY) {
		
		if (checkEntered((int)cursor.getX(), (int)cursor.getY()) && choose) {
			box.x = box.x+distanceX;
			box.y = box.y+distanceY;
		}
		if (choose) {
			g2.setStroke(new BasicStroke(3));
			g2.drawRect(box.x, box.y, box.width, box.height);
		}
		
		
		if (box.width != pixel) {
			img = new ImageIcon("Ship"+size+"_2.png").getImage();
			g2.drawImage(img, box.x, box.y, box.width, box.height, null);
		}
		else {
			img = new ImageIcon("Ship"+size+".png").getImage();
			g2.drawImage(img, box.x, box.y, box.width, box.height, null);
		}
	}
	public boolean checkEntered(int pointX, int pointY) {
		if (pointX >= box.x && pointX <= box.x+box.width && pointY >= box.y && pointY <= box.y+box.height) return true;
		else return false;
	}
	
	public boolean isInsideMap() {
		if (box.x < 0 || box.x+box.width > 500 || box.y < 0 || box.y+box.height > 500) {
			setDefault();
			return false;
		}
		return true;
	}
	
	public void isIntersects(ArrayList<Ship> ships) {
		Rectangle copyBox = new Rectangle(box.x+1, box.y+1, box.width-1, box.height-1);
		for (Ship s : ships) {
			if (box != s.box) {
				if (copyBox.intersects(s.box)) {
					setDefault();
				}
			}
		}
	}
	
	public void checkPosition() {
		if (box.x%50<25) box.x -=box.x%50;
		else box.x += 50-box.x%50;
		if (box.y%50<25) box.y -= box.y%50;
		else box.y += 50-box.y%50;
	}
	
	public void getPosition(int a[][]) {
		int width = box.width/50;
		int height = box.height/50;
		int x = box.x/50;
		int y = box.y/50;
		a[y][x] = size;
		width--;
		height--;
		while (width != 0) {
			x++;
			width--;
			a[y][x] = size;
		}
		while (height != 0) {
			y++;
			height--;
			a[y][x] = size;
		}
	}
}
