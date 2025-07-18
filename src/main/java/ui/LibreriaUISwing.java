package ui;

import model.Valutazione;
import model.Libro;
import persistenza.CriterioOrdinamento;
import persistenza.LibroGiaPresenteException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione Swing dell'interfaccia utente per la gestione
 * della libreria personale.
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
    private JButton btnAdd;
    private JButton btnRemove;
    private JButton btnModify;
    private JButton btnSortTitolo;
    private JButton btnSortAutore;
    private JButton btnSearch;

    /**
     * Avvia la finestra principale e inizializza tutti i componenti UI.
     */
    @Override
    public void avvia()
    {
        // Configurazione finestra principale
        frame = new JFrame("Gestione Libreria");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(5, 5));

        // Modello e tabella per la visualizzazione dei libri
        tableModel = new DefaultTableModel(
                new Object[]{"ISBN", "Titolo", "Autori", "Editore", "Genere", "Stato", "Valutazione"},
                0
        )
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false; // rendi le celle non editabili direttamente
            }
        };
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // ComboBox per i filtri stato e valutazione
        cbFiltroStato = new JComboBox<>(new String[]{"Tutti", "NON_LETTO", "IN_LETTURA", "LETTO"});
        List<String> vals = new ArrayList<>();
        vals.add("Tutti");
        for (Valutazione v : Valutazione.values())
        {
            if (v != Valutazione.nonValutato)
            {
                vals.add(v.name());
            }
        }
        vals.add("Non valutato");
        cbFiltroValutazione = new JComboBox<>(vals.toArray(new String[0]));
        cbFiltroValutazione.setSelectedIndex(0);

        btnResetFiltri = new JButton("↺ Reset");
        btnResetFiltri.setFont(btnResetFiltri.getFont().deriveFont(Font.BOLD, 14f));

        // Pannello filtri
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.add(new JLabel("Filtro Stato:"));
        filterPanel.add(cbFiltroStato);
        filterPanel.add(new JLabel("Filtro Valutazione:"));
        filterPanel.add(cbFiltroValutazione);
        filterPanel.add(btnResetFiltri);

        // Bottoni di azione
        btnRemove     = new JButton("Rimuovi");
        btnSortTitolo = new JButton("Ordina per Titolo");
        btnSearch     = new JButton("Cerca Titolo");
        btnAdd        = new JButton("Aggiungi");
        btnModify     = new JButton("Modifica Stato/Valutazione");
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

        // Posizionamento pannelli e registrazione listener
        JPanel south = new JPanel(new BorderLayout(5, 5));
        south.add(filterPanel, BorderLayout.NORTH);
        south.add(actionPanel, BorderLayout.CENTER);
        frame.add(south, BorderLayout.SOUTH);

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

        // Carica dati iniziali e mostra finestra
        loadAll();
        frame.setVisible(true);
    }

    /**
     * Carica tutti i libri dall'archivio e popola la tabella.
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
     * Applica i filtri selezionati (stato e valutazione) alla lista di libri.
     */
    private void applyFilters()
    {
        if (allBooks == null)
        {
            return;
        }

        String fs = (String) cbFiltroStato.getSelectedItem();
        String fv = (String) cbFiltroValutazione.getSelectedItem();

        List<Libro> filtered = allBooks.stream()
                .filter(l -> "Tutti".equals(fs) || l.getStatoLibro().name().equalsIgnoreCase(fs))
                .filter(l ->
                {
                    if ("Tutti".equals(fv))
                    {
                        return true;
                    }
                    if ("Non valutato".equals(fv))
                    {
                        return l.getValutazione() == null
                                || l.getValutazione() == Valutazione.nonValutato;
                    }
                    return l.getValutazione() != null
                            && l.getValutazione().name().equalsIgnoreCase(fv);
                })
                .toList();

        popolaTabella(filtered);
    }

    /**
     * Mostra un dialog per aggiungere un nuovo libro e
     * gestisce validazioni e inserimento nell'archivio.
     */
    private void addBook()
    {
        JTextField tfTit  = new JTextField();
        JTextField tfAut  = new JTextField();
        JTextField tfEd   = new JTextField();
        JTextField tfIsbn = new JTextField();

        JComboBox<String> cbGen = new JComboBox<>(
                new String[]{
                        "ROMANZO", "GIALLO", "FANTASY", "FANTASCIENZA", "STORICO",
                        "BIOGRAFIA", "SAGGIO", "DISTOPIA", "HORROR", "AVVENTURA",
                        "POESIA", "CLASSICO", "THRILLER", "ALTRO"
                }
        );

        JComboBox<String> cbSt = new JComboBox<>(
                new String[]{"NON_LETTO", "IN_LETTURA", "LETTO"}
        );

        JComboBox<String> cbVal = new JComboBox<>(
                new String[]{"1", "2", "3", "4", "5", "Non valutato"}
        );

        Object[] prompts = {
                "Titolo:", tfTit,
                "Autori (separare più autori con virgola):", tfAut,
                "Editore:", tfEd,
                "ISBN:", tfIsbn,
                "Genere:", cbGen,
                "Stato:", cbSt,
                "Valutazione:", cbVal
        };

        if (JOptionPane.showConfirmDialog(
                frame, prompts, "Aggiungi Libro", JOptionPane.OK_CANCEL_OPTION
        ) == JOptionPane.OK_OPTION)
        {
            // Controlla campi obbligatori
            if (tfTit.getText().isBlank()
                    || tfAut.getText().isBlank()
                    || tfEd.getText().isBlank()
                    || tfIsbn.getText().isBlank())
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "I campi Titolo, Autori, Editore e ISBN sono obbligatori.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String isbnInput = tfIsbn.getText().trim();

            // Validazione basic ISBN
            if (!isValidIsbn(isbnInput))
            {
                JOptionPane.showMessageDialog(
                        frame,
                        "ISBN non valido: deve contenere 10 o 13 cifre numeriche.",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try
            {
                archivio.aggiungiLibro(
                        tfTit.getText(),
                        tfAut.getText(),
                        tfEd.getText(),
                        isbnInput,
                        (String) cbGen.getSelectedItem()
                );

                archivio.modificaStato(isbnInput, (String) cbSt.getSelectedItem());

                String v = (String) cbVal.getSelectedItem();
                if (!"Non valutato".equals(v))
                {
                    archivio.modificaValutazione(isbnInput, v);
                }

                loadAll();
            }
            catch (LibroGiaPresenteException | IllegalArgumentException ex)
            {
                // ISBN duplicato o input non valido
                JOptionPane.showMessageDialog(
                        frame,
                        ex.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            catch (IOException ex)
            {
                showError(ex);
            }
        }
    }

    /**
     * Rimuove il libro selezionato dalla tabella e dall'archivio.
     */
    private void removeSelected()
    {
        int sel = table.getSelectedRow();
        if (sel < 0)
        {
            return;
        }

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
     * Mostra dialog per modificare stato o valutazione del libro selezionato.
     */
    private void modifySelected()
    {
        int sel = table.getSelectedRow();
        if (sel < 0)
        {
            return;
        }

        String isbn = (String) tableModel.getValueAt(sel, 0);
        String[] opts = {"Stato", "Valutazione"};

        int ch = JOptionPane.showOptionDialog(
                frame,
                "Cosa modificare?",
                "Modifica",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opts,
                opts[0]
        );

        try
        {
            if (ch == 0)
            {
                // Modifica dello stato di lettura
                String ns = (String) JOptionPane.showInputDialog(
                        frame,
                        "Nuovo stato:",
                        "Modifica Stato",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new String[]{"NON_LETTO", "IN_LETTURA", "LETTO"},
                        "LETTO"
                );
                if (ns != null)
                {
                    archivio.modificaStato(isbn, ns);
                }
            }
            else if (ch == 1)
            {
                // Modifica della valutazione
                String nv = (String) JOptionPane.showInputDialog(
                        frame,
                        "Nuova valutazione:",
                        "Modifica Valutazione",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        new String[]{"1", "2", "3", "4", "5", "Non valutato"},
                        "3"
                );
                if (nv != null )
                {
                    archivio.modificaValutazione(isbn, nv);
                }
            }

            loadAll();
        }
        catch (IOException e)
        {
            showError(e);
        }
    }

    /**
     * Richiama l'ordinamento sull'archivio e aggiorna la tabella.
     *
     * @param c criterio di ordinamento (AUTORE o TITOLO)
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
     * Apre un dialog per la ricerca per titolo e filtra i risultati.
     */
    private void searchByTitle()
    {
        String q = JOptionPane.showInputDialog(frame, "Ricerca titolo :");
        if (q == null)
        {
            return;
        }
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
     * Popola la tabella con la lista di libri fornita.
     *
     * @param list lista di {@link Libro}
     */
    private void popolaTabella(List<Libro> list)
    {
        tableModel.setRowCount(0); // svuota prima di riempire
        for (Libro l : list)
        {
            String autoriStr = String.join(", ", l.getAutori());
            tableModel.addRow(new Object[]{
                    l.getIsbn(),
                    l.getTitolo(),
                    autoriStr,
                    l.getEditore(),
                    l.getGenere(),
                    l.getStatoLibro(),
                    l.getValutazione()
            });
        }
    }

    /**
     * Mostra un dialog di errore con il messaggio dell'eccezione.
     *
     * @param e eccezione da visualizzare
     */
    private void showError(Exception e)
    {
        JOptionPane.showMessageDialog(
                frame,
                e.getMessage(),
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /**
     * Verifica che l'ISBN contenga esattamente 10 o 13 cifre numeriche.
     *
     * @param isbn stringa da validare
     * @return true se valida, false altrimenti
     */
    private boolean isValidIsbn(String isbn)
    {
        return isbn.matches("\\d{10}|\\d{13}");
    }

    /**
     * Normalizza una stringa convertendo in maiuscolo e sostituendo spazi con underscore.
     *
     * @param s stringa di input
     * @return stringa normalizzata
     */
    private String normalize(String s)
    {
        return s.toUpperCase().replace(' ', '_');
    }
}
