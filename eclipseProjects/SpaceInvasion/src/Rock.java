import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Rock extends GameObject {

	BufferedImageLoader loader = new BufferedImageLoader();
	private BufferedImage rockSprite = loader.loadImage("/rockDraft.png");
	
	public Rock(int x, int y, ID id) {
		super(x, y, id);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.drawImage(rockSprite, x, y, null);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

}
