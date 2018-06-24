
public class Camera {
	
	private float x, y;
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}

	
	public void tick(GameObject object) {
		
		//Have the camera roughly lock onto character
		x += ((object.getX() - x) - 1024/2) * 0.05f;
		y += ((object.getY() - y) - 768/2) * 0.05f;
		
		//Prevent camera from going out of level
		if(x <= 0) x = 0;
		if(x >= 1000 + 16) x = 1000 + 16;
		if(y <= 0) y = 0;
		if(y >= 768 + 16) y = 768 + 16;
	}


	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}
	
}
