import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Used with Bullet class
public class MouseInput extends MouseAdapter {
	
	private Handler handler;
	private Camera camera;
	private Game game;
	
	public MouseInput(Handler handler, Camera camera, Game game) {
		this.handler = handler;
		this.camera = camera;
		this.game = game;
	}
	
	public void mousePressed(MouseEvent e) {
		double mx = (double) (e.getX() + camera.getX());
		double my = (double) (e.getY() + camera.getY());
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player && game.ammo >= 1) {
				//Add 16 and 24 so bullet begins at center of player, not top left
				handler.addObject(new Bullet(tempObject.getX()+16, tempObject.getY()+24, ID.Bullet, handler, mx, my));
				game.ammo--;
			}
		}
	}
	
}
