package juego;

import entorno.Entorno;
import java.awt.Color;


public class Cuadra {
	private int x;
	private int y;
	private int ancho;
	private int alto;

	
	public Cuadra(int x, int y, int ancho, int alto) {
		this.x=x;
		this.y=y;
		this.ancho=ancho;
		this.alto=alto;
	}
	
	public void dibujarCuadra(Entorno e) {
		e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0, Color.GRAY);
	}
	
	public int bordeSuperior() {
		return this.y-this.alto/2;
	}
	public int bordeInferior() {
		return this.y+this.alto/2;
	}
	public int bordeDerecho() {
		return this.x+this.ancho/2;
	}
	public int bordeIzquierdo() {
		return this.x-this.ancho/2;
	}
}