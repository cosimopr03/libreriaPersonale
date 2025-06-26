package persistenza;

import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import model.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Implementazione concreta di ArchivioLibri che conserva i dati
 * in un file CSV chiamato <code>dati.csv</code> posizionato nella
 * cartella radice del progetto (working directory).
 * <p>
 * La classe fornisce unicamente i due metodi di persistenza richiesti:
 * {@link #salvaLibri()} e {@link #caricaLibri()}.
 */
public class ArchivioLibriFileCsv extends AbstractArchivioLibriFile
{

    /** Percorso del file nella directory del progetto */
    private static final Path FILE_PATH = Paths.get("dati.csv");

    /* ------------------------------------------------------------------ */
    /*                              SALVA                                 */
    /* ------------------------------------------------------------------ */
    @Override
    public void salvaLibri()
    {
        try {
            // crea eventuali cartelle (in genere nessuna, ma per sicurezza)
            Path parent = FILE_PATH.getParent();
            if (parent != null) Files.createDirectories(parent);

            try (CSVWriter writer = new CSVWriter(
                    Files.newBufferedWriter(FILE_PATH),
                    ';',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {

                // intestazione
                writer.writeNext(new String[]{
                        "isbn", "titolo", "autore", "editore",
                        "valutazione", "stato", "genere"
                });

                // record
                for (Libro l : libri) {
                    writer.writeNext(new String[]{
                            nullable(l.getIsbn()),
                            nullable(l.getTitolo()),
                            nullable(l.getAutore()),
                            nullable(l.getEditore()),
                            enumOrBlank(l.getValutazione()),
                            enumOrBlank(l.getStatoLibro()),
                            enumOrBlank(l.getGenere())
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    /* ------------------------------------------------------------------ */
    /*                              CARICA                                */
    /* ------------------------------------------------------------------ */
    @Override
    public void caricaLibri() throws IOException {
        // Se il file non esiste, lo creo con la sola intestazione
        if (Files.notExists(FILE_PATH)) {
            Files.writeString(FILE_PATH,
                    "isbn;titolo;autore;editore;valutazione;stato;genere\n");
        }

        try (CSVReader reader = new CSVReaderBuilder(
                Files.newBufferedReader(FILE_PATH))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            libri.clear();
            String[] rec;
            int line = 0;

            while ((rec = reader.readNext()) != null) {
                line++;

                // salta intestazione
                if (line == 1 && rec.length > 0 && "isbn".equalsIgnoreCase(rec[0]))
                    continue;

                if (rec.length < 7) {
                    System.err.println("Linea malformata (" + line + "): " + Arrays.toString(rec));
                    continue;
                }

                // parsing enum con gestione errori
                Valutazione val;
                Stato sta;
                Genere gen;
                try {
                    val = rec[4].isBlank() ? null : Valutazione.valueOf(rec[4].trim());
                    sta = rec[5].isBlank() ? null : Stato.valueOf(rec[5].trim());
                    gen = rec[6].isBlank() ? null : Genere.valueOf(rec[6].trim());
                } catch (IllegalArgumentException e) {
                    System.err.println("Enum non valida alla riga " + line + ": " + e.getMessage());
                    continue;
                }

                try {
                    Libro libro = new Libro.Builder(
                            rec[1], // titolo
                            rec[2], // autore
                            rec[3], // editore
                            rec[0], // isbn
                            gen == null ? null : gen.name() // builder accetta string
                    )
                            .valutazione(val)
                            .statoLibro(sta)
                            .build();
                    libri.add(libro);
                } catch (Exception e) {
                    System.err.println("Errore creazione libro alla riga " + line + ": " + e.getMessage());
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException("Errore nella validazione del CSV: " + e.getMessage(), e);
        }
    }

    /* ------------------------------------------------------------------ */
    /*                           HELPER PRIVATI                            */
    /* ------------------------------------------------------------------ */
    private static String nullable(String s) { return s == null ? "" : s; }
    private static String enumOrBlank(Enum<?> e) { return e == null ? "" : e.name(); }
}
