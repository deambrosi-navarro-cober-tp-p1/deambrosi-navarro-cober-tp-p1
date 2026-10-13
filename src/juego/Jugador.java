package juego;
import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;

public class Jugador {
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private Image arriba;
	private Image abajo;
	private Image derecha;
	private Image izquierda;
	private char direccion= 'w';
	public Jugador(double x,double y) {
		this.x=x;
		this.y=y;
		this.ancho = 35;
		this.alto = 35;
		this.arriba=Herramientas.cargarImagen("img/Repartidor arriba.jpg");
		this.abajo=Herramientas.cargarImagen("img/Repartidor abajo.jpg");
		this.derecha=Herramientas.cargarImagen("img/Repartidor derecha.jpg");
		this.izquierda=Herramientas.cargarImagen("img/Repartidor izquierda.jpg");
}
	
	public void cambiarDireccion(char nuevaDireccion) {
		direccion = nuevaDireccion;
	}
	
	public void dibujar(Entorno e) {
		if (direccion == 'w') {
			e.dibujarImagen(arriba, this.x, this.y, 0, 0.10);
		}else if (direccion == 's'){
			e.dibujarImagen(abajo, this.x, this.y, 0, 0.10);
		}else if (direccion == 'a') {
			e.dibujarImagen(izquierda, this.x, this.y, 0, 0.10);
		}else if(direccion == 'd') {
			e.dibujarImagen(derecha, this.x, this.y, 0, 0.10);
		}
	}
	
	public void moverDerecha() {
		this.x=this.x+4;
	}
	public void moverIzquierda() {
		this.x=this.x-4;
	}
	public void moverArriba() {
		this.y=this.y-4;
	}
	public void moverAbajo() {
		this.y=this.y+4;
	}
	
	public double bordeSuperior() {
		return this.y-alto/2;
	}
	public double bordeInferior() {
		return this.y+alto/2;
	}
	public double bordeDerecho() {
		return this.x+ancho/2;
	}
	public double bordeIzquierdo() {
		return this.x-ancho/2;
	}
	
	public boolean colisionaCon(Cuadra c) {
		if(this.bordeIzquierdo()>c.bordeDerecho()) {
			return false;
		}
		if(this.bordeDerecho()<c.bordeIzquierdo()) {
			return false;
		}
		if(this.bordeSuperior()>c.bordeInferior()) {
			return false;
		}
		if(this.bordeInferior()<c.bordeSuperior()) {
			return false;
		}
		return true;
	}
}