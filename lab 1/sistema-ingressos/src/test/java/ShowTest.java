import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.models.Lote;
import com.ingressos.models.Show;

class ShowTest {

    private Show show;

    @BeforeEach
    void setUp() {
        show = new Show("2024-11-16", "Linkin Park", 1000.0, 2000.0, false);
    }

    @Test
    void testCriarShow() {
        assertEquals("2024-11-16", show.getData());
        assertEquals("Linkin Park", show.getArtista());
        assertEquals(1000.0, show.getCache());
        assertEquals(2000.0, show.getDespesasInfraestrutura());
        assertEquals(false, show.isDataEspecial());
    }

    @Test
    void testCriarShowComDataNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show(null, "Metalica", 50000.0, 20000.0, false);
        });
        assertEquals("A data do show não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testCriarShowComDataVazia() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("   ", "Twenty One Pilots", 50000.0, 20000.0, false);
        });
        assertEquals("A data do show não pode ser nula ou vazia", exception.getMessage());
    }

    @Test
    void testCriarShowComArtistaNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", null, 50000.0, 20000.0, false);
        });
        assertEquals("O nome do artista não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCriarShowComArtistaVazio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "   ", 50000.0, 20000.0, false);
        });
        assertEquals("O nome do artista não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testCriarShowComCacheNulo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Imagine Dragons", null, 20000.0, false);
        });
        assertEquals("O cache deve ser um valor positivo", exception.getMessage());
    }

    @Test
    void testCriarShowComCacheNegativo() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Coldplay", -10000.0, 20000.0, false);
        });
        assertEquals("O cache deve ser um valor positivo", exception.getMessage());
    }

    @Test
    void testCriarShowComDespesasInfraestruturaNula() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Cage The Elephant", 50000.0, null, false);
        });
        assertEquals("As despesas de infraestrutura não podem ser nulas ou negativas", exception.getMessage());
    }

    @Test
    void testCriarShowComDespesasInfraestruturaNegativas() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Show("2024-12-15", "Breaking Benjamin", 50000.0, -5000.0, false);
        });
        assertEquals("As despesas de infraestrutura não podem ser nulas ou negativas", exception.getMessage());
    }

    @Test
    void testAdicionarLote() {
        Lote lote = new Lote(500, 0.2, 500.0, 0.15);
        show.adicionarLote(lote);

        assertEquals(1, show.getLotes().size());
        assertTrue(show.getLotes().contains(lote));
    }

    @Test
    void testGerarRelatorio() {
        Relatorio relatorio = show.gerarRelatorio();
        assertEquals(0, relatorio.getIngressosVipVendidos());
        assertEquals(0, relatorio.getIngressosMeiaVendidos());
        assertEquals(0, relatorio.getIngressosNormaisVendidos());
        assertEquals(-3300, relatorio.getReceitaLiquida());
        assertEquals(StatusFinanceiro.PREJUÍZO, relatorio.getStatusFinanceiro());
    }
}