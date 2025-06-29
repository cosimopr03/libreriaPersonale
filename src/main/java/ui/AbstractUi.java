package ui;


import persistenza.ArchivioLibri;

/**
 * Classe base astratta per tutti i controller dell'interfaccia grafica.
 * Contiene il riferimento all'archivio libri.
 * NON include implementazioni UI specifiche, che dovranno essere fornite
 * dalle classi concrete.
 */
public abstract class AbstractUi
{

    protected ArchivioLibri archivio;

    /**
     * Imposta il riferimento all'archivio libri.
     * Deve essere chiamato subito dopo l'istanza del controller.
     *
     * @param archivio l'implementazione di ArchivioLibri da utilizzare
     */
    public void setArchivio(ArchivioLibri archivio)
    {
        this.archivio = archivio;
    }
    /**
     * Avvia il controller, creando e mostrando la UI associata.
     * Deve essere implementato nelle sottoclassi per definire
     * il flusso di avvio specifico.
     */

    public abstract void avvia();

}
