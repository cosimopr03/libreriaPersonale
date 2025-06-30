package main;

import persistenza.ArchivioLibri;
import persistenza.ArchivioLibriFileCsv;
import ui.LibreriaUISwing;

import javax.swing.SwingUtilities;
import java.io.IOException;

public class Main
{




    public static void main(String[] args)
    {
        // Crea l'archivio basato su CSV
        ArchivioLibri archivio = new ArchivioLibriFileCsv();
        // Istanzia la UI e le passa l'archivio
        LibreriaUISwing ui = new LibreriaUISwing();
        ui.setArchivio(archivio);
        // Avvia la GUI nel thread Event Dispatch Thread
        SwingUtilities.invokeLater(ui::avvia);
    }

}

