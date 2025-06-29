package ui;

import model.Valutazione;
import model.Libro;
import persistenza.CriterioOrdinamento;
import persistenza.ArchivioLibri;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI per la gestione della libreria personale.
 * Include filtri, obbligatorietà campi e aggiornamento automatico.
 */
public class LibreriaUISwing extends AbstractUi
{
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Libro> allBooks;

    private JComboBox<String> cbFiltroStato;
    private JComboBox<String> cbFiltroValutazione;
    private JButton btnResetFiltri;
    private JButton btnAdd, btnRemove, btnModify, btnSortTitolo, btnSortAutore, btnSearch;

    /**
     * Inizializza e mostra l'interfaccia grafica della libreria.
     * Configura frame, pannelli, tabella e associa gli eventi.
     */
    @Override
    public void avvia()
    {
        frame = new JFrame("Gestione Libreria");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(5, 5));

        // Tabella libri
        tableModel = new DefaultTableModel(
                new Object[]{"ISBN", "Titolo", "Autore", "Editore", "Genere", "Stato", "Valutazione"},
                0
        )
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Pannello filtri
        cbFiltroStato = new JComboBox<>(new String[]{"Tutti", "da leggere", "in lettura", "letto"});
        List<String> vals = new ArrayList<>();
        vals.add("Tutti");
        for (Valutazione v : Valutazione.values())
        {
            // Escludi il valore "nonValutato" definito nell'enum
            if (v == Valutazione.nonValutato)
                continue;
            vals.add(v.name());
        }
        vals.add("Non valutato");
        cbFiltroValutazione = new JComboBox<>(vals.toArray(new String[0]));
        cbFiltroValutazione.setSelectedIndex(0);

        btnResetFiltri = new JButton("↺ Reset Filtri");
        btnResetFiltri.setFont(btnResetFiltri.getFont().deriveFont(Font.BOLD, 14f));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.add(new JLabel("Filtro Stato:"));
        filterPanel.add(cbFiltroStato);
        filterPanel.add(new JLabel("Filtro Valutazione:"));
        filterPanel.add(cbFiltroValutazione);
        filterPanel.add(btnResetFiltri);

        // Pannello comandi
        btnRemove = new JButton("Rimuovi");
        btnSortTitolo = new JButton("Ordina per Titolo");
        btnSearch = new JButton("Cerca Titolo");
        btnAdd = new JButton("Aggiungi");
        btnModify = new JButton("Modifica Stato/Valutazione");
        btnSortAutore = new JButton("Ordina per Autore");

        JPanel row1 = new JPanel(new GridLayout(1, 3, 5, 5));
        row1.add(btnRemove);
        row1.add(btnSortTitolo);
        row1.add(btnSearch);
        JPanel row2 = new JPanel(new GridLayout(1, 3, 5, 5));
        row2.add(btnAdd);
        row2.add(btnModify);
        row2.add(btnSortAutore);

        JPanel actionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        actionPanel.add(row1);
        actionPanel.add(row2);

        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.add(filterPanel, BorderLayout.NORTH);
        south.add(actionPanel, BorderLayout.CENTER);
        frame.add(south, BorderLayout.SOUTH);

        // Event handlers
        cbFiltroStato.addActionListener(e -> applyFilters());
        cbFiltroValutazione.addActionListener(e -> applyFilters());
        btnResetFiltri.addActionListener(e ->
        {
            cbFiltroStato.setSelectedIndex(0);
            cbFiltroValutazione.setSelectedIndex(0);
            applyFilters();
        });
        btnAdd.addActionListener(e -> addBook());
        btnRemove.addActionListener(e -> removeSelected());
        btnModify.addActionListener(e -> modifySelected());
        btnSortTitolo.addActionListener(e -> sortBy(CriterioOrdinamento.TITOLO));
        btnSortAutore.addActionListener(e -> sortBy(CriterioOrdinamento.AUTORE));
        btnSearch.addActionListener(e -> searchByTitle());

        loadAll();
        frame.setVisible(true);
    }

    /**
     * Carica tutti i libri dall'archivio persistente e li memorizza.
     * Popola la tabella con l'elenco completo.
     */
    private void loadAll()
    {
        try
        {
            archivio.caricaLibri();
            allBooks = archivio.getLibri();
            popolaTabella(allBooks);
        }
        catch (IOException e)
        {
            showError(e);
        }
    }

    /**
     * Applica i filtri selezionati di stato e valutazione.
     * Aggiorna la tabella con i risultati filtrati.
     */
    private void applyFilters()
    {
        if (allBooks == null) return;
        String fs = (String) cbFiltroStato.getSelectedItem();
        String fv = (String) cbFiltroValutazione.getSelectedItem();
        List<Libro> filtered = allBooks.stream()
                .filter(l -> "Tutti".equals(fs) || l.getStatoLibro().name().equalsIgnoreCase(normalize(fs)))
                .filter(l ->
                {
                    if ("Tutti".equals(fv)) return true;
                    if ("Non valutato".equals(fv)) return l.getValutazione() == null;
                    return l.getValutazione() != null && l.getValutazione().name().equalsIgnoreCase(fv);
                })
                .toList();
        popolaTabella(filtered);
    }

    /**
     * Mostra un dialog di input per inserire un nuovo libro.
     * Verifica i campi obbligatori (Titolo, Autore, Editore, ISBN).
     */
    private void addBook()
    {
        JTextField tfTit = new JTextField(), tfAut = new JTextField(), tfEd = new JTextField(), tfIsbn = new JTextField();
        JComboBox<String> cbGen = new JComboBox<>(new String[]{"ROMANZO","GIALLO","FANTASY","FANTASCIENZA","STORICO","BIOGRAFIA","SAGGIO","DISTOPIA","HORROR","AVVENTURA","POESIA","CLASSICO","THRILLER","ALTRO"});
        JComboBox<String> cbSt = new JComboBox<>(new String[]{"da leggere","in lettura","letto"});
        JComboBox<String> cbVal = new JComboBox<>(new String[]{"1","2","3","4","5","Non valutato"});
        Object[] prompts = {"Titolo:", tfTit, "Autore:", tfAut, "Editore:", tfEd, "ISBN:", tfIsbn, "Genere:", cbGen, "Stato:", cbSt, "Valutazione:", cbVal};
        if (JOptionPane.showConfirmDialog(frame, prompts, "Aggiungi Libro", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
        {
            if (tfTit.getText().isBlank() || tfAut.getText().isBlank() || tfEd.getText().isBlank() || tfIsbn.getText().isBlank())
            {
                JOptionPane.showMessageDialog(frame, "I campi Titolo, Autore, Editore e ISBN sono obbligatori.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try
            {
                archivio.aggiungiLibro(tfTit.getText(), tfAut.getText(), tfEd.getText(), tfIsbn.getText(), (String) cbGen.getSelectedItem());
                archivio.modificaStato(tfIsbn.getText(), normalize((String) cbSt.getSelectedItem()));
                String v = (String) cbVal.getSelectedItem();
                if (!"Non valutato".equals(v)) archivio.modificaValutazione(tfIsbn.getText(), v);
                loadAll();
            }
            catch (IOException e)
            {
                showError(e);
            }
        }
    }

    /**
     * Rimuove il libro selezionato e aggiorna la tabella.
     */
    private void removeSelected()
    {
        int sel = table.getSelectedRow();
        if (sel < 0) return;
        String isbn = (String) tableModel.getValueAt(sel, 0);
        try
        {
            archivio.rimuoviLibro(isbn);
            loadAll();
        }
        catch (IOException e)
        {
            showError(e);
        }
    }

    /**
     * Permette di modificare stato o valutazione del libro selezionato.
     */
    private void modifySelected()
    {
        int sel = table.getSelectedRow();
        if (sel < 0) return;
        String isbn = (String) tableModel.getValueAt(sel, 0);
        String[] opts = {"Stato", "Valutazione"};
        int ch = JOptionPane.showOptionDialog(frame, "Cosa modificare?", "Modifica", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opts, opts[0]);
        try
        {
            if (ch == 0)
            {
                String ns = (String) JOptionPane.showInputDialog(frame, "Nuovo stato:", "Modifica Stato", JOptionPane.PLAIN_MESSAGE, null, new String[]{"da leggere","in lettura","letto"}, "letto");
                if (ns != null) archivio.modificaStato(isbn, normalize(ns));
            }
            else if (ch == 1)
            {
                String nv = (String) JOptionPane.showInputDialog(frame, "Nuova valutazione:", "Modifica Valutazione", JOptionPane.PLAIN_MESSAGE, null, new String[]{"1","2","3","4","5","Non valutato"}, "3");
                if (nv != null && !"Non valutato".equals(nv)) archivio.modificaValutazione(isbn, nv);
            }
            loadAll();
        }
        catch (IOException e)
        {
            showError(e);
        }
    }

    /**
     * Ordina i libri secondo il criterio specificato e aggiorna la tabella.
     * @param c criterio di ordinamento
     */
    private void sortBy(CriterioOrdinamento c)
    {
        try
        {
            popolaTabella(archivio.ordina(c));
        }
        catch (IOException e)
        {
            showError(e);
        }
    }

    /**
     * Cerca libri per titolo parziale e aggiorna la tabella.
     */
    private void searchByTitle()
    {
        String q = JOptionPane.showInputDialog(frame, "Ricerca titolo (parziale):");
        if (q == null) return;
        try
        {
            popolaTabella(archivio.cerca(q));
        }
        catch (IOException e)
        {
            showError(e);
        }
    }

    /**
     * Popola la JTable con la lista di libri fornita.
     * @param list lista di libri
     */
    private void popolaTabella(List<Libro> list)
    {
        tableModel.setRowCount(0);
        for (Libro l : list)
        {
            tableModel.addRow(new Object[]{l.getIsbn(), l.getTitolo(), l.getAutore(), l.getEditore(), l.getGenere(), l.getStatoLibro(), l.getValutazione()});
        }
    }

    /**
     * Mostra un dialog per errori.
     * @param e eccezione da visualizzare
     */
    private void showError(Exception e)
    {
        JOptionPane.showMessageDialog(frame, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Normalizza stringhe di stato per l'enum (es. "da leggere" -> "DA_LEGGERE").
     * @param s stringa di input
     * @return stringa normalizzata
     */
    private String normalize(String s)
    {
        return s.toUpperCase().replace(' ', '_');
    }
}
