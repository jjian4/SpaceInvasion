import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Rock extends GameObject {

	//decides which sprite to use
	private boolean large;
	
	BufferedImageLoader loader = new BufferedImageLoader();
	private BufferedImage rockSprite = loader.loadImage("/rockSprite.png");
	private BufferedImage rockSpriteLarge = loader.loadImage("/rockSpriteLarge.png");
	
	public Rock(int x, int y, ID id, boolean large) {
		super(x, y, id);
		this.large = large;
		
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		if(large) {
			g.drawImage(rockSpriteLarge, x, y, null);
		} else {
		g.drawImage(rockSprite, x, y, null);
		}
	}

	public Rectangle getBounds() {
		if(large) {
			return new Rectangle(x, y, 128, 128);
		} else {
		return new Rectangle(x, y, 64, 64);
		}
	}

}
