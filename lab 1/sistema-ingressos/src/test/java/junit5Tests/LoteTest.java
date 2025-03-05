package junit5Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.Ingresso;
import com.ingressos.models.Lote;

@DisplayName("Testes para Lote")
class LoteTest {

    private Lote lote;

    @BeforeEach
    void setUp() {
        lote = new Lote(100, 0.2, 50.0, 0.1);
    }

    @Test
    @DisplayName("Deve criar lote com a distribuição correta de ingressos")
    void testCriarLote() {
        List<Ingresso> ingressos = lote.getIngressos();

        long vipCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count();
        long meiaCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count();
        long normalCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count();

        assertAll("Valida distribuição de ingressos",
                () -> assertEquals(20, vipCount),
                () -> assertEquals(10, meiaCount),
                () -> assertEquals(70, normalCount));
    }

    @Nested
    @DisplayName("Validações de criação do lote")
    class CriacaoLoteTests {

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = { -10, 0 })
        @DisplayName("Deve lançar exceção com quantidade inválida")
        void testCriarLoteComQuantidadeInvalida(Integer quantidade) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Lote(quantidade, 0.2, 50.0, 0.1));
            assertEquals("A quantidade de ingressos deve ser um número positivo", exception.getMessage());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(doubles = { 0.15, 0.35 })
        @DisplayName("Deve lançar exceção com percentual VIP inválido")
        void testCriarLoteComPercentualVipInvalido(Double percentualVip) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Lote(100, percentualVip, 50.0, 0.1));
            assertEquals("O percentual de ingressos VIP deve estar entre 20% e 30%", exception.getMessage());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(doubles = { 0.0, -10.0 })
        @DisplayName("Deve lançar exceção com preço normal inválido")
        void testCriarLoteComPrecoNormalInvalido(Double precoNormal) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Lote(100, 0.2, precoNormal, 0.1));
            assertEquals("O preço normal deve ser um número positivo", exception.getMessage());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(doubles = { -0.1, 0.3 })
        @DisplayName("Deve lançar exceção com desconto inválido")
        void testCriarLoteComDescontoInvalido(Double desconto) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> new Lote(100, 0.2, 50.0, desconto));
            assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
        }

    }

    @Test
    @DisplayName("Deve criar lote com desconto válido aplicando nos ingressos")
    void testCriacaoLoteComDescontoValido() {
        Lote loteDesconto = new Lote(2, 0.3, 100.0, 0.15);
        for (Ingresso ingresso : loteDesconto.getIngressos()) {
            if (ingresso.getTipo() == TipoIngresso.VIP || ingresso.getTipo() == TipoIngresso.NORMAL) {
                double precoEsperado = ingresso.getTipo() == TipoIngresso.VIP ? 200.0 * 0.85 : 100.0 * 0.85;
                assertEquals(precoEsperado, ingresso.getPrecoFinal());
            } else {
                assertEquals(50.0, ingresso.getPrecoFinal());
            }
        }
    }

    @Nested
    @DisplayName("Testes de venda de ingressos")
    class VendaIngressosTests {
        @Test
        @DisplayName("Deve vender ingressos normalmente")
        void testVenderIngressosValidos() {
            int vendidos = lote.venderIngressos(TipoIngresso.NORMAL, 10);
            assertEquals(10, vendidos);
            long normalCount = lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count();
            assertEquals(60, normalCount);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar vender ingressos acima do disponível")
        void testVenderIngressosAcimaDoDisponivel() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                lote.venderIngressos(TipoIngresso.VIP, 25);
            });
            assertEquals("Não há ingressos suficientes do tipo VIP para vender", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para tipo de ingresso inválido")
        void testVenderIngressosTipoInexistente() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                lote.venderIngressos(null, 5);
            });
            assertEquals("Tipo de ingresso inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao vender quantidade zero de ingressos")
        void testVenderIngressosQuantidadeZero() {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                lote.venderIngressos(TipoIngresso.MEIA_ENTRADA, 0);
            });
            assertEquals("A quantidade solicitada deve ser maior que zero", exception.getMessage());
        }

        @Test
        @DisplayName("Deve vender todos os ingressos disponíveis de um tipo")
        void testVenderIngressosTodosDisponiveis() {
            int vendidos = lote.venderIngressos(TipoIngresso.VIP, 20);
            assertEquals(20, vendidos);
            long vipCount = lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count();
            assertEquals(0, vipCount);
        }
    }
}
