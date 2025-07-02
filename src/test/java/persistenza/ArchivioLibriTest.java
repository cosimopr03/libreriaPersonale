package persistenza;

import model.Genere;
import model.Libro;
import model.Stato;
import model.Valutazione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchivioLibriTest {

    private ArchivioLibri archivio;

    @BeforeEach
    void setUp() throws IOException {
        archivio = new ArchivioLibriFileCsv();
        archivio.svuota();  // pulizia del file prima di ogni test
    }

    @Test
    void testAggiuntaLibro() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        List<Libro> libri = archivio.getLibri();
        assertEquals(1, libri.size());
        assertEquals("1984", libri.get(0).getTitolo());
    }

    @Test
    void testDuplicatoLanciaEccezione() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        assertThrows(LibroGiaPresenteException.class, () ->
                archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA")
        );
    }

    @Test
    void testRimozioneLibro() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        assertTrue(archivio.rimuoviLibro("9788845294970"));
        assertEquals(0, archivio.getLibri().size());
    }

    @Test
    void testRimozioneNonEsistente() throws IOException {
        assertFalse(archivio.rimuoviLibro("0000000000"));
    }

    @Test
    void testSvuota() throws IOException {
        archivio.aggiungiLibro("Book1", "Author", "Ed", "1234567890", "DISTOPIA");
        archivio.svuota();
        assertTrue(archivio.getLibri().isEmpty(), "Lista deve essere vuota dopo svuota");
        // Verifica che anche cerca non trovi nulla
        assertTrue(archivio.cerca("Book1").isEmpty());
    }
    @Test
    void testModificaValutazione() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        archivio.modificaValutazione("9788845294970", "5");
        assertEquals(Valutazione.cinque, archivio.getLibri().get(0).getValutazione());
    }

    @Test
    void testModificaValutazioneDefault() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        archivio.modificaValutazione("9788845294970", "abc");
        assertEquals(Valutazione.nonValutato, archivio.getLibri().get(0).getValutazione(),
                "Valutazione non riconosciuta deve impostare nonValutato");
    }

    @Test
    void testModificaStato() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        archivio.modificaStato("9788845294970", "letto");
        assertEquals(Stato.LETTO, archivio.getLibri().get(0).getStatoLibro());
    }

    @Test
    void testModificaStatoInvalid() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        assertThrows(NullPointerException.class, () ->
                archivio.filtra(null, "any")
        );
    }

    @Test
    void testPersistenza() throws IOException {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        List<Libro> libri = archivio.cerca("1984");
        assertEquals(1, libri.size());
        assertEquals("1984", libri.get(0).getTitolo());
    }


    @Test
    void testFiltro() throws IOException {
        archivio.aggiungiLibro("1984", "A", "E", "1234567890", "DISTOPIA");
        archivio.aggiungiLibro("Brave New World", "B", "E", "1234567891", "FANTASY");
        List<Libro> byGen = archivio.filtra(CriterioFiltro.GENERE, "DISTOPIA");
        assertEquals(1, byGen.size());
        List<Libro> bySta = archivio.filtra(CriterioFiltro.STATO, "NON_LETTO");
        assertFalse(bySta.isEmpty());
        assertThrows(NullPointerException.class, () ->
                archivio.filtra(null, "any")
        );
    }
    @Test
    void testOrdinaTitolo() throws IOException {
        archivio.aggiungiLibro("B Title", "Auth", "E", "1234567890", "DISTOPIA");
        archivio.aggiungiLibro("A Title", "Auth", "E", "1234567891", "DISTOPIA");
        List<Libro> sorted = archivio.ordina(CriterioOrdinamento.TITOLO);
        assertEquals("A TITLE", sorted.get(0).getTitolo());
    }
    @Test
    void testOrdinaAutore() throws IOException
    {
        archivio.aggiungiLibro("T1", "Z Author", "E", "1234567892", "FANTASY");
        archivio.aggiungiLibro("T2", "A Author", "E", "1234567893", "FANTASY");
        List<Libro> sorted = archivio.ordina(CriterioOrdinamento.AUTORE);
        assertEquals("A AUTHOR", sorted.get(0).getAutori().iterator().next());
    }
    @Test
    void testCercaNullEmpty() throws IOException {
        archivio.aggiungiLibro("X", "A", "E", "1234567894", "DISTOPIA");
        assertTrue(archivio.cerca(null).isEmpty(), "Ricerca null non deve lanciare eccezione");
        assertTrue(archivio.cerca("").isEmpty(), "Ricerca stringa vuota non deve trovare nulla");
    }
    @Test
    void testCerca() throws IOException {
        archivio.aggiungiLibro("Il Signore degli Anelli", "J.R.R. Tolkien", "Bompiani", "9788804668232", "FANTASY");
        archivio.aggiungiLibro("  il   signore degli anelli ", "J.R.R. Tolkien", "Bompiani", "9788804668233", "FANTASY");
        List<Libro> risultati = archivio.cerca(" il signore DEGLI   Anelli   ");
        assertEquals(2, risultati.size());
        assertTrue(archivio.cerca("Harmless").isEmpty());
    }
}
