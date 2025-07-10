package persistenza;

import model.Genere;
import model.Libro;
import model.Stato;
import model.Valutazione;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test di integrazione per l'implementazione ArchivioLibriFileCsv.
 * Verifica le operazioni CRUD, persistenza, ricerca, filtri e ordinamento.
 */
class ArchivioLibriTest
{

    private ArchivioLibri archivio;

    /**
     * Prima di ogni test, inizializza l'archivio e lo svuota.
     */
    @BeforeEach
    void setUp()
            throws IOException
    {
        archivio = new ArchivioLibriFileCsv();
        archivio.svuota();  // pulizia del file CSV
    }

    /**
     * Verifica che l'aggiunta di un libro incrementi la lista.
     */
    @Test
    void testAggiuntaLibro()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        List<Libro> libri = archivio.getLibri();
        assertEquals(1, libri.size(), "Deve esserci un solo libro dopo l'aggiunta");
        assertEquals("1984", libri.get(0).getTitolo(), "Il titolo deve corrispondere");
    }

    /**
     * Verifica che aggiungere due volte lo stesso ISBN lanci un'eccezione.
     */
    @Test
    void testDuplicatoLanciaEccezione()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        assertThrows(LibroGiaPresenteException.class, () ->
                        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA"),
                "Dovrebbe lanciare eccezione se ISBN già presente"
        );
    }

    /**
     * Verifica la rimozione di un libro esistente.
     */
    @Test
    void testRimozioneLibro()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        assertTrue(archivio.rimuoviLibro("9788845294970"), "Rimozione dovrebbe restituire true");
        assertTrue(archivio.getLibri().isEmpty(), "La lista deve essere vuota dopo rimozione");
    }

    /**
     * Verifica che la rimozione di un ISBN inesistente restituisca false.
     */
    @Test
    void testRimozioneNonEsistente()
            throws IOException
    {
        assertFalse(archivio.rimuoviLibro("0000000000"), "Rimozione su ISBN inesistente deve restituire false");
    }

    /**
     * Verifica che svuota() rimuova tutti i libri e resetti anche la ricerca.
     */
    @Test
    void testSvuota()
            throws IOException
    {
        archivio.aggiungiLibro("Book1", "Author", "Ed", "1234567890", "DISTOPIA");
        archivio.svuota();
        assertTrue(archivio.getLibri().isEmpty(), "Lista deve essere vuota dopo svuota");
        assertTrue(archivio.cerca("Book1").isEmpty(), "Ricerca non deve trovare nulla dopo svuota");
    }

    /**
     * Verifica la modifica della valutazione al valore corretto.
     */
    @Test
    void testModificaValutazione()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        archivio.modificaValutazione("9788845294970", "5");
        assertEquals(Valutazione.cinque, archivio.getLibri().get(0).getValutazione(),
                "La valutazione deve diventare cinque");
    }

    /**
     * Valutazione non riconosciuta deve impostare nonValutato.
     */
    @Test
    void testModificaValutazioneDefault()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        archivio.modificaValutazione("9788845294970", "abc");
        assertEquals(Valutazione.nonValutato, archivio.getLibri().get(0).getValutazione(),
                "Valutazione non valida deve essere nonValutato");
    }

    /**
     * Verifica la modifica dello stato di lettura.
     */
    @Test
    void testModificaStato()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        archivio.modificaStato("9788845294970", "letto");
        assertEquals(Stato.LETTO, archivio.getLibri().get(0).getStatoLibro(),
                "Lo stato deve diventare LETTO");
    }

    /**
     * Invocare filtra() con criterio null deve lanciare NullPointerException.
     */
    @Test
    void testModificaStatoInvalid()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        assertThrows(NullPointerException.class, () ->
                        archivio.filtra(null, "any"),
                "Filtra con criterio null deve lanciare NullPointerException"
        );
    }

    /**
     * Verifica che i dati siano persistiti correttamente tramite cerca().
     */
    @Test
    void testPersistenza()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "George Orwell", "Mondadori", "9788845294970", "DISTOPIA");
        List<Libro> libri = archivio.cerca("1984");
        assertEquals(1, libri.size(), "Ricerca deve trovare il libro aggiunto");
        assertEquals("1984", libri.get(0).getTitolo(), "Il titolo deve corrispondere");
    }

    /**
     * Verifica i filtri per genere e stato, e il comportamento con parametri null.
     */
    @Test
    void testFiltro()
            throws IOException
    {
        archivio.aggiungiLibro("1984", "A", "E", "1234567890", "DISTOPIA");
        archivio.aggiungiLibro("Brave New World", "B", "E", "1234567891", "FANTASY");

        List<Libro> byGen = archivio.filtra(CriterioFiltro.GENERE, "DISTOPIA");
        assertEquals(1, byGen.size(), "Filtra per genere DISTOPIA deve restituire 1");

        List<Libro> bySta = archivio.filtra(CriterioFiltro.STATO, "NON_LETTO");
        assertFalse(bySta.isEmpty(), "Filtra per stato NON_LETTO non deve essere vuoto");

        assertThrows(NullPointerException.class, () ->
                        archivio.filtra(null, "any"),
                "Filtra con criterio null deve lanciare eccezione"
        );
    }

    /**
     * Verifica l'ordinamento per titolo in ordine alfabetico.
     */
    @Test
    void testOrdinaTitolo()
            throws IOException
    {
        archivio.aggiungiLibro("B Title", "Auth", "E", "1234567890", "DISTOPIA");
        archivio.aggiungiLibro("A Title", "Auth", "E", "1234567891", "DISTOPIA");

        List<Libro> sorted = archivio.ordina(CriterioOrdinamento.TITOLO);
        assertEquals("A TITLE", sorted.get(0).getTitolo(),
                "Il primo titolo ordinato deve essere A TITLE");
    }

    /**
     * Verifica l'ordinamento per autore in ordine alfabetico.
     */
    @Test
    void testOrdinaAutore()
            throws IOException
    {
        archivio.aggiungiLibro("T1", "Z Author", "E", "1234567892", "FANTASY");
        archivio.aggiungiLibro("T2", "A Author", "E", "1234567893", "FANTASY");

        List<Libro> sorted = archivio.ordina(CriterioOrdinamento.AUTORE);
        assertEquals("A AUTHOR", sorted.get(0).getAutori().iterator().next(),
                "Il primo autore ordinato deve essere A AUTHOR");
    }

    /**
     * Verifica il comportamento di cerca() con parametri null o vuoti.
     */
    @Test
    void testCercaNullEmpty()
            throws IOException
    {
        archivio.aggiungiLibro("X", "A", "E", "1234567894", "DISTOPIA");

        assertTrue(archivio.cerca(null).isEmpty(),
                "Ricerca con null non deve lanciare eccezione");
        assertTrue(archivio.cerca("").isEmpty(),
                "Ricerca con stringa vuota non deve trovare nulla");
    }

    /**
     * Verifica la ricerca parziale e normalizzazione del titolo.
     */
    @Test
    void testCerca()
            throws IOException
    {
        archivio.aggiungiLibro(
                "Il Signore degli Anelli",
                "J.R.R. Tolkien",
                "Bompiani",
                "9788804668232",
                "FANTASY"
        );
        archivio.aggiungiLibro(
                "  il   signore degli anelli ",
                "J.R.R. Tolkien",
                "Bompiani",
                "9788804668233",
                "FANTASY"
        );

        List<Libro> risultati = archivio.cerca(" il signore DEGLI   Anelli   ");
        assertEquals(2, risultati.size(),
                "Ricerca normalizzata deve trovare entrambe le varianti");
        assertTrue(archivio.cerca("Harmless").isEmpty(),
                "Ricerca non corrispondente deve restituire lista vuota");
    }

}
