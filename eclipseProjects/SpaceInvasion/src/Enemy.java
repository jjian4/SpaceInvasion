import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends GameObject {

	private Handler handler;
	//Enemy has higher hp if it has forcefield
	private boolean forceField;
	
	Random r = new Random();
	private int rand = 0;
	private int hp;
	
	BufferedImageLoader loader = new BufferedImageLoader();
	private BufferedImage enemySprite = loader.loadImage("/enemySprite.png");
	
	
	public Enemy(int x, int y, ID id, Handler handler, boolean forceField) {
		super(x, y, id);
		this.handler = handler;
		this.forceField = forceField;
		
		if(forceField) {
			hp = 200;
		}
		else {
			hp = 100;
		}
	}

	public void tick() {
		x += velX;
		y += velY;
		
		rand = r.nextInt(10);
		
		//If at a block, bound, or another enemy, move in another direction
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Rock || tempObject.getId() == ID.Bounds 
					|| (tempObject.getId() == ID.Enemy && tempObject != this)) {
				if(getBigBounds().intersects(tempObject.getBounds())) {
					x += velX * -2;
					y += velY * -2;
					velX *= -1;
					velY *= -1;
				}
			}
			
			else if(tempObject.getId() == ID.Bullet) {
				if(getBounds().intersects(tempObject.getBounds())) {
					hp -= 50;
					//remove the bullet after it hits an enemy
					handler.removeObject(tempObject);
				}
			}
			
			//Move randomly
			else if(rand == 0) {
				velX = (r.nextInt(4 - -4) + -4);
				velY = (r.nextInt(4 - -4) + -4);
			}
		}
		
		//kill enemy
		if(hp <= 0) 
			handler.removeObject(this);
		
	}

	public void render(Graphics g) {
		g.drawImage(enemySprite, x, y, null);
		//Remove forceField when hp drops to 100
		if(forceField && hp > 100) {
			Graphics2D g2d = (Graphics2D) g;
			g.setColor(Color.red);
			g2d.draw(getBounds());
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}
	
	//Increase bounds to avoid  touching blocks
	public Rectangle getBigBounds () {
		return new Rectangle(x - 16, y - 16, 64, 64);
	}

}
