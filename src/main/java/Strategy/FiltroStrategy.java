package Strategy;

import model.Libro;
import java.util.List;

public interface FiltroStrategy
{
    List<Libro> filtra(List<Libro> libri,String input);
}
