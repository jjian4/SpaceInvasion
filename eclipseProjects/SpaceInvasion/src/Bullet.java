import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bullet extends GameObject {

	Handler handler;
	
	public Bullet(int x, int y, ID id, Handler handler, double mx, double my) {
		super(x, y, id);
		this.handler = handler; 
		
		//Keep net speed constant no matter the direction using trig
		final int netSpeed = 12;
		//theta = arctan(y/x)
		double theta = Math.atan((my - (double) y)/(mx - (double) x));
		if(mx > x) {
			velX = netSpeed * Math.cos(theta);
			velY = netSpeed * Math.sin(theta);
		}
		else {
			velX = -netSpeed * Math.cos(theta);
			velY = -netSpeed * Math.sin(theta);
		}
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
		g.setColor(Color.blue);
		g.fillOval(x, y, 12, 12);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 12, 12);
	}
	
}
