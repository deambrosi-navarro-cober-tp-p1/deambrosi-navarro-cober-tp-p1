package juego;

import java.awt.Color;

import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Jugador jugador;
	private Cuadra cuadra;
	// Variables y métodos propios de cada grupo
	// ...
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);
		jugador = new Jugador(400,500);
		cuadra = new Cuadra(150,150,100,100);
		// Inicializar lo que haga falta para el juego
		// ...

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
		jugador.dibujar(entorno);
		cuadra.dibujarCuadra(entorno);
		
		if(entorno.estaPresionada('w') && jugador.bordeSuperior()>=0 && !jugador.colisionaCon(cuadra)) {
			jugador.cambiarDireccion('w');
			jugador.moverArriba();
			if(jugador.colisionaCon(cuadra)) {
				jugador.moverAbajo();
			}
		}
		if (entorno.estaPresionada('s') && jugador.bordeInferior()<=entorno.alto() && !jugador.colisionaCon(cuadra)) {
			jugador.cambiarDireccion('s');
			jugador.moverAbajo();
			if(jugador.colisionaCon(cuadra)) {
				jugador.moverArriba();
			}
		}
		if (entorno.estaPresionada('d') && jugador.bordeDerecho()<=entorno.ancho()) {
			jugador.cambiarDireccion('d');
			jugador.moverDerecha();
			if(jugador.colisionaCon(cuadra)) {
				jugador.moverIzquierda();
			}
		}
		if(entorno.estaPresionada('a')&& jugador.bordeIzquierdo()>=0 && !jugador.colisionaCon(cuadra)) {
			jugador.cambiarDireccion('a');
			jugador.moverIzquierda();
			if(jugador.colisionaCon(cuadra)) {
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
