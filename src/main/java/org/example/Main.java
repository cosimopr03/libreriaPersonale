package org.example;

import model.Libro;

public class Main
{
    public static void main(String[] args)
    {
        Libro l=new Libro.Builder("l","Pino","Mondadori","9788812345673","ROMANZO").build();
        l.setStatoLibro("LETTO");
        System.out.println(l);
    }
}