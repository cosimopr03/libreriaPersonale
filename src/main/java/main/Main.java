package main;


import persistenza.ArchivioLibri;
import persistenza.ArchivioLibriFileCsv;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        ArchivioLibri archiovio= new ArchivioLibriFileCsv();
        System.out.println(archiovio.cerca("1984"));
    }
}