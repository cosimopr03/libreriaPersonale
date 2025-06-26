package persistenza;

public class libroNonPresente extends RuntimeException
{
    public libroNonPresente(String isbn)
    {

      super("Libro non presente, controllare ISBN: " + isbn);
    }
}
