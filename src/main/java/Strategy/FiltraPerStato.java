package Strategy;

import model.Libro;

import java.util.ArrayList;
import java.util.List;
import model.Stato;
public class FiltraPerStato implements FiltroStrategy
{

    @Override
    public List<Libro> filtra(List<Libro> libri,String input)
    {
       Stato stato = Stato.valueOf(input);
       List<Libro> ret=new ArrayList<>();
       for(Libro libro:libri)
           if(libro.getStatoLibro().equals(stato))
               ret.add(libro);
       return ret;
    }

}
