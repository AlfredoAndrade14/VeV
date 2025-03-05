package junit5Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.*;
import com.ingressos.service.Gerenciador;

@DisplayName("Testes para Gerenciador de Shows")
class GerenciadorTest {

    private Gerenciador gerenciador;

    @BeforeEach
    void setUp() {
        gerenciador = new Gerenciador();
    }

    @Test
    @DisplayName("Deve criar um show com sucesso")
    void testCriarShow() {
        gerenciador.criarShow("Linkin Park", "2024-11-16", 1000.0, 2000.0, true);

        Show show = gerenciador.buscarShow("Linkin Park", "2024-11-16");
        assertAll("Valida dados do show criado",
                () -> assertEquals("Linkin Park", show.getArtista()),
                () -> assertEquals("2024-11-16", show.getData()),
                () -> assertEquals(1000.0, show.getDespesasInfraestrutura()),
                () -> assertEquals(2000.0, show.getCache()),
                () -> assertTrue(show.isDataEspecial()));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar criar show já existente")
    void testCriarShowComShowExistente() {
        gerenciador.criarShow("Linkin Park", "2024-11-16", 1000.0, 2000.0, true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow("Linkin Park", "2024-11-16", 1000.0, 2000.0, false);
        });
        assertEquals("O show já está registrado no sistema.", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Falha ao criar show com dados inválidos")
    @NullSource
    @ValueSource(strings = { "", "  " })
    void testCriarShowComDadosInvalidos(String artista) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.criarShow(artista, "2024-11-16", -1000.0, 2000.0, true);
        });
        assertEquals("Dados inválidos para criar o show", exception.getMessage());
    }

    @Test
    @DisplayName("Deve remover show com sucesso")
    void testRemoverShow() {
        gerenciador.criarShow("Metallica", "2024-12-15", 1500.0, 3000.0, false);
        List<Show> lista = gerenciador.listarShows();
        assertEquals(1, lista.size());

        Show show = gerenciador.buscarShow("Metallica", "2024-12-15");
        gerenciador.removerShow(show);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.buscarShow("Metallica", "2024-12-15");
        });
        assertEquals("Nenhum show encontrado para o artista Metallica na data 2024-12-15", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao remover show não registrado")
    void testRemoverShowNaoExistente() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.removerShow(new Show("Coldplay", "2024-12-31", 2000.0, 5000.0, false));
        });
        assertEquals("O show não está registrado no sistema.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve adicionar lote ao show com sucesso")
    void testAdicionarLoteAoShow() {
        gerenciador.criarShow("Imagine Dragons", "2024-12-01", 1000.0, 3000.0, false);
        Show show = gerenciador.buscarShow("Imagine Dragons", "2024-12-01");

        Lote lote = new Lote(500, 0.25, 120.00, 0.1);
        gerenciador.adicionarLoteAoShow("Imagine Dragons", "2024-12-01", lote);

        assertAll("Valida lote adicionado",
                () -> assertTrue(show.getLotes().contains(lote)),
                () -> assertEquals(1, show.getLotes().size()));
    }

    @Test
    @DisplayName("Deve lançar exceção ao vender ingressos com quantidade insuficiente")
    void testVenderIngressosComQuantidadeInsuficiente() {
        gerenciador.criarShow("The Rolling Stones", "2024-12-20", 2000.0, 4000.0, false);
        Show show = gerenciador.buscarShow("The Rolling Stones", "2024-12-20");

        Lote lote = new Lote(100, 0.2, 100.00, 0.1);
        show.adicionarLote(lote);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.venderIngressos("The Rolling Stones", "2024-12-20", TipoIngresso.VIP, 200);
        });
        assertEquals(
                "Não há ingressos suficientes do tipo VIP disponíveis para o show de The Rolling Stones em 2024-12-20",
                exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Falha ao buscar show com artista inválido")
    @NullSource
    @ValueSource(strings = { "", "  " })
    void testBuscarShowComArtistaInvalido(String artista) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.buscarShow(artista, "2024-12-31");
        });
        assertEquals("O artista não pode ser nulo ou vazio", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Falha ao buscar show com data inválida")
    @NullSource
    @ValueSource(strings = { "", "  " })
    void testBuscarShowComDataInvalida(String data) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gerenciador.buscarShow("The Beatles", data);
        });
        assertEquals("A data não pode ser nula ou vazia", exception.getMessage());
    }
}
