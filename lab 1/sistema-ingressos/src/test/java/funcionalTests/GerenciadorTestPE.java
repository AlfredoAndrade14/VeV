package funcionalTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.service.Gerenciador;
import com.ingressos.models.Show;
import com.ingressos.models.Lote;
import com.ingressos.enums.TipoIngresso;

public class GerenciadorTestPE {

    private Gerenciador gerenciador;

    @BeforeEach
    public void setUp() {
        gerenciador = new Gerenciador();
    }

    // CT-PE-01
    @Test
    public void testCriarShowValido() {
        Show show = gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, true);
        assertNotNull(show);
        assertEquals("Artista A", show.getArtista());
        assertEquals("2025-12-25", show.getData());
    }

    // CT-PE-02
    @Test
    public void testCriarShowComArtistaNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow(null, "2025-12-25", 2000.0, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    // CT-PE-03
    @Test
    public void testCriarShowComDataNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", null, 2000.0, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    // CT-PE-04
    @Test
    public void testCriarShowComDespesaNegativa() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", -100.0, 1000.0, false);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    // CT-PE-05
    @Test
    public void testCriarShowComCacheNaoPositivo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, -30.00, false);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    // CT-PE-06
    @Test
    public void testCriarShowDuplicado() {
        gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Artista A", "2025-12-25", 2000.0, 1000.0, false);
        });
        assertEquals("O show já está registrado no sistema.", exception.getMessage());
    }

    // CT-PE-07
    @Test
    public void testVendaIngressosValida() {
        gerenciador.criarShow("Artista B", "2025-11-10", 1500.0, 800.0, false);
        Lote lote = new Lote(100, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista B", "2025-11-10", lote);

        assertDoesNotThrow(() -> {
            gerenciador.venderIngressos("Artista B", "2025-11-10", TipoIngresso.NORMAL, 30);
        });
        long vendidos = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL && i.isVendido())
                .count();
        assertEquals(30, vendidos);
    }

    // CT-PE-08
    @Test
    public void testVendaIngressosQuantidadeZero() {
        gerenciador.criarShow("Artista C", "2025-10-05", 1500.0, 800.0, false);
        Lote lote = new Lote(100, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista C", "2025-10-05", lote);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("Artista C", "2025-10-05", TipoIngresso.NORMAL, 0);
        });
        assertTrue(exception.getMessage().contains("maior que zero"));
    }

    // CT-PE-09
    @Test
    public void testVendaIngressosQuantidadeMaiorQueDisponivel() {
        gerenciador.criarShow("Artista D", "2025-09-15", 1500.0, 800.0, false);
        Lote lote = new Lote(50, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista D", "2025-09-15", lote);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("Artista D", "2025-09-15", TipoIngresso.NORMAL, 60);
        });
        assertTrue(exception.getMessage().contains("Não há ingressos suficientes"));
    }

    // CT-PE-10
    @Test
    public void testVendaIngressosTipoNulo() {
        gerenciador.criarShow("Artista E", "2025-08-20", 1500.0, 800.0, false);
        Lote lote = new Lote(50, 0.25, 10.0, 0.10);
        gerenciador.adicionarLoteAoShow("Artista E", "2025-08-20", lote);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("Artista E", "2025-08-20", null, 10);
        });
        assertTrue(exception.getMessage().contains("Tipo de ingresso inválido"));
    }
}
