import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends GameObject {

	Handler handler;
	
	public Bullet(int x, int y, ID id, Handler handler, int mx, int my) {
		super(x, y, id);
		this.handler = handler; 
		
		//Speed of the bullet
		velX = (mx - x) / 10;
		velY = (my - y) / 10;
	}

	public void tick() {
		x += velX;
		y += velY;
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Rock) {
				if(getBounds().intersects(tempObject.getBounds())) {
					handler.removeObject(this);
				}
			}
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(x, y, 8, 8);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 8, 8);
	}
	
}
