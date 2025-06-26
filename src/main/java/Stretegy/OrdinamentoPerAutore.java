package Stretegy;

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
        libri.sort(Comparator.comparing(Libro::getAutore, String.CASE_INSENSITIVE_ORDER));
        return libri;
    }
}
