import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.*;
import com.ingressos.service.Gerenciador;

class GerenciadorTest {

    private Gerenciador gerenciador;

    @BeforeEach
    void setUp() {
        gerenciador = new Gerenciador();
    }

    @Test
    void testCriarShow() {
        gerenciador.criarShow("Linkin Park", "2024-11-16", 1000.0, 2000.0, true);

        Show show = gerenciador.buscarShow("Linkin Park", "2024-11-16");
        assertEquals("Linkin Park", show.getArtista());
        assertEquals("2024-11-16", show.getData());
        assertEquals(1000.0, show.getDespesasInfraestrutura());
        assertEquals(2000.0, show.getCache());
        assertTrue(show.isDataEspecial());
    }

    @Test
    void testCriarShowComShowExistente() {
        gerenciador.criarShow("Linkin Park", "2024-11-16", 1000.0, 2000.0, true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Linkin Park", "2024-11-16", 1000.0, 2000.0, false);
        });

        assertEquals("O show já está registrado no sistema.", exception.getMessage());
    }

    @Test
    void testCriarShowComDadosInvalidos() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow(null, "2024-11-16", -1000.0, 2000.0, true);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    @Test
    void testRemoverShow() {
        gerenciador.criarShow("Metallica", "2024-12-15", 1500.0, 3000.0, false);
        List<Show> lista = gerenciador.listarShows();

        assertEquals(1, lista.size());

        Show show = gerenciador.buscarShow("Metallica", "2024-12-15");

        gerenciador.removerShow(show);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.buscarShow("Metallica", "2024-12-15");
        });

        assertEquals("Nenhum show encontrado para o artista Metallica na data 2024-12-15", exception.getMessage());
    }

    @Test
    void testRemoverShowNaoExistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.removerShow(new Show("Coldplay", "2024-12-31", 2000.0, 5000.0, false));
        });

        assertEquals("O show não está registrado no sistema.", exception.getMessage());
    }

    @Test
    void testAdicionarLoteAoShow() {
        gerenciador.criarShow("Imagine Dragons", "2024-12-01", 1000.0, 3000.0, false);
        Show show = gerenciador.buscarShow("Imagine Dragons", "2024-12-01");

        Lote lote = new Lote(500, 0.25, 120.00, 0.1);
        gerenciador.adicionarLoteAoShow("Imagine Dragons", "2024-12-01", lote);

        assertTrue(show.getLotes().contains(lote));
        assertEquals(1, show.getLotes().size());
    }


    @Test
    void testVenderIngressosComQuantidadeInsuficiente() {
        gerenciador.criarShow("The Rolling Stones", "2024-12-20", 2000.0, 4000.0, false);
        Show show = gerenciador.buscarShow("The Rolling Stones", "2024-12-20");

        Lote lote = new Lote(100, 0.2, 100.00, 0.1);
        show.adicionarLote(lote);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("The Rolling Stones", "2024-12-20", TipoIngresso.VIP, 200);
        });

        assertEquals("Não há ingressos suficientes do tipo VIP disponíveis para o show de The Rolling Stones em 2024-12-20", exception.getMessage());
    }

    @Test
    void testBuscarShowComArtistaInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.buscarShow("", "2024-12-31");
        });
        assertEquals("O artista não pode ser nulo ou vazio", exception.getMessage());
    }

    @Test
    void testBuscarShowComDataInvalida() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.buscarShow("The Beatles", "");
        });
        assertEquals("A data não pode ser nula ou vazia", exception.getMessage());
    }
}
