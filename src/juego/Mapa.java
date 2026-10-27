
package juego;

import entorno.Entorno;
import java.awt.Color;

public class Mapa {

    private Cuadra[] cuadras;
    private Casa[] casas;
    private Negocio[] negocios;

    private String[] imagenesCasas = {
        "src/img/casa_verde.png",
        "src/img/casa_azul.png",
        "src/img/casa_roja.png",
        "src/img/casa_amarilla.png",
        "src/img/casa_violeta.png",
        "src/img/casa_rosa.png",
        "src/img/casa_turquesa.png",
        "src/img/casa_gris.png"
    };

    private String[] imagenesNegocios = {
        "src/img/negocio_almacen.png",
        "src/img/negocio_kiosco.png",
        "src/img/negocio_farmacia.png",
        "src/img/negocio_restaurante.png",
        "src/img/negocio_restaurante.png",
        "src/img/negocio_almacen.png",
        "src/img/negocio_kiosco.png",
        "src/img/negocio_farmacia.png",
        "src/img/negocio_kiosco.png",
        "src/img/negocio_restaurante.png",
        "src/img/negocio_almacen.png",
        "src/img/negocio_farmacia.png",
        "src/img/negocio_farmacia.png",
        "src/img/negocio_kiosco.png",
        "src/img/negocio_restaurante.png",
        "src/img/negocio_almacen.png"
    };

    public Mapa() {

        cuadras = new Cuadra[16];
        casas = new Casa[48];
        negocios = new Negocio[16];

        int indice = 0;

        for (int fila = 0; fila < 4; fila++) {
            for (int columna = 0; columna < 4; columna++) {

                int x = 120 + columna * 185;
                int y = 100 + fila * 130;

                cuadras[indice] = new Cuadra(x, y, 75, 75);

                // Casas
                casas[indice * 3] = new Casa(
                    x - 32, y - 26, 26, 26,
                    imagenesCasas[(indice * 3) % imagenesCasas.length]
                );

                casas[indice * 3 + 1] = new Casa(
                    x + 32, y - 26, 26, 26,
                    imagenesCasas[(indice * 3 + 1) % imagenesCasas.length]
                );

                casas[indice * 3 + 2] = new Casa(
                    x - 32, y + 26, 26, 26,
                    imagenesCasas[(indice * 3 + 2) % imagenesCasas.length]
                );

                // Negocios
                negocios[indice] = new Negocio(
                    x + 32, y + 26, 26, 26,
                    imagenesNegocios[indice]
                );

                indice++;
            }
        }
    }

    public void dibujar(Entorno e) {

        for (int i = 0; i < cuadras.length; i++) {
            cuadras[i].dibujarCuadra(e);
        }

        for (int i = 0; i < casas.length; i++) {
            casas[i].dibujar(e);
        }

        for (int i = 0; i < negocios.length; i++) {
            negocios[i].dibujar(e);
        }
    }

    public void dibujarFondo(Entorno e) {

        // Pasto
        e.dibujarRectangulo(
            400, 300, 800, 600, 0,
            new Color(115, 160, 95)
        );

        // Calles verticales
        for (int x = 27; x <= 767; x += 185) {
            e.dibujarRectangulo(
                x, 300, 28, 600, 0,
                new Color(65, 70, 78)
            );
        }

        // Calles horizontales
        for (int y = 35; y <= 555; y += 130) {
            e.dibujarRectangulo(
                400, y, 800, 20, 0,
                new Color(65, 70, 78)
            );
        }
    }

    // Las casas bloquean el movimiento.
    public boolean colisionaCon(Jugador jugador) {

        for (int i = 0; i < casas.length; i++) {
            if (casas[i].colisionaCon(jugador)) {
                return true;
            }
        }

        return false;
    }

    // Los negocios se comprueban por separado.
    public boolean colisionaConNegocio(Jugador jugador) {

        for (int i = 0; i < negocios.length; i++) {
            if (negocios[i].colisionaCon(jugador)) {
                return true;
            }
        }

        return false;
    }

    public Casa[] getCasas() {
        return casas;
    }

    public Negocio[] getNegocios() {
        return negocios;
    }
}
