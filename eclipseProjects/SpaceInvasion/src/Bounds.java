import java.awt.Graphics;
import java.awt.Rectangle;

//Invisible (no rendering) but used to keep player and enemies in the level
public class Bounds extends GameObject {

	public Bounds(int x, int y, ID id) {
		super(x, y, id);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 32);
	}

}
