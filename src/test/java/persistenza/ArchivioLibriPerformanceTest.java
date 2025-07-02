package persistenza;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.io.IOException;
import java.time.Duration;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@TestInstance(Lifecycle.PER_CLASS)
class ArchivioLibriPerformanceTest {

    private ArchivioLibri archivio = new ArchivioLibriFileCsv();

    /**
     * Definisce i vari scenari di performance:
     *  - N: numero di record
     *  - timeoutSec: tempo massimo in secondi
     */
    Stream<Arguments> performanceScenarios() {
        return Stream.of(
                arguments(200, 1),    // base: 200 cicli di aggiunta+svuota in <1s
                arguments(1000, 5),   // carico medio: 1000 record + ordinamento in <5s
                arguments(5000, 30),  // carico elevato: 5000 record + ordina+filtra+svuota in <30s
                arguments(10000, 120) // stress: 10000 record senza svuota intermedia in <120s
        );
    }

    @ParameterizedTest(name = "Performance test: {0} operations within {1}s")
    @MethodSource("performanceScenarios")
    void testPerformance(int n, int timeoutSec) throws IOException {
        assertTimeout(Duration.ofSeconds(timeoutSec), () -> {
            // aggiunta di N libri
            for (int i = 0; i < n; i++) {
                String isbn = String.format("%013d", 9800000000000L + i);
                archivio.aggiungiLibro(
                        "Book" + i,
                        "Author" + (i % 50),
                        "Editor",
                        isbn,
                        (i % 2 == 0 ? "DISTOPIA" : "FANTASY")
                );
            }
            // operazioni successive
            archivio.ordina(CriterioOrdinamento.TITOLO);
            archivio.ordina(CriterioOrdinamento.AUTORE);
            archivio.filtra(CriterioFiltro.GENERE, "DISTOPIA");
            archivio.filtra(CriterioFiltro.STATO, "NON_LETTO");
            // svuota alla fine solo per tutti gli scenari
            archivio.svuota();
        }, n + " operations must complete within " + timeoutSec + "s");
    }
}
