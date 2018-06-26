import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
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
	private BufferedImage background = null;
	
	public int ammo = 100;
	public int hp = 100;
	
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
		
		background = loader.loadImage("/background.png");
		
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
		
		g2d.translate(-camera.getX(), -camera.getY());
		
		//make the floor tiles
		for(int x = 0; x < 1024*2; x+=256) {
			for(int y = 0; y < 768*2; y+=256) {
				g.drawImage(background, x, y, null);
			}
		}
		
		handler.render(g);
		
		g2d.translate(camera.getX(), camera.getY());
		
		//Health bar
		g.setColor(Color.gray);
		g.fillRect(362, 10, 300, 32);
		
		g.setColor(Color.green);
		if(hp > 100) {
			g.fillRect(362, 10, 300, 32);
			g.setColor(Color.yellow);
			g.fillRect(662, 10, hp * 3 - 300, 32);
		}
		else
			g.fillRect(362, 10, hp * 3, 32);
			
		g.setColor(Color.black);
		g.drawRect(362, 10, 300, 32);
		
		//Ammo display
		g.setColor(Color.white);
		g.setFont(new Font("Impact", Font.PLAIN, 20));
		g.drawString("Ammo: " + ammo, 362, 60);
		
		//Game over
		if(hp <= 0) {
			g.setColor(Color.red);
			g.setFont(new Font("Impact", Font.PLAIN, 100));
			g.drawString("G A M E     O V E R", 200, 400);
			//Remove player
			for(int i = 0; i < handler.object.size(); i++) {
				if(handler.object.get(i).getId() == ID.Player) {
					handler.removeObject(handler.object.get(i));
				}
			}
		}
		
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
				
				if(red == 255 && green == 0 && blue == 0)	//red pixel
					handler.addObject(new Bounds(x * 32, y * 32, ID.Bounds));

				if(red == 100 && green == 100 && blue == 100)	//gray pixel
					handler.addObject(new Rock(x * 32, y * 32, ID.Rock, false));
				
				if(red == 255 && green == 255 && blue == 255)	//white pixel
					handler.addObject(new Rock(x * 32, y * 32, ID.Rock, true));
				
				if(red == 0 && blue == 255 && green == 0)	//blue pixel
					handler.addObject(new Player(x * 32, y * 32, ID.Player, handler, this));
				
				if(red == 0 && green == 255 && blue == 0)	//green pixel
					handler.addObject(new Enemy(x * 32, y * 32, ID.Enemy, handler, false));
				
				if(red == 255 && green == 0 && blue == 255)	//purple pixel
					handler.addObject(new Enemy(x * 32, y * 32, ID.Enemy, handler, true));

				if(red == 0 && blue == 255 && green == 255) //cyan pixel
					handler.addObject(new Crate(x * 32, y * 32, ID.Crate));
				
				if(red == 255 && green == 255 && blue == 0)	//yellow pixel
					handler.addObject(new Health(x * 32, y * 32, ID.Health));

			}
		}
		
	}
	
	
	public static void main(String args[]) {
		new Game();
	}
}
