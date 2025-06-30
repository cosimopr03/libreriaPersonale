package facade;

import javax.swing.SwingUtilities;
import persistenza.ArchivioLibri;
import persistenza.ArchivioLibriFileCsv;
import ui.AbstractUi;
import ui.LibreriaUISwing;

public class LibreriaUiFacade
{

    private static LibreriaUiFacade instance;

    private final AbstractUi ui;


    private LibreriaUiFacade()
    {
        ArchivioLibri archivio = new ArchivioLibriFileCsv();
        this.ui = new LibreriaUISwing();
        this.ui.setArchivio(archivio);
    }

    // Singleton
    public static synchronized LibreriaUiFacade getInstance()
    {
        if (instance == null)
        {
            instance = new LibreriaUiFacade();
        }
        return instance;
    }


    public void avvia()
    {
       SwingUtilities.invokeLater(ui::avvia);

    }
}
