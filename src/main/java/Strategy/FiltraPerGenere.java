package Strategy;

import model.Genere;
import model.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltraPerGenere implements FiltroStrategy
{
    @Override
    public List<Libro> filtra(List<Libro> libri, String input)
    {
        Genere genere = Genere.valueOf(input);
        List<Libro> ret = new ArrayList<Libro>();
        for (Libro libro : libri)
            if(libro.getGenere().equals(genere))
                ret.add(libro);
        return ret;

    }
}
