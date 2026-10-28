
package juego;

import java.awt.Color;
import entorno.Entorno;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

    // ATRIBUTOS DEL JUEGO

    private Entorno entorno;
    private Jugador jugador;
    private Mapa mapa;
    private Pedido pedidoActual;

    // Pedidos
    private int pedidosEntregados = 0;
    private int pedidosPerdidos = 0;
    private final int META_PEDIDOS = 5;
    private final int MAX_PEDIDOS_PERDIDOS = 5;
    private long momentoPedidoPerdido;
    private long momentoCreacionPedido;

    // Jugador
    private int vidas = 3;
    private long momentoUltimoGolpe = 0;
    private final long tiempoInvulnerable = 1500;

    // Enemigos y proyectiles
    private Enemigo[] enemigos = new Enemigo[5];
    private int enemigosEliminados = 0;
    private Proyectil proyectil;

    // Bombas
    private Bomba[] bombas = new Bomba[20];

    // Tiempo y estado de la partida
    private long momentoInicioJuego;
    private boolean juegoTerminado = false;
    private boolean victoria = false;

    // CONSTRUCTOR
    
    Juego() {
        entorno = new Entorno(this, "Proyecto para TP", 800, 600);

        mapa = new Mapa();
        jugador = new Jugador(400, 500);

        for (int i = 0; i < enemigos.length; i++) {
            enemigos[i] = crearEnemigo();
        }

        crearPedido();

        momentoInicioJuego = System.currentTimeMillis();

        entorno.iniciar();
    }

    // CREACIÓN DE PEDIDOS

    private void crearPedido() {
        Casa[] casas = mapa.getCasas();
        Negocio[] negocios = mapa.getNegocios();

        int indiceCasa = (int) (Math.random() * casas.length);
        int indiceNegocio = (int) (Math.random() * negocios.length);

        Casa casaDestino = casas[indiceCasa];
        Negocio negocioOrigen = negocios[indiceNegocio];

        pedidoActual = new Pedido(casaDestino, negocioOrigen);

        momentoCreacionPedido = System.currentTimeMillis();
    }

    // DIBUJO DE FLECHAS

    private void dibujarFlecha(double destinoX, double destinoY) {
        double puntaX = destinoX;
        double puntaY = destinoY - 18;
        double baseY = puntaY - 14;

        // Palo de la flecha
        entorno.dibujarRectangulo(destinoX,baseY - 12,7,24,0,Color.YELLOW);

        // Punta de la flecha
        dibujarSegmento(destinoX - 12, baseY - 2,puntaX, puntaY,7);

        dibujarSegmento(puntaX, puntaY,destinoX + 12, baseY - 2,7);
    }

    private void dibujarSegmento(double x1, double y1,double x2, double y2,double grosor) {
        double dx = x2 - x1;
        double dy = y2 - y1;

        double largo = Math.sqrt(dx * dx + dy * dy);
        double angulo = Math.atan2(dy, dx);

        entorno.dibujarRectangulo((x1 + x2) / 2,(y1 + y2) / 2,largo,grosor,angulo,Color.YELLOW);
    }

    // CICLO PRINCIPAL DEL JUEGO

    public void tick() {

        // Si la partida terminó, mostrar el resultado.
        if (juegoTerminado) {
            mostrarResultado();
            return;
        }

        // 1. Dibujar el fondo y el mapa.
        mapa.dibujarFondo(entorno);
        mapa.dibujar(entorno);

        // 2. Actualizar y dibujar enemigos.
        actualizarEnemigos();

        // 3. Comprobar colisiones con enemigos.
        comprobarColisionesEnemigos();

        // 4. Reponer enemigos eliminados.
        reponerEnemigos();

        // 5. Actualizar y comprobar bombas.
        actualizarBombas();

        // 6. Crear pedidos y actualizar sus estados.
        actualizarPedidos();

        // 7. Comprobar victoria y derrota.
        comprobarFinDelJuego();

        // Si se terminó la partida durante este ciclo,
        // mostrar el resultado y no continuar.
        if (juegoTerminado) {
            mostrarResultado();
            return;
        }

        // 8. Dibujar al jugador.
        jugador.dibujar(entorno);

        // 9. Disparar y actualizar el proyectil.
        disparar();
        actualizarProyectil();

        // 10. Mostrar la información del juego.
        dibujarHUD();

        // 11. Dibujar la flecha del pedido actual.
        dibujarFlechaPedido();

        // 12. Actualizar el movimiento del jugador.
        moverJugador();
    }

    // ENEMIGOS

    private void actualizarEnemigos() {
        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i] != null) {
                enemigos[i].mover();
                enemigos[i].dibujar(entorno);
            }
        }
    }

    private void comprobarColisionesEnemigos() {
        long ahora = System.currentTimeMillis();

        if (ahora - momentoUltimoGolpe < tiempoInvulnerable) {
            return;
        }

        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i] != null && enemigos[i].colisionaCon(jugador)) {

                // Reemplazar al enemigo que chocó con el jugador.
                enemigos[i] = crearEnemigo();

                // Restar una vida.
                if (vidas > 0) {
                    vidas--;
                }

                if (vidas <= 0) {
                    juegoTerminado = true;
                    victoria = false;
                }
                momentoUltimoGolpe = ahora;

                // Perder el pedido si estaba en camino.
                if (pedidoActual != null && pedidoActual.getEstado().equals("En camino")) {
                    pedidoActual.setEstado("Perdido");
                    pedidosPerdidos++;
                    momentoPedidoPerdido = ahora;
                }
                break;
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

    private void reponerEnemigos() {
        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i] == null) {
                enemigos[i] = crearEnemigo();
            }
        }
    }

    // BOMBAS

    private void actualizarBombas() {

        // Permitir que los enemigos dejen bombas.
        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i] != null) {
                Bomba nueva = enemigos[i].intentarDejarBomba();

                if (nueva != null) {
                    for (int j = 0; j < bombas.length; j++) {
                        if (bombas[j] == null) {
                            bombas[j] = nueva;
                            break;
                        }
                    }
                }
            }
        }

        // Actualizar, dibujar y eliminar bombas inactivas.
        for (int i = 0; i < bombas.length; i++) {
            if (bombas[i] != null) {            	
                bombas[i].actualizar();
                bombas[i].dibujar(entorno);

                if (!bombas[i].estaActiva()) {
                    bombas[i] = null;
                }
            }
        }

        // Derrota inmediata por contacto con una bomba
        // o por estar dentro del área de explosión.
        for (int i = 0; i < bombas.length; i++) {
            if (bombas[i] != null && (bombas[i].tocaJugador(jugador) || bombas[i].afectaJugador(jugador))) {
                juegoTerminado = true;
                victoria = false;
                break;
            }
        }
    }

    // PEDIDOS

    private void actualizarPedidos() {

        // Crear un pedido cuando corresponda.
        if (pedidoActual == null || pedidoActual.getEstado().equals("Entregado")) {
            crearPedido();

        } else if (pedidoActual.getEstado().equals("Perdido")  && System.currentTimeMillis() - momentoPedidoPerdido >= 2000) {
            crearPedido();
        }

        // Pasar de Realizado a Esperando después de 2 segundos.
        if (pedidoActual != null && pedidoActual.getEstado().equals("Realizado") && System.currentTimeMillis() - momentoCreacionPedido >= 2000) {
        	pedidoActual.setEstado("Esperando");
        }

        // Retirar el pedido en el negocio con clic derecho.
        if (pedidoActual.getEstado().equals("Esperando") && entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {

            Negocio negocio = pedidoActual.getNegocioOrigen();

            double distanciaX = Math.abs(jugador.getX() - negocio.getX());

            double distanciaY = Math.abs(jugador.getY() - negocio.getY());

            if (distanciaX <= 35 && distanciaY <= 45) {
                pedidoActual.setEstado("En camino");
            }
        }

        // Entregar el pedido en la casa con clic derecho.
        if (pedidoActual.getEstado().equals("En camino") && entorno.sePresionoBoton(entorno.BOTON_DERECHO)) {

            Casa casa = pedidoActual.getCasaDestino();

            double distanciaX = Math.abs(jugador.getX() - casa.getX());

            double distanciaY = Math.abs(jugador.getY() - casa.getY());

            if (distanciaX <= 35 && distanciaY <= 45) {
                pedidoActual.setEstado("Entregado");
                pedidosEntregados++;
            }
        }

        // Comprobar si venció el tiempo de entrega.
        if (pedidoActual.getEstado().equals("En camino") && pedidoActual.seVencio(20000)) {
            pedidoActual.setEstado("Perdido");
            pedidosPerdidos++;
            momentoPedidoPerdido = System.currentTimeMillis();
        }
    }

    // PROYECTILES

    private void disparar() {
        if (proyectil == null && entorno.sePresionoBoton(entorno.BOTON_IZQUIERDO)) {
        	proyectil = new Proyectil(jugador.getX(),jugador.getY(),jugador.getDireccion());
        }
    }

    private void actualizarProyectil() {
        if (proyectil == null) {
            return;
        }
        proyectil.mover();
        proyectil.dibujar(entorno);

        // Si sale del mapa, desaparece.
        if (proyectil.estaFueraDelMapa()) {
            proyectil = null;
            return;
        }

        // Si choca con una casa o un negocio, desaparece.
        if (mapa.colisionaProyectil(proyectil)) {
            proyectil = null;
            return;
        }

        // Comprobar colisiones con enemigos.
        for (int i = 0; i < enemigos.length; i++) {
            if (enemigos[i] != null && proyectil.colisionaCon(enemigos[i])) {
            	proyectil = null;
            	enemigos[i] = null;
            	enemigosEliminados++;

                break;
            }
        }
    }

    // MOVIMIENTO Y COLISION DEL JUGADOR

    private void moverJugador() {

        // Movimiento hacia arriba.
        if (entorno.estaPresionada('w') && jugador.bordeSuperior() >= 0) {
        	jugador.cambiarDireccion('w');
        	jugador.moverArriba();

            if (mapa.colisionaCon(jugador) || mapa.colisionaConNegocio(jugador)) {
            	jugador.moverAbajo();
            }
        }

        // Movimiento hacia abajo.
        if (entorno.estaPresionada('s') && jugador.bordeInferior() <= entorno.alto()) {
        	jugador.cambiarDireccion('s');
        	jugador.moverAbajo();

            if (mapa.colisionaCon(jugador) || mapa.colisionaConNegocio(jugador)) {
            	jugador.moverArriba();
            }
        }

        // Movimiento hacia la derecha.
        if (entorno.estaPresionada('d') && jugador.bordeDerecho() <= entorno.ancho()) {
        	jugador.cambiarDireccion('d');
        	jugador.moverDerecha();

            if (mapa.colisionaCon(jugador) || mapa.colisionaConNegocio(jugador)) {
            	jugador.moverIzquierda();
            }
        }

        // Movimiento hacia la izquierda.
        if (entorno.estaPresionada('a') && jugador.bordeIzquierdo() >= 0) {
        	jugador.cambiarDireccion('a');
        	jugador.moverIzquierda();

            if (mapa.colisionaCon(jugador) || mapa.colisionaConNegocio(jugador)) {
            	jugador.moverDerecha();
            }
        }
    }

    // HUD

    private void dibujarHUD() {
        entorno.cambiarFont("Arial", 18, Color.WHITE);

        // Estado del pedido.
        entorno.escribirTexto("Estado: " + pedidoActual.getEstado(),20, 25);

        // Pedidos entregados.
        entorno.escribirTexto("Pedidos entregados: " + pedidosEntregados,20, 50);

        // Tiempo restante del pedido.
        if (pedidoActual.getEstado().equals("En camino")) {
        	long tiempoRestante = 20 - (System.currentTimeMillis() - pedidoActual.getInicioEntrega()) / 1000;
        	
        	if (tiempoRestante < 0) {tiempoRestante = 0;
            }
        	entorno.escribirTexto("Tiempo restante: " + tiempoRestante + " segundos",20, 75);

        } else {
            entorno.escribirTexto("Tiempo restante: --",20, 75);
        }

        // Vidas.
        entorno.escribirTexto("Vidas: " + vidas,20, 100);

        // Enemigos eliminados.
        entorno.escribirTexto("Enemigos eliminados: " + enemigosEliminados,20, 125);

        // Pedidos perdidos.
        entorno.escribirTexto("Pedidos perdidos: " + pedidosPerdidos,20, 150);

        // Tiempo transcurrido de la partida.
        long tiempoTranscurrido = (System.currentTimeMillis() - momentoInicioJuego) / 1000;

        entorno.escribirTexto("Tiempo transcurrido: " + tiempoTranscurrido + " segundos",20, 175);
    }

    // FLECHA DEL PEDIDO

    private void dibujarFlechaPedido() {

        if (pedidoActual.getEstado().equals("Esperando")) {
            Negocio negocio = pedidoActual.getNegocioOrigen();

            dibujarFlecha(negocio.getX(),negocio.getY());

        } else if (pedidoActual.getEstado().equals("En camino")) {
            Casa casa = pedidoActual.getCasaDestino();

            dibujarFlecha(casa.getX(),casa.getY());
        }
    }

    // VICTORIA Y DERROTA

    private void comprobarFinDelJuego() {

        // Victoria por completar los pedidos necesarios.
        if (pedidosEntregados >= META_PEDIDOS) {juegoTerminado = true;victoria = true;
        }

        // Derrota por perder demasiados pedidos.
        if (pedidosPerdidos >= MAX_PEDIDOS_PERDIDOS) {juegoTerminado = true;victoria = false;
        }
    }

    private void mostrarResultado() {

        if (victoria) {
            entorno.cambiarFont("Arial", 36, Color.YELLOW);
            entorno.escribirTexto("¡GANASTE!", 270, 280);

        } else {
            entorno.cambiarFont("Arial", 36, Color.RED);
            entorno.escribirTexto("¡PERDISTE!", 270, 280);
        }

        entorno.cambiarFont("Arial", 20, Color.WHITE);

        entorno.escribirTexto( "Pedidos entregados: " + pedidosEntregados,270, 320);
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego juego = new Juego();
    }
}