
package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

    private Entorno entorno;
    private Jugador jugador;
    private Mapa mapa;
    private Pedido pedidoActual;
    private long momentoPedidoPerdido;
    private int pedidosEntregados = 0;
    private int vidas = 3;
    private int enemigosEliminados = 0;
    private Enemigo[] enemigos = new Enemigo[5];
    private long momentoUltimoGolpe = 0;
    private final long tiempoInvulnerable = 1500;

    Juego() {
        this.entorno = new Entorno(this, "Proyecto para TP", 800, 600);

        mapa = new Mapa();
        jugador = new Jugador(400, 500);
        
        for (int i = 0; i < enemigos.length; i++) {
            enemigos[i] = crearEnemigo(); 
        }
        
        crearPedido();

        this.entorno.iniciar();
    }

    // Crea un pedido con una casa de destino y un negocio de origen aleatorios.
    private void crearPedido() {
        Casa[] casas = mapa.getCasas();
        Negocio[] negocios = mapa.getNegocios();

        int indiceCasa = (int) (Math.random() * casas.length);
        int indiceNegocio = (int) (Math.random() * negocios.length);

        Casa casaDestino = casas[indiceCasa];
        Negocio negocioOrigen = negocios[indiceNegocio];

        pedidoActual = new Pedido(casaDestino, negocioOrigen);
        pedidoActual.setEstado("Esperando");
    }
    





    private void dibujarFlecha(double destinoX, double destinoY) {
        double puntaX = destinoX;
        double puntaY = destinoY - 18;
        double baseY = puntaY - 14;

        // Palo de la flecha
        entorno.dibujarRectangulo(
            destinoX, baseY - 12,
            7, 24, 0, Color.YELLOW
        );

        // Punta más ancha y bien unida al palo
        dibujarSegmento(
            destinoX - 12, baseY - 2,
            puntaX, puntaY, 7
        );

        dibujarSegmento(
            puntaX, puntaY,
            destinoX + 12, baseY - 2, 7
        );
    }




private void dibujarSegmento(
        double x1, double y1, double x2, double y2, double grosor) {

    double dx = x2 - x1;
    double dy = y2 - y1;
    double largo = Math.sqrt(dx * dx + dy * dy);
    double angulo = Math.atan2(dy, dx);

    entorno.dibujarRectangulo(
        (x1 + x2) / 2,
        (y1 + y2) / 2,
        largo,
        grosor,
        angulo,
        Color.YELLOW
    );
}



public void tick() {

    // 1. Fondo y mapa.
    mapa.dibujarFondo(entorno);
    mapa.dibujar(entorno);
    
    // enemigos
    for (int i = 0; i < enemigos.length; i++) {
        if (enemigos[i] != null) {
            enemigos[i].mover();
            enemigos[i].dibujar(entorno);
        }
    }


    long ahora = System.currentTimeMillis();

    if (ahora - momentoUltimoGolpe >= tiempoInvulnerable) {
        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i] != null && enemigos[i].colisionaCon(jugador)) {

                // El enemigo desaparece y se crea otro inmediatamente
                enemigos[i] = crearEnemigo();

                // Se resta una vida
                if (vidas > 0) {
                    vidas--;
                }

                momentoUltimoGolpe = ahora;

                // Si llevaba un pedido, lo pierde
                if (pedidoActual != null
                        && pedidoActual.getEstado().equals("En camino")) {
                    pedidoActual.setEstado("Perdido");
                    momentoPedidoPerdido = ahora;
                }

                break;
            }
        }
    }

    
    // 2. Crear un pedido nuevo cuando el anterior fue entregado.

 // 2. Crear un pedido nuevo cuando corresponda.
    if (pedidoActual == null
            || pedidoActual.getEstado().equals("Entregado")) {

        crearPedido();

    } else if (pedidoActual.getEstado().equals("Perdido")
            && System.currentTimeMillis() - momentoPedidoPerdido >= 2000) {

        crearPedido();
    }



    // 3. Retirar el pedido al acercarse al negocio y hacer clic derecho.
    if (pedidoActual.getEstado().equals("Esperando")
            && entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {

        Negocio negocio = pedidoActual.getNegocioOrigen();

        double distanciaX = Math.abs(jugador.getX() - negocio.getX());
        double distanciaY = Math.abs(jugador.getY() - negocio.getY());

        
        if (distanciaX <= 35 && distanciaY <= 45) {
            pedidoActual.setEstado("En camino");
            
        }
    }

    // 4. Entregar el pedido al acercarse a la casa y hacer clic derecho.

if (pedidoActual.getEstado().equals("En camino")
        && entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {

    Casa casa = pedidoActual.getCasaDestino();

    double distanciaX = Math.abs(jugador.getX() - casa.getX());
    double distanciaY = Math.abs(jugador.getY() - casa.getY());

   

    if (distanciaX <= 35 && distanciaY <= 45) {
        pedidoActual.setEstado("Entregado");
        pedidosEntregados++;
    }
}

//Verificar si se vencieron los 60 segundos.
if (pedidoActual.getEstado().equals("En camino")
     && pedidoActual.seVencio(30000)) {

 pedidoActual.setEstado("Perdido");
 momentoPedidoPerdido = System.currentTimeMillis();
}


//6. Mostrar el jugador.
jugador.dibujar(entorno);

//7. Mostrar la HUD.
entorno.cambiarFont("Arial", 18, Color.WHITE);

//Estado del pedido.
entorno.escribirTexto(
     "Estado: " + pedidoActual.getEstado(),
     20, 25
);

//Pedidos entregados.
entorno.escribirTexto(
     "Pedidos entregados: " + pedidosEntregados,
     20, 50
);

//Cuenta regresiva de 60 segundos.
if (pedidoActual.getEstado().equals("En camino")) {

 long tiempoRestante = 30
         - (System.currentTimeMillis()
         - pedidoActual.getInicioEntrega()) / 1000;

 if (tiempoRestante < 0) {
     tiempoRestante = 0;
 }

 entorno.escribirTexto(
         "Tiempo restante: " + tiempoRestante + " segundos",
         20, 75
 );

} else {
 entorno.escribirTexto(
         "Tiempo restante: --",
         20, 75
 );
}

//Vidas.
entorno.escribirTexto(
     "Vidas: " + vidas, 20, 100);

//Enemigos eliminados.
entorno.escribirTexto(
     "Enemigos eliminados: " + enemigosEliminados,20, 125);

    // 8. Dibujar la flecha según el estado actual.
    if (pedidoActual.getEstado().equals("Esperando")) {
        Negocio negocio = pedidoActual.getNegocioOrigen();
        dibujarFlecha(negocio.getX(), negocio.getY());

    } else if (pedidoActual.getEstado().equals("En camino")) {
        Casa casa = pedidoActual.getCasaDestino();
        dibujarFlecha(casa.getX(), casa.getY());
    }

    // 9. Movimiento hacia arriba.

if (entorno.estaPresionada('w')
        && jugador.bordeSuperior() >= 0) {

    jugador.cambiarDireccion('w');
    jugador.moverArriba();

    if (mapa.colisionaCon(jugador)
            || mapa.colisionaConNegocio(jugador)) {
        jugador.moverAbajo();
    }
}

if (entorno.estaPresionada('s')
        && jugador.bordeInferior() <= entorno.alto()) {

    jugador.cambiarDireccion('s');
    jugador.moverAbajo();

    if (mapa.colisionaCon(jugador)
            || mapa.colisionaConNegocio(jugador)) {
        jugador.moverArriba();
    }
}

if (entorno.estaPresionada('d')
        && jugador.bordeDerecho() <= entorno.ancho()) {

    jugador.cambiarDireccion('d');
    jugador.moverDerecha();

    if (mapa.colisionaCon(jugador)
            || mapa.colisionaConNegocio(jugador)) {
        jugador.moverIzquierda();
    }
}

if (entorno.estaPresionada('a')
        && jugador.bordeIzquierdo() >= 0) {

    jugador.cambiarDireccion('a');
    jugador.moverIzquierda();

    if (mapa.colisionaCon(jugador)
            || mapa.colisionaConNegocio(jugador)) {
        jugador.moverDerecha();
    }
    }
}

private Enemigo crearEnemigo() {
    double[] callesX = {27, 212, 397, 582, 767};
    double[] callesY = {35, 165, 295, 425, 555};

    if (Math.random() < 0.5) {
        double x = callesX[(int) (Math.random() * callesX.length)];
        double y = callesY[(int) (Math.random() * callesY.length)];
        return new Enemigo(x, y, true, 20, 780);
    } else {
        double x = callesX[(int) (Math.random() * callesX.length)];
        double y = callesY[(int) (Math.random() * callesY.length)];
        return new Enemigo(x, y, false, 20, 580);
    }
}

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}
