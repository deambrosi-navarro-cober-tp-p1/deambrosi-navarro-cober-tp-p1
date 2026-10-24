package juego;

import entorno.Entorno;

public class Mapa {

	private Cuadra[] cuadras;
	
	public Mapa() {
		cuadras = new Cuadra[16];
		
		int indice = 0;
		
		for (int fila=0; fila < 4; fila++) {
			for(int columna=0; columna < 4; columna++) {
				int x = 120 + columna * 185;
				int y = 100 + fila * 130;
				
				cuadras[indice] = new Cuadra(x,y,75,75);
				indice++;
			}
		}
		
	}
	
	public void dibujar (Entorno e) {
		for(int i=0; i<cuadras.length; i++) {
			cuadras[i].dibujarCuadra(e);
		}
	}
	public boolean colisionaCon(Jugador jugador) {
		for(int i=0; i<cuadras.length; i++) {
			if(jugador.colisionaCon(cuadras[i])) {
				return true;
			}
		}
		return false;
	}
}