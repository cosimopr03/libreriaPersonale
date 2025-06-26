package persistenza;

import Strategy.*;
import model.Libro;
import model.Stato;
import model.Valutazione;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static model.Stato.IN_LETTURA;

public abstract class AbstractArchivioLibriFile implements ArchivioLibri
{
    protected List<Libro>libri = new ArrayList<Libro>();
    protected OrdinamentoStrategy ordinamentoAutore=new OrdinamentoPerAutore();
    protected OrdinamentoStrategy ordinamentoTitolo =new OrdinamentoPerTitolo();
    protected FiltroStrategy filtroGenere=new FiltraPerGenere();
    protected FiltroStrategy filtroStato=new FiltraPerStato();




    @Override
    public void aggiungiLibro(String titolo,String autore, String editore, String isbn,String genere) throws IOException
    {
        caricaLibri();
        Libro libro = new Libro.Builder(titolo, autore, editore, isbn,genere).build();
        if (libri.contains(libro))
        {
            throw new LibroGiaPresenteException(libro.getIsbn());
        }
        libri.add(libro);
        salvaLibri();


    }

    @Override
    public boolean rimuoviLibro(Libro libro) throws IOException
    {
        return false;
    }

    @Override
    public void modificaValutazione(String isbn, String valutazione) throws IOException
    {
        caricaLibri();
        for(Libro l: libri) {
            if (l.getIsbn().equals(isbn))
            {
                switch (valutazione.trim().toLowerCase())
                {
                    case "1", "uno" -> l.setValutazione(Valutazione.uno);
                    case "2", "due" -> l.setValutazione(Valutazione.due);
                    case "3", "tre" -> l.setValutazione(Valutazione.tre);
                    case "4", "quattro" -> l.setValutazione(Valutazione.quattro);
                    case "5", "cinque" -> l.setValutazione(Valutazione.cinque);
                    default -> l.setValutazione(Valutazione.nonValutato);
                }
                ;
            }
        }
        salvaLibri();

    }

    @Override
    public void svuota() throws IOException
    {
        caricaLibri();
        libri.clear();
        salvaLibri();

    }

    @Override
    public void modificaStato(String isbn, String stato) throws IOException
    {
        caricaLibri();
        for (Libro l : libri)
        {
            if (l.getIsbn().equals(isbn)) {
                try {
                    l.setStatoLibro(Stato.valueOf(stato.trim().toUpperCase()));
                } catch (IllegalArgumentException e) {
                    switch (stato.trim().toLowerCase()) {
                        case "letto" -> l.setStatoLibro(Stato.LETTO);
                        case "in lettura" -> l.setStatoLibro(Stato.IN_LETTURA);
                        case "non letto" -> l.setStatoLibro(Stato.NON_LETTO);
                        default -> throw new IllegalArgumentException("Stato non valido: " + stato);
                    }
                }
                break;
            }
        }
        salvaLibri();

    }
    @Override
    public List<Libro> getLibri()
    {
        return new ArrayList<>(libri);
    }

    @Override
    public List<Libro> cerca(String titolo)
    {
        return List.of();
    }



    @Override
    public List<Libro> filtraPerStato(Stato stato)  throws IOException
    {


    }

    @Override
    public List<Libro> filtraPerGenere(String genere)
    {
        return List.of();
    }

    @Override
    public List<Libro> filtraPeGenerePerStato(String genere, String stato)
    {
        return List.of();
    }

    @Override
    public List<Libro> ordinaPerAutore() throws IOException
    {
        caricaLibri();
        libri = new ArrayList<>(ordinamentoAutore.ordina(libri));
        salvaLibri();
        return new ArrayList<>(libri);
    }

    @Override
    public List<Libro> ordinaPerTitolo() throws IOException
    {
        caricaLibri();
        libri = new ArrayList<>(ordinamentoTitolo.ordina(libri));
        salvaLibri();
        return new ArrayList<>(libri);

    }
}
