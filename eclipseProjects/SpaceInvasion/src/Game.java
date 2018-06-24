import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

	//serial ID? to remove warning
	private static final long serialVersionUID = 1L;

	private boolean isRunning = false;
	private Thread thread;


	//Constructor
	public Game() {
		new Window(1000, 563, "Space Invasion", this);
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
		
	}
	
	//Animates objects, is called repeatedly during run()
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
	
		
		///////////////
		g.dispose();
		bs.show();
	}
	
	
	
	public static void main(String args[]) {
		new Game();
	}
}
