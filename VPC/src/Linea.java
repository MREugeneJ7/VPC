
public class Linea {
	
	private Coordenada[] puntos;
	private int lastIndex;

	public Linea(int i) {
		// TODO Auto-generated constructor stub
		lastIndex = 0;
		puntos = new Coordenada[i];
	}

	public void add(int x, int y) {
		// TODO Auto-generated method stub
		puntos[lastIndex] = new Coordenada(x,y);
		lastIndex++;
		
	}

	public int getLastIndex() {
		// TODO Auto-generated method stub
		return lastIndex;
	}

	public Coordenada getCoordenada(int i) {
		// TODO Auto-generated method stub
		return puntos[i];
	}

}
