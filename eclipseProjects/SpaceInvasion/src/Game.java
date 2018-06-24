import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

	//serial ID? to remove warning
	private static final long serialVersionUID = 1L;

	private boolean isRunning = false;
	private Thread thread;
	private Handler handler;
	private Camera camera;

	private BufferedImage level = null;
	
	
	public int ammo = 100;
	
	//Constructor
	public Game() {
		//32 x 24 window
		new Window(1024, 768, "Space Invasion", this);
		start();
		
		handler = new Handler();
		camera = new Camera(0, 0);
		this.addKeyListener(new KeyInput(handler));
		this.addMouseListener(new MouseInput(handler, camera, this));
		
		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.loadImage("/spaceLevel.png");
		
		loadLevel(level);
		
	}
	
	
	//Method that starts thread
	private void start() {
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}
	
	//Method that stops thread
	private void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//Similar game loop used in MineCraft
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				tick();
				//updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				frames = 0;
				//updates = 0;
			}
		}
		stop();
	}
	
	//Use to have camera follow player, is called repeatedly during run()
	public void tick() {
		
		//Find the player, then have the camera follow the player
		for(int i = 0; i < handler.object.size(); i++) {
			if(handler.object.get(i).getId() == ID.Player) {
				camera.tick(handler.object.get(i));
			}
		}
		
		handler.tick();
	}
	
	//Animates objects, is called repeatedly during run()
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g;
		
		///////////////////////////////////////
		g.setColor(Color.red);
		g.fillRect(0, 0, 1024, 768);
		
		g2d.translate(-camera.getX(), -camera.getY());
		
		handler.render(g);
		
		///////////////
		g.dispose();
		bs.show();
	}
	
	
	// Load the level
	private void loadLevel(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(red == 255 && green == 0)
					handler.addObject(new Rock(x * 32, y * 32, ID.Rock));
				
				if(blue == 255 && green == 0)
					handler.addObject(new Player(x * 32, y * 32, ID.Player, handler, this));
				
				if(green == 255 && blue == 0)
					handler.addObject(new Enemy(x * 32, y * 32, ID.Enemy, handler ));

				if(blue == 255 && green == 255) {
					handler.addObject(new Crate(x * 32, y * 32, ID.Crate));
				}

			}
		}
		
	}
	
	
	public static void main(String args[]) {
		new Game();
	}
}
