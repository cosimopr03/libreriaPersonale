package main;


<<<<<<< HEAD
import model.Libro;
=======
import persistenza.ArchivioLibri;
import persistenza.ArchivioLibriFileCsv;

import java.io.IOException;
>>>>>>> develop

public class Main
{
    public static void main(String[] args) throws IOException
    {
<<<<<<< HEAD
        

=======
        ArchivioLibri archiovio= new ArchivioLibriFileCsv();
        System.out.println(archiovio.cerca("1984"));
>>>>>>> develop
    }
}