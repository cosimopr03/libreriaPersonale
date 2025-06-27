package persistenza;

import model.Libro;

import java.io.IOException;
import java.util.List;
import model.Stato;
public interface ArchivioLibri
{

    /**
     * Aggiunge un nuovo {@link Libro} alla collezione e ne salva
     * immediatamente i dati su memoria di massa.
     *
     * @param titolo   il titolo del libro
     * @param autore   l’autore del libro
     * @param editore  la casa editrice
     * @param isbn     il codice ISBN univoco
     * @param genere   il genere letterario (es. «Narrativa», «Saggio», …)
     *
     * @throws IOException se si verifica un errore di I/O durante il
     *         salvataggio persistente
     */
    void aggiungiLibro(String titolo,
                       String autore,
                       String editore,
                       String isbn,
                       String genere) throws IOException;


    /**
     * Rimuove un libro dalla collezione.
     *
     * @param libro  il {@link Libro} da eliminare
     * @return {@code true} se il libro è stato effettivamente rimosso, {@code false} se non era presente
     * @throws IOException se si verifica un errore di I/O durante l’aggiornamento del salvataggio
     */
    boolean rimuoviLibro(String isbn) throws IOException;

    /**
     * Aggiorna la valutazione assegnata a un libro.
     *
     * @param isbn         codice ISBN univoco del libro
     * @param valutazione  nuova valutazione (es. “5”, “4.5”, “★ ★ ★ ★”, ecc.)
     * @throws IOException se si verifica un errore di I/O durante il salvataggio delle modifiche
     */
    void modificaValutazione(String isbn, String valutazione) throws IOException;

    /**
     * Svuota completamente la libreria eliminando ogni libro.
     *
     * @throws IOException se si verifica un errore di I/O durante la rimozione o il salvataggio
     */
    void svuota() throws IOException;

    /**
     * Aggiorna lo stato di lettura di un libro (es. «da leggere», «in lettura», «letto»).
     *
     * @param isbn   codice ISBN del libro da aggiornare
     * @param stato  nuovo stato di lettura
     * @throws IOException se si verifica un errore di I/O durante il salvataggio delle modifiche
     */
    void modificaStato(String isbn, String stato) throws IOException;

    /**
     * Restituisce l’elenco completo dei libri presenti in libreria.
     *
     * @return lista immutabile di {@link Libro}
     */
    List<Libro> getLibri();

    /**
     * Cerca libri il cui titolo contiene la stringa specificata (case-insensitive).
     *
     * @param titolo parte o intero del titolo da cercare
     * @return lista di libri corrispondenti; può essere vuota se non ci sono risultati
     */
    List<Libro> cerca(String titolo)throws IOException;

    /**
     * Salva lo stato corrente della libreria su memoria persistente
     * (file, database, ecc.). Normalmente viene invocato internamente dai metodi
     * mutatori, ma può essere richiamato esplicitamente.
     */
    void salvaLibri();

    /**
     * Carica i libri precedentemente salvati, sostituendo l’elenco in memoria.
     *
     * @throws IOException se il file/database non è accessibile o è corrotto
     */
    void caricaLibri() throws IOException;

    /**
     * Filtra i libri in base allo stato di lettura.
     *
     * @param stato lo {@link Stato} desiderato
     * @return lista di libri che corrispondono allo stato indicato
     */
    List<Libro> filtraPerStato(String stato) throws IOException;

    /**
     * Filtra i libri in base al genere letterario.
     *
     * @param genere il genere da filtrare (es. “Fantasy”, “Saggio”)
     * @return lista di libri del genere specificato
     */
    List<Libro> filtraPerGenere(String genere) throws IOException;

    /**
     * Filtra i libri che soddisfano contemporaneamente genere e stato.
     *
     * @param genere genere letterario
     * @param stato  stato di lettura
     * @return lista dei libri che rispettano entrambi i criteri
     */
    List<Libro> filtraPerGenerePerStato(String genere, String stato) throws IOException;

    /**
     * Restituisce i libri ordinati alfabeticamente per autore.
     *
     * @return lista ordinata (nuova copia, non muta la collezione originale)
     * @throws IOException se dovesse essere necessario accedere a risorse esterne
     */
    List<Libro> ordinaPerAutore() throws IOException;

    /**
     * Restituisce i libri ordinati alfabeticamente per titolo.
     *
     * @return lista ordinata (nuova copia, non muta la collezione originale)
     * @throws IOException se dovesse essere necessario accedere a risorse esterne
     */
    List<Libro> ordinaPerTitolo() throws IOException;









}
