
package juego;

import java.awt.Color;
import entorno.Entorno;

public class Proyectil {

    private double x;
    private double y;
    private char direccion;
    private double velocidad = 6;
    private boolean activo = true;

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
            entorno.dibujarCirculo(x, y, 10, Color.YELLOW);
        }
    }

    public boolean estaFueraDelMapa() {
        return x < 0 || x > 800 || y < 0 || y > 600;
    }

    public boolean colisionaCon(Enemigo enemigo) {
        return Math.abs(x - enemigo.getX()) <= 10
            && Math.abs(y - enemigo.getY()) <= 10;
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
