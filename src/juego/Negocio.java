
package juego;

import entorno.Entorno;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Negocio {
    private int x;
    private int y;
    private int alto;
    private int ancho;
    private Image imagen;

    public Negocio(int x, int y, int alto, int ancho, String archivo) {
        this.x = x;
        this.y = y;
        this.alto = alto;
        this.ancho = ancho;

        try {
            this.imagen = ImageIO.read(new File(archivo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dibujar(Entorno e) {
        // Rectángulo de colisión
        e.dibujarRectangulo(this.x, this.y, this.ancho, this.alto, 0,new Color(115, 160, 95));

        // Imagen del negocio
        if (imagen != null) {
            e.dibujarImagen(imagen, this.x, this.y, 0, 0.20);
        }
    }

    public int bordeSuperior() {
        return this.y - this.alto / 2;
    }

    public int bordeInferior() {
        return this.y + this.alto / 2;
    }

    public int bordeDerecho() {
        return this.x + this.ancho / 2;
    }

    public int bordeIzquierdo() {
        return this.x - this.ancho / 2;
    }

    public boolean colisionaCon(Jugador jugador) {
        if (jugador.bordeIzquierdo() >= this.bordeDerecho()) {
            return false;
        }

        if (jugador.bordeDerecho() <= this.bordeIzquierdo()) {
            return false;
        }

        if (jugador.bordeSuperior() >= this.bordeInferior()) {
            return false;
        }

        if (jugador.bordeInferior() <= this.bordeSuperior()) {
            return false;
        }

        return true;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean contieneMouse(int mouseX, int mouseY) {
        return mouseX >= bordeIzquierdo()&& mouseX <= bordeDerecho()&& mouseY >= bordeSuperior()&& mouseY <= bordeInferior();
    }
}
