import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Player extends GameObject {

	Handler handler;
	Game game;
	
	BufferedImageLoader loader = new BufferedImageLoader();
	private BufferedImage playerSpriteDown = loader.loadImage("/playerSprite.png");
	private BufferedImage playerSpriteDL = loader.loadImage("/playerSpriteDiagonal.png");
	
	//Create different rotations of the player sprite to match movement
	private BufferedImage rotate(BufferedImage img, int numQuadrants) {
		return new AffineTransformOp(
				AffineTransform.getQuadrantRotateInstance(numQuadrants, img.getWidth() / 2,
				img.getHeight() / 2), AffineTransformOp.TYPE_BILINEAR).filter(img, null);
	}
	
    BufferedImage playerSpriteLeft = rotate(playerSpriteDown, 1);
    BufferedImage playerSpriteUp = rotate(playerSpriteDown, 2);
    BufferedImage playerSpriteRight = rotate(playerSpriteDown, 3);
    BufferedImage playerSpriteUL = rotate(playerSpriteDL, 1);
    BufferedImage playerSpriteUR = rotate(playerSpriteDL, 2);
    BufferedImage playerSpriteDR = rotate(playerSpriteDL, 3);

    
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
			
			//Lose health when touching an enemy
			if(tempObject.getId() == ID.Enemy) {
				if(getBounds().intersects(tempObject.getBounds())) {
					game.hp--;
				}
			}
			
			//Gain hp when touching health
			if(tempObject.getId() == ID.Health) {
				if(getBounds().intersects(tempObject.getBounds())) {
					game.hp += 25;
					handler.removeObject(tempObject);
				}
			}
		}
	}
	
	//Initialize lastPosition
	private BufferedImage lastPosition = playerSpriteDown;
	//Use to render specific rotation and update lastPosition at once
	private void positionPlayer(BufferedImage position, Graphics g) {
		g.drawImage(position, x, y, null);
		lastPosition = position;
	}
	
	//Render position depending on keyboard input
	public void render(Graphics g) {
		if(handler.isLeft() && handler.isUp()) {
			positionPlayer(playerSpriteUL, g);
		}
		else if(handler.isLeft() && handler.isDown()) {
			positionPlayer(playerSpriteDL, g);
		}
		else if(handler.isRight() && handler.isUp()) {
			positionPlayer(playerSpriteUR, g);
		}
		else if(handler.isRight() && handler.isDown()) {
			positionPlayer(playerSpriteDR, g);
		}
		else if(handler.isLeft()) {
			positionPlayer(playerSpriteLeft, g);
		}
		else if(handler.isRight()) {
			positionPlayer(playerSpriteRight, g);
		}
		else if(handler.isUp()) {
			positionPlayer(playerSpriteUp, g);
		}
		else if(handler.isDown()){
			positionPlayer(playerSpriteDown, g);
		}
		else {
			g.drawImage(lastPosition, x, y, null);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 48, 48);
	}

}
