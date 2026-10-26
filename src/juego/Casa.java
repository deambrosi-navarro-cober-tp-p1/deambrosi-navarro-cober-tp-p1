package juego;
import entorno.Entorno;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

	public class Casa {
	private int x;
	private int y;
	private int ancho;
	private int alto;
	private Image imagen;
	
	public Casa(int x, int y, int ancho, int alto,String archivo) {
		this.x=x;
		this.y=y;
		this.ancho=ancho;
		this.alto=alto;
        try {
            this.imagen = ImageIO.read(new File(archivo));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	
	public void dibujar(Entorno e) {
		if(imagen != null) {
			e.dibujarImagen(imagen, this.x, this.y, 0, 0.21);
		}
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
	
	public boolean colisionaCon(Jugador jugador) {
		if(jugador.bordeDerecho() < this.bordeIzquierdo()) {
			return false;
		}
		if(jugador.bordeIzquierdo() > this.bordeDerecho()) {
			return false;
		}
		if(jugador.bordeInferior() < this.bordeSuperior()) {
			return false;
		}
		if(jugador.bordeSuperior() > this.bordeInferior()) {
			return false;
		}
		return true;
	}
}