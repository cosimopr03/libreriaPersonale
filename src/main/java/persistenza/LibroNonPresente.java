package persistenza;

public class LibroNonPresente extends RuntimeException
{
    public LibroNonPresente(String isbn)
    {

      super("Libro non presente, controllare ISBN: " + isbn);
    }
}
