package Strategy;

import java.util.Comparator;
import java.util.List;
import model.Libro;

/**
 * Ordina i libri in ordine lessicografico di titolo
 */
public class OrdinamentoPerTitolo implements OrdinamentoStrategy
{
    public List<Libro> ordina(List<Libro> libri)
    {
        libri.sort(Comparator.comparing(Libro::getTitolo, String.CASE_INSENSITIVE_ORDER));
        return libri;
    }
}
