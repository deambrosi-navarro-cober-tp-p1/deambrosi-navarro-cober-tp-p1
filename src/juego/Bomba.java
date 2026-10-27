
package juego;

import java.awt.Color;
import entorno.Entorno;

public class Bomba {

    private double x;
    private double y;
    private boolean calleHorizontal;

    private long momentoColocacion;
    private long tiempoParaExplotar = 2500;
    private long duracionExplosion = 400;

    private boolean exploto = false;
    private boolean activa = true;
    private boolean esInterseccion;

    public Bomba(double x, double y, boolean calleHorizontal) {
        this.x = x;
        this.y = y;
        this.calleHorizontal = calleHorizontal;

        this.esInterseccion =
            estaCercaDeCalle(x, new double[]{27, 212, 397, 582, 767})
            && estaCercaDeCalle(y, new double[]{35, 165, 295, 425, 555});

        this.momentoColocacion = System.currentTimeMillis();
    }
    
    private boolean estaCercaDeCalle(double valor, double[] calles) {
        for (int i = 0; i < calles.length; i++) {
            if (Math.abs(valor - calles[i]) <= 12) {
                return true;
            }
        }
        return false;
    }
    
    public void actualizar() {
        long ahora = System.currentTimeMillis();

        if (!exploto && ahora - momentoColocacion >= tiempoParaExplotar) {
            exploto = true;
            momentoColocacion = ahora;
        }

        if (exploto && ahora - momentoColocacion >= duracionExplosion) {
            activa = false;
        }
    }

    public void dibujar(Entorno entorno) {
        if (!activa) {
            return;
        }

        if (!exploto) {
        	entorno.dibujarCirculo(x, y, 14, Color.BLACK);
        	entorno.dibujarCirculo(x, y, 8, Color.RED);
        } else {
            if (esInterseccion) {
                // Explosión horizontal
                entorno.dibujarRectangulo(
                    x, y, 90, 20, 0, Color.ORANGE);

                // Explosión vertical
                entorno.dibujarRectangulo(
                    x, y, 28, 90, 0, Color.ORANGE);
            } else if (calleHorizontal) {
                entorno.dibujarRectangulo(
                    x, y, 90, 20, 0, Color.ORANGE);
            } else {
                entorno.dibujarRectangulo(
                    x, y, 28, 90, 0, Color.ORANGE);
            }
        }
        }
    public boolean afectaJugador(Jugador jugador) {
        if (!exploto || !activa) {
            return false;
        }

        boolean horizontal =
            Math.abs(jugador.getY() - y) <= 10
            && Math.abs(jugador.getX() - x) <= 45;

        boolean vertical =
            Math.abs(jugador.getX() - x) <= 14
            && Math.abs(jugador.getY() - y) <= 45;

        if (esInterseccion) {
            return horizontal || vertical;
        }

        return calleHorizontal ? horizontal : vertical;
    }

    public boolean exploto() {
        return exploto;
    }

    public boolean estaActiva() {
        return activa;
    }
}
