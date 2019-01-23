
public class Seleccion {
	
	private static Coordenada comienzoOriginal, finOriginal;
	private Coordenada comienzo, fin;
	private boolean selected, comienzoAdded, finAdded;

	public Seleccion(int i, int j) {
		// TODO Auto-generated constructor stub
		selected = false;
		comienzoOriginal = new Coordenada(0,0);
		finOriginal = new Coordenada(i,j);
		comienzo = new Coordenada(comienzoOriginal);
		fin = new Coordenada(finOriginal);
	}

	public void add(int x, int y) {
		if(finAdded) clear();
		if(comienzoAdded){
			Coordenada aux = new Coordenada(x,y);
			if(aux.lessXThan(comienzo)){
				fin = new Coordenada(comienzo.getX(),y);
				comienzo = new Coordenada(x, comienzo.getY());
			}else {
				fin = new Coordenada(aux);
			}
			if(fin.lessYThan(comienzo)){
				fin = new Coordenada(fin.getX(),comienzo.getY());
				comienzo = new Coordenada(comienzo.getX(), y);
			}
			finAdded = true;
			selected = true;
		} else {
			comienzo = new Coordenada(x,y);
			comienzoAdded = true;
		}
	}

	public Coordenada getComienzo() {
		// TODO Auto-generated method stub
		if(selected) return comienzo;
		else return comienzoOriginal;
	}

	public Coordenada getFin() {
		// TODO Auto-generated method stub
		if(selected) return fin;
		else return finOriginal;
	}

	public void clear() {
		// TODO Auto-generated method stub
		selected = false;
		finAdded = false;
		comienzoAdded = false;
		comienzo = new Coordenada(comienzoOriginal);
		fin = new Coordenada(finOriginal);
	}

	public boolean isSelected() {
		// TODO Auto-generated method stub
		return selected;
	}

}
