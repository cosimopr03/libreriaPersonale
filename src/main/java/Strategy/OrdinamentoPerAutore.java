package Strategy;

import java.util.Comparator;
import java.util.List;
import model.Libro;

/**
 * Ordina i libri in ordine lessicografico di autore
 */
public class OrdinamentoPerAutore implements OrdinamentoStrategy
{
    @Override
    public List<Libro> ordina(List<Libro> libri)
    {
        libri.sort(Comparator.comparing(
                libro -> libro.getAutori().iterator().next(),  // primo autore
                String.CASE_INSENSITIVE_ORDER
        ));
        return libri;
    }
}
