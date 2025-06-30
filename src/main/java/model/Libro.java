package model;

import java.util.Objects;

public class Libro
{
    /**
     *  campi obligatori
     */
    private final String titolo;
    private final String autore;
    private final String editore;
    private final String isbn;
    private  final Genere genere;

    /**
     * Campi che presentano un'inzializzazione di default e dunque facoltativi
     */
    private Valutazione valutazione;
    private Stato statoLibro;


    public Libro(String titolo, String autore, String editore, String isbn, String genere)
    {
        this.titolo  = Objects.requireNonNull(titolo,  "Titolo non può essere null").toUpperCase();
        this.autore  = Objects.requireNonNull(autore,  "Autore non può essere null").toUpperCase();
        this.editore = Objects.requireNonNull(editore, "Editore non può essere null").toUpperCase();
        this.isbn    = Objects.requireNonNull(isbn,    "ISBN non può essere null").toUpperCase();
        this.genere= Genere.valueOf(Objects.requireNonNull(genere,"genere non può essere null"));
    }//Libro

    // Getter e  setter (solo i mutabili hanno anche il setter)
    public String getTitolo()
    {
        return titolo;
    }
    public String getAutore()
    {
        return autore;
    }
    public String getEditore()
    {
        return editore;
    }
    public String getIsbn()
    {
        return isbn;
    }
    public Valutazione getValutazione()
    {
        return valutazione;
    }
    public Stato getStatoLibro()
    {
        return statoLibro;
    }
    public Genere getGenere()
    {
        return genere;
    }

    public void setValutazione(Valutazione valutazione)
    {
        this.valutazione =  valutazione;
    }

    public void setStatoLibro(Stato statoLibro)
    {
        this.statoLibro = statoLibro;
    }

    @Override
    public String toString()
    {
        return String.format(
                "Libro{titolo='%s', autore='%s', editore='%s', isbn='%s', valutazione='%s', stato=%s , genere='%s'}",
                titolo, autore, editore, isbn,
                valutazione == null ? "non valutato" : valutazione,
                statoLibro
                ,genere
        );
    }

    /**
     *
     * @param o
     * @return sono ugauli se hanno lo stesso isbn
     */
    @Override
    public boolean equals(Object o)
    {
        if(o ==null) return false;
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return libro.isbn.equals(isbn);
    }

    /***
     *
     * @return Hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(isbn);
    }


}