package Stretegy;

import java.util.List;
import model.Libro;

/**
 * Strategie di ordinamento per oggetti {@link Libro}
 */


public interface OrdinamentoStrategy
{
    List<Libro> ordina(List<Libro> libri);
}
