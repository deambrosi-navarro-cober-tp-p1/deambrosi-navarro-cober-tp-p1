package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Jugador jugador;
	private Mapa Mapa;
	// Variables y métodos propios de cada grupo
	// ...
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		// Inicializar lo que haga falta para el juego
		// ...
		Mapa = new Mapa();
		jugador = new Jugador(400,500);
		// Inicia el juego!
		this.entorno.iniciar();
	}
	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		Mapa.dibujarFondo(entorno);
		Mapa.dibujar(entorno);
		jugador.dibujar(entorno);
		
		if(entorno.estaPresionada('w') && jugador.bordeSuperior()>=0 && !Mapa.colisionaCon(jugador)) {
			jugador.cambiarDireccion('w');
			jugador.moverArriba();
			if(Mapa.colisionaCon(jugador)) {
				jugador.moverAbajo();
			}
		}
		if (entorno.estaPresionada('s') && jugador.bordeInferior()<=entorno.alto() && !Mapa.colisionaCon(jugador)) {
			jugador.cambiarDireccion('s');
			jugador.moverAbajo();
			if(Mapa.colisionaCon(jugador)) {
				jugador.moverArriba();
			}
		}
		if (entorno.estaPresionada('d') && jugador.bordeDerecho()<=entorno.ancho()) {
			jugador.cambiarDireccion('d');
			jugador.moverDerecha();
			if(Mapa.colisionaCon(jugador)) {
				jugador.moverIzquierda();
			}
		}
		if(entorno.estaPresionada('a')&& jugador.bordeIzquierdo()>=0 && !Mapa.colisionaCon(jugador)) {
			jugador.cambiarDireccion('a');
			jugador.moverIzquierda();
			if(Mapa.colisionaCon(jugador)) {
				jugador.moverDerecha();
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}