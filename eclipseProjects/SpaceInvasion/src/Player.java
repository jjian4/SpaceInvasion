import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

	Handler handler;
	Game game;
	
	BufferedImageLoader loader = new BufferedImageLoader();
	private BufferedImage playerSprite = loader.loadImage("/playerSprite.png");
	

	
	public Player(int x, int y, ID id, Handler handler, Game game) {
		super(x, y, id);
		this.handler = handler;
		this.game = game; 
	}

	public void tick() {
		x += velX;
		y += velY;
				
		collision();
		
		//If there is valid key input, move wizard accordingly
		if(handler.isUp()) velY = -5;
		else if(!handler.isDown()) velY = 0;
		
		if(handler.isDown()) velY = 5;
		else if(!handler.isUp()) velY = 0;
		
		if(handler.isLeft()) velX = -5;
		else if(!handler.isRight()) velX = 0;
		
		if(handler.isRight()) velX = 5;
		else if(!handler.isLeft()) velX = 0;
	}

	private void collision() {
		for(int i = 0; i < handler.object.size(); i++) {
			
			GameObject tempObject = handler.object.get(i);
			
			//Ensure that player cannot share the same coordinates as a block
			if(tempObject.getId() == ID.Rock) {
				if(getBounds().intersects(tempObject.getBounds())) {
					x += velX * -1;
					y += velY * -1;
				}
			}
			
			//Crate gives ammo and disappears
			if(tempObject.getId() == ID.Crate) {
				if(getBounds().intersects(tempObject.getBounds())) {
					game.ammo += 10;
					handler.removeObject(tempObject);
				}
			}
			
			
		}
	}
	
	public void render(Graphics g) {
		g.drawImage(playerSprite, x, y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 48, 48);
	}

}
