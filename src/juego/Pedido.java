
package juego;

public class Pedido {

    private Casa casaDestino;
    private Negocio negocioOrigen;
    private String estado;
    private long inicioEntrega;
    
    public long getInicioEntrega() {
        return inicioEntrega;
    }

    public Pedido(Casa casaDestino, Negocio negocioOrigen) {
        this.casaDestino = casaDestino;
        this.negocioOrigen = negocioOrigen;
        this.estado = "Realizado";
        this.inicioEntrega = 0;
    }

    public Casa getCasaDestino() {
        return casaDestino;
    }

    public Negocio getNegocioOrigen() {
        return negocioOrigen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;

        if (estado.equals("En camino")) {
            inicioEntrega = System.currentTimeMillis();
        }
    }

    public boolean seVencio(long limiteMilisegundos) {
        if (!estado.equals("En camino")) {
            return false;
        }

        long tiempoTranscurrido =
                System.currentTimeMillis() - inicioEntrega;

        return tiempoTranscurrido >= limiteMilisegundos;
    }
}
