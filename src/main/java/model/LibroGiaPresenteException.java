package model;

/**
 * Eccezione Custom per identificare la presenza di un libro
 */
public class LibroGiaPresenteException extends RuntimeException
{
    public LibroGiaPresenteException(String isbn)
    {
        super("Libro già presente con ISBN: " + isbn);
    }
}
