package juego;
import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
		this.ancho = 10;
		this.alto = 10;
		try {
		    this.arriba = ImageIO.read(new File("src/img/Repartidor arriba.png"));
		    this.abajo = ImageIO.read(new File("src/img/Repartidor abajo.png"));
		    this.derecha = ImageIO.read(new File("src/img/Repartidor derecha.png"));
		    this.izquierda = ImageIO.read(new File("src/img/Repartidor izquierda.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
}
	
	public void cambiarDireccion(char nuevaDireccion) {
		direccion = nuevaDireccion;
	}
	
	public void dibujar(Entorno e) {
		if (direccion == 'w') {
			e.dibujarImagen(arriba, this.x, this.y, 0, 0.036);
		}else if (direccion == 's'){
			e.dibujarImagen(abajo, this.x, this.y, 0, 0.036);
		}else if (direccion == 'a') {
			e.dibujarImagen(izquierda, this.x, this.y, 0, 0.036);
		}else if(direccion == 'd') {
			e.dibujarImagen(derecha, this.x, this.y, 0, 0.036);
		}
	}
	
	public void moverDerecha() {
		this.x=this.x+3;
	}
	public void moverIzquierda() {
		this.x=this.x-3;
	}
	public void moverArriba() {
		this.y=this.y-3;
	}
	public void moverAbajo() {
		this.y=this.y+3;
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
	public double getX() {
	    return this.x;
	}

	public double getY() {
	    return this.y;
	}
	public boolean colisionaConNegocio(Negocio negocio) {
	    if (this.bordeIzquierdo() > negocio.bordeDerecho()) {
	        return false;
	    }
	    if (this.bordeDerecho() < negocio.bordeIzquierdo()) {
	        return false;
	    }
	    if (this.bordeSuperior() > negocio.bordeInferior()) {
	        return false;
	    }
	    if (this.bordeInferior() < negocio.bordeSuperior()) {
	        return false;
	    }
	    return true;
	}
	
	public boolean colisionaConCasa(Casa casa) {
	    if (this.bordeIzquierdo() > casa.bordeDerecho()) {
	        return false;
	    }
	    if (this.bordeDerecho() < casa.bordeIzquierdo()) {
	        return false;
	    }
	    if (this.bordeSuperior() > casa.bordeInferior()) {
	        return false;
	    }
	    if (this.bordeInferior() < casa.bordeSuperior()) {
	        return false;
	    }
	    return true;
	}
	
}