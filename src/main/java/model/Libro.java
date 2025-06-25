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


    private Libro(Builder builder)
    {
        this.titolo      = builder.titolo;
        this.autore      = builder.autore;
        this.editore     = builder.editore;
        this.isbn        = builder.isbn;
        this.valutazione = builder.valutazione;
        this.statoLibro  = builder.statoLibro;
        this.genere = builder.genere;
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

    public void setValutazione(String valutazione)
    {
        this.valutazione =  Valutazione.valueOf(valutazione);
    }

    public void setStatoLibro(String statoLibro)
    {
        this.statoLibro = Stato.valueOf(statoLibro);
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

    /**
     * Costruisce un'istanza di Libro utilizzando un oggetto Builder
     */

    public static class Builder
    {
        // campi obbligatori
        private final String titolo;
        private final String autore;
        private final String editore;
        private final String isbn;
        private final Genere genere;

        // campi opzionali con default
        private Valutazione valutazione = Valutazione.nonValutato;
        private Stato statoLibro = Stato.NON_LETTO;

        /**
         *  Questi campi NON possono essere null o non inizializzati
         * @param titolo
         * @param autore
         * @param editore
         * @param isbn
         * @param genere
         */
        public Builder(String titolo, String autore, String editore, String isbn, String genere)
        {
            this.titolo  = Objects.requireNonNull(titolo,  "Titolo non può essere null").toUpperCase();
            this.autore  = Objects.requireNonNull(autore,  "Autore non può essere null").toUpperCase();
            this.editore = Objects.requireNonNull(editore, "Editore non può essere null").toUpperCase();
            this.isbn    = Objects.requireNonNull(isbn,    "ISBN non può essere null").toUpperCase();
            this.genere= Genere.valueOf(Objects.requireNonNull(genere,"genere non può essere null"));
        }
        public Builder valutazione(Valutazione valutazione)
        {
            this.valutazione = valutazione;
            return this;
        }

        public Builder statoLibro(Stato stato)
        {
            this.statoLibro = stato;
            return this;
        }


        /**
         * Prima di creare l'oggetto, viene effettuata una validazione sull'ISBN, che deve contenere
         * esattamente 10 o 13 cifre numeriche.
         * @return una nuova istanza di {Libro} correttamente inizializzata
         * @throws IllegalArgumentException se l'ISBN non è composto da 10 o 13 cifre numeriche
         */
        public Libro build()
        {
            if (! isbn.matches("\\d{10}|\\d{13}"))
            {
                throw new IllegalArgumentException("ISBN non valido: deve avere 10 o 13 cifre numeriche");
            }
            return new Libro(this);
        }
    }
}