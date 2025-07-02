package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

public class LibroTest
{

    @Test
    void testCostruttore()
    {
        Libro libro = new Libro("1984", Set.of("George Orwell"), "Mondadori","1234567890123", "DISTOPIA");
        assertEquals("1984", libro.getTitolo());
        assertTrue(libro.getAutori().contains("GEORGE ORWELL"));
        assertEquals("1234567890123", libro.getIsbn());
        assertEquals(Genere.DISTOPIA, libro.getGenere());
        assertEquals(Valutazione.nonValutato, libro.getValutazione());
        assertEquals(Stato.NON_LETTO, libro.getStatoLibro());
    }

    @Test
    void testSetValutazione()
    {
        Libro libro = new Libro("1984", Set.of("George Orwell"), "Mondadori","1234567890123", "DISTOPIA");
        assertEquals(Valutazione.nonValutato, libro.getValutazione());
        libro.setValutazione(Valutazione.cinque);
        assertEquals(Valutazione.cinque, libro.getValutazione());
    }
    @Test
    void testSetStato()
    {
        Libro libro = new Libro("1984", Set.of("George Orwell"), "Mondadori","1234567890123", "DISTOPIA");
        libro.setStatoLibro(Stato.IN_LETTURA);
        assertEquals(Stato.IN_LETTURA, libro.getStatoLibro());
        libro.setStatoLibro(Stato.LETTO);
        assertEquals(Stato.LETTO, libro.getStatoLibro());
    }
    @Test
    void testTitoloNullLanciaEccezione()
    {
        assertThrows(NullPointerException.class, () -> {
            new Libro(null, Set.of("George Orwell"), "Mondadori","1234567890123", "DISTOPIA");;
        });
    }
    @Test
    void testEccezioneIsbnCorto()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("1984", Set.of("George Orwell"), "Mondadori", "123", "DISTOPIA");
        });
    }
    @Test
    void testEccezioneIsbnLungo()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("1984", Set.of("George Orwell"), "Mondadori", "15353637383737323", "DISTOPIA");
        });
    }
    void testEccezioneIsbnNull()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("1984", Set.of("George Orwell"), "Mondadori", null, "DISTOPIA");
        });
    }
    @Test
    void testAutoriNullLanciaEccezione()
    {
        assertThrows(NullPointerException.class, () -> {
            new Libro("1984", null, "Mondadori","1234567890123", "DISTOPIA");
        });
    }
    @Test
    void testAutoriVuotoLanciaEccezione()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("1984", Set.of(), "Mondadori","1234567890123", "DISTOPIA");
        });
    }
    @Test
    void testGenereNonValidoLanciaEccezione()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new Libro("1984", Set.of("George Orwell"), "Mondadori","1234567890123", "GenereNonVAlido");
        });
    }
    @Test
    void testEquals()
    {
        Libro libro1 = new Libro("1984", Set.of("George Orwell"), "Mondadori","1234567890123", "DISTOPIA");
        Libro libro2 = new Libro("1984", Set.of("George Orwell"), "Mondadori","1234567890123", "DISTOPIA");
        Libro libro3 = new Libro("1984", Set.of("George Orwell"), "Mondadori","1234567890124", "DISTOPIA");
        assertEquals(libro1, libro2);
        assertNotEquals(libro1, libro3);
    }
    @Test
    void testHashCodeStessoISBN()
    {
        Libro libro1 = new Libro("1984", Set.of("George Orwell"), "Mondadori", "9788845294970", "DISTOPIA");
        Libro libro2 = new Libro("1984", Set.of("George Orwell"), "Mondadori", "9788845294970", "DISTOPIA");

        assertEquals(libro1.hashCode(), libro2.hashCode(), "Due libri con stesso ISBN devono avere stesso hashCode");
    }

    @Test
    void testHashCodeISBNDiversi()
    {
        Libro libro1 = new Libro("1984", Set.of("George Orwell"), "Mondadori", "9788845294970", "DISTOPIA");
        Libro libro2 = new Libro("1984", Set.of("George Orwell"), "Mondadori", "9788804703100", "DISTOPIA");

        assertNotEquals(libro1.hashCode(), libro2.hashCode(), "Libri con ISBN diversi dovrebbero avere hashCode diversi");
    }






}
