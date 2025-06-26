package persistenza;

import model.Libro;

import java.io.IOException;
import java.util.List;
import model.Stato;
public interface ArchivioLibri
{

    void aggiungiLibro(Libro libro) throws IOException;
    boolean rimuoviLibro(Libro libro) throws IOException;
    void modificaValutazione(String isbn,String valutazione) throws IOException;
    void svuota()throws IOException;
    void modificaStato(String isbn,String stato) throws IOException;
    List<Libro> getLibri();
    List<Libro> cerca(String titolo);
    void salvaLibri();
    void caricaLibri() throws IOException;
    List<Libro> filtraPerStato(Stato stato);
    List<Libro> filtraPerGenere(String genere) ;
    List<Libro> filtraPeGenerePerStato(String genere,String stato) ;
    List<Libro> ordinaPerAutore() throws IOException;
    List<Libro> ordinaPerTitolo() throws IOException;








}
