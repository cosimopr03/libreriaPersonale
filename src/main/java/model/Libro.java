package model;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Libro
{
    /**
     * Campi obbligatori
     */
    private final String titolo;
    private final Set<String> autori;
    private final String editore;
    private final String isbn;
    private final Genere genere;

    /**
     * Campi facoltativi (con inizializzazione successiva)
     */
    private Valutazione valutazione;
    private Stato statoLibro;

    /**
     * Costruttore
     */
    public Libro(String titolo, Set<String> autori, String editore, String isbn, String genere)
    {
        if (! isbn.matches("\\d{10}|\\d{13}")  || isbn==null)
        {
            throw new IllegalArgumentException("ISBN non valido: deve avere 10 o 13 cifre numeriche");
        }

        this.titolo = Objects.requireNonNull(titolo, "Titolo non può essere null").toUpperCase();
        this.editore = Objects.requireNonNull(editore, "Editore non può essere null").toUpperCase();
        this.isbn = isbn.toUpperCase();
        this.genere = Genere.valueOf(Objects.requireNonNull(genere, "Genere non può essere null"));

        // Validazione e normalizzazione autori
        Objects.requireNonNull(autori, "Set di autori non può essere null");
        if (autori.isEmpty()) {
            throw new IllegalArgumentException("Il libro deve avere almeno un autore");
        }

        this.autori = new TreeSet<>();
        for (String autore : autori)
        {
            this.autori.add(Objects.requireNonNull(autore, "Autore nullo trovato").toUpperCase());
        }
        this.statoLibro=Stato.NON_LETTO;
        this.valutazione=Valutazione.nonValutato;
    }

    // Getter e setter (solo i mutabili hanno il setter)
    public String getTitolo()
    {
        return titolo;
    }

    public Set<String> getAutori()
    {
        return new TreeSet<>(autori); // ritorna una copia per evitare modifiche esterne
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
        this.valutazione = valutazione;
    }

    public void setStatoLibro(Stato statoLibro)
    {
        this.statoLibro = statoLibro;
    }

    @Override
    public String toString()
    {
        return String.format(
                "Libro{titolo='%s', autori='%s', editore='%s', isbn='%s', valutazione='%s', stato=%s, genere='%s'}",
                titolo,
                String.join(", ", autori),
                editore,
                isbn,
                valutazione == null ? "non valutato" : valutazione,
                statoLibro == null ? "non definito" : statoLibro,
                genere
        );
    }

    /**
     * Due libri sono uguali se hanno lo stesso ISBN
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;
        return isbn.equals(libro.isbn);
    }

    @Override
    public int hashCode()
    {

        return Objects.hash(isbn);
    }



}
