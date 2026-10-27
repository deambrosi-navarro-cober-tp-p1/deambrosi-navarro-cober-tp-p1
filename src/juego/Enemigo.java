
package juego;

import entorno.Entorno;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemigo {

    private double x;
    private double y;
    private double ancho;
    private double alto;
    private Image imagen;

    // 0 = derecha, 1 = abajo, 2 = izquierda, 3 = arriba
    private int direccion;

    private double velocidad = 2;

    private int ultimoCruceX = -1;
    private int ultimoCruceY = -1;

    public Enemigo(double x, double y, boolean horizontal,
                   double limiteMinimo, double limiteMaximo) {

        this.x = x;
        this.y = y;
        this.ancho = 10;
        this.alto = 10;

        // Se conservan estos parámetros para mantener
        // la compatibilidad con el constructor anterior.
        if (horizontal) {
            direccion = 0;
        } else {
            direccion = 1;
        }

        try {
            imagen = ImageIO.read(
                new File("src/img/Enemigo.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void dibujar(Entorno e) {
        if (imagen != null) {
            e.dibujarImagen(imagen, x, y, 0, 0.022);
        }
    }

    public void mover() {

        // Detectar si está cerca de una intersección.
        int cruceX = (int) Math.round((x - 27) / 185.0);
        int cruceY = (int) Math.round((y - 35) / 130.0);

        double xInterseccion = 27 + cruceX * 185;
        double yInterseccion = 35 + cruceY * 130;

        boolean cercaDeCruce =
            cruceX >= 0 && cruceX <= 4
            && cruceY >= 0 && cruceY <= 4
            && Math.abs(x - xInterseccion) <= 3
            && Math.abs(y - yInterseccion) <= 3;

        if (cercaDeCruce
                && (cruceX != ultimoCruceX
                || cruceY != ultimoCruceY)) {

            ultimoCruceX = cruceX;
            ultimoCruceY = cruceY;

            // 50 % de probabilidad de doblar.
            if (Math.random() < 0.5) {

                if (direccion == 0 || direccion == 2) {
                    // Iba horizontal: elige arriba o abajo.
                    direccion = Math.random() < 0.5 ? 1 : 3;
                } else {
                    // Iba vertical: elige izquierda o derecha.
                    direccion = Math.random() < 0.5 ? 0 : 2;
                }
            }
        }

        // Avanzar en la dirección elegida.
        switch (direccion) {
            case 0:
                x += velocidad;
                break;
            case 1:
                y += velocidad;
                break;
            case 2:
                x -= velocidad;
                break;
            case 3:
                y -= velocidad;
                break;
        }

        // Volver a entrar al mapa si llega al borde.
        if (x > 780) x = 20;
        if (x < 20) x = 780;
        if (y > 580) y = 20;
        if (y < 20) y = 580;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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

    public boolean colisionaCon(Jugador jugador) {
        if (jugador.bordeIzquierdo() > bordeDerecho()) return false;
        if (jugador.bordeDerecho() < bordeIzquierdo()) return false;
        if (jugador.bordeSuperior() > bordeInferior()) return false;
        if (jugador.bordeInferior() < bordeSuperior()) return false;

        return true;
    }
}
