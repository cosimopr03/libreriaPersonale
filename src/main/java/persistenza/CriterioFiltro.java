package persistenza;
public enum CriterioFiltro
{
    /**
     * Tiene solo i libri il cui stato corrisponde al parametro1.
     */
    STATO,
    /**
     * Tiene solo i libri il cui genere corrisponde al parametro1.
     */
    GENERE,
    /**
     * Tiene solo i libri che soddisfano sia il genere (parametro1) sia lo stato (parametro2).
     */
    GENERE_E_STATO
}