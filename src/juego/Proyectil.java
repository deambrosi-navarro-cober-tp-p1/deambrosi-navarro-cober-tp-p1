
package juego;

import java.awt.Color;
import entorno.Entorno;

public class Proyectil {

    private double x;
    private double y;
    private char direccion;
    private double velocidad = 6;
    private boolean activo = true;
    private double ancho = 10;
    private double alto = 10;

    public Proyectil(double x, double y, char direccion) {
        this.x = x;
        this.y = y;
        this.direccion = direccion;
    }

    public void mover() {
        if (direccion == 'w') {
            y -= velocidad;
        } else if (direccion == 's') {
            y += velocidad;
        } else if (direccion == 'a') {
            x -= velocidad;
        } else if (direccion == 'd') {
            x += velocidad;
        }
    }

    public void dibujar(Entorno entorno) {
        if (activo) {
            entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.YELLOW);
        }
    }

    public double bordeSuperior() {
        return y - alto / 2;
    }

    public double bordeInferior() {
        return y + alto / 2;
    }

    public double bordeDerecho() {
        return x + ancho / 2;
    }

    public double bordeIzquierdo() {
        return x - ancho / 2;
    }

    public boolean estaFueraDelMapa() {
        return x < 0 || x > 800 || y < 0 || y > 600;
    }

    public boolean colisionaCon(Enemigo enemigo) {
        if (bordeDerecho() < enemigo.bordeIzquierdo()) {
            return false;
        }

        if (bordeIzquierdo() > enemigo.bordeDerecho()) {
            return false;
        }

        if (bordeInferior() < enemigo.bordeSuperior()) {
            return false;
        }

        if (bordeSuperior() > enemigo.bordeInferior()) {
            return false;
        }

        return true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void desactivar() {
        activo = false;
    }
}
