package facade;

import javax.swing.SwingUtilities;
import persistenza.ArchivioLibri;
import persistenza.ArchivioLibriFileCsv;
import ui.AbstractUi;
import ui.LibreriaUISwing;

/**
 * Facade per l’avvio dell’interfaccia grafica della libreria.
 * Implementa il pattern Singleton per garantire una sola istanza
 * e orchestra la creazione del backend (ArchivioLibri) e della UI.
 */
public class LibreriaUiFacade
{

    /**
     * Istanza singleton di {@code LibreriaUiFacade}.
     */
    private static LibreriaUiFacade instance;

    /**
     * Riferimento all’interfaccia astratta della UI,
     * a cui verrà passato il riferimento all’archivio.
     */
    private final AbstractUi ui;


    /**
     * Costruttore privato: crea l'archivio CSV e la UI Swing,
     * e collega i due componenti.
     */
    private LibreriaUiFacade()
    {
        // Crea l’implementazione concreta dell’archivio (persistenza CSV)
        ArchivioLibri archivio = new ArchivioLibriFileCsv();
        // Crea la UI basata su Swing
        this.ui = new LibreriaUISwing();
        // Inietta l’archivio nella UI
        this.ui.setArchivio(archivio);
    }

    /**
     * Restituisce l’unica istanza di {@code LibreriaUiFacade}.
     * Se non esiste ancora, la crea in modo thread-safe.
     *
     * @return l’istanza singleton di LibreriaUiFacade
     */
    public static synchronized LibreriaUiFacade getInstance()
    {
        if (instance == null)
        {
            instance = new LibreriaUiFacade();
        }
        return instance;
    }


    /**
     * Avvia l’applicazione UI nel thread
     * di Event Dispatch di Swing per la sicurezza
     * nella gestione dei componenti grafici.
     */
    public void avvia()
    {
        SwingUtilities.invokeLater(ui::avvia);
    }
}
