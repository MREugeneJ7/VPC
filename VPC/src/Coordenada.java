
public class Coordenada {
	
	private int x,y;
	
	Coordenada(int x,int y){
		this.x =x;
		this.y =y;
	}
	
	public Coordenada(Coordenada other) {
		// TODO Auto-generated constructor stub
		this.x = other.getX();
		this.y = other.getY();
	}

	int getX() {
		return x;
	}
	
	int getY() {
		return y;
	}

	public boolean lessXThan(Coordenada other) {
		// TODO Auto-generated method stub
		return this.x < other.getX();
	}
	
	public boolean lessYThan(Coordenada other) {
		// TODO Auto-generated method stub
		return this.y < other.getY();
	}

}
