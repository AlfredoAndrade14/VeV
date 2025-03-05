package junit5Tests.funcionalTests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.Ingresso;
import com.ingressos.models.Lote;

@DisplayName("Testes Funcionais de Lote utilizando AVL")
class LoteTestAVL {

    @ParameterizedTest
    @DisplayName("Teste de Distribuição de VIP - Valores Inválidos")
    @ValueSource(doubles = {0.19, 0.31})
    void testDistribuicaoVIP_Invalido(double percentualVIP) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Lote(100, percentualVIP, 50.00, 0.25));
        assertEquals("O percentual de ingressos VIP deve estar entre 20% e 30%", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Teste de Distribuição de VIP - Valores Válidos")
    @MethodSource("validVipDistributionProvider")
    void testDistribuicaoVIP_Valido(double percentualVIP, int esperadoVIP, int esperadoMeia, int esperadoNormal) {
        Lote lote = new Lote(100, percentualVIP, 50.00, 0.25);
        List<Ingresso> ingressos = lote.getIngressos();
        
        assertEquals(esperadoVIP, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count());
        assertEquals(esperadoMeia, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count());
        assertEquals(esperadoNormal, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count());
    }

    static Stream<Arguments> validVipDistributionProvider() {
        return Stream.of(
            Arguments.of(0.20, 20, 10, 70),
            Arguments.of(0.21, 21, 10, 69),
            Arguments.of(0.25, 25, 10, 65),
            Arguments.of(0.29, 29, 10, 61),
            Arguments.of(0.30, 30, 10, 60)
        );
    }

    @ParameterizedTest
    @DisplayName("Teste de Desconto - Valores Inválidos")
    @ValueSource(doubles = {-0.01, 0.26})
    void testDesconto_Invalido(double desconto) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Lote(100, 0.20, 100.0, desconto));
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }

    @ParameterizedTest
    @DisplayName("Teste de Desconto - Valores Válidos")
    @MethodSource("validDiscountProvider")
    void testDesconto_Valido(double desconto, double precoFinal) {
        Lote lote = new Lote(100, 0.20, 50.0, desconto);
        Ingresso ingresso = lote.getIngressos().stream().filter(i -> i.getTipo() == TipoIngresso.VIP).findFirst().orElseThrow();
        assertEquals(precoFinal, ingresso.getPrecoFinal(), 0.001);
    }

    static Stream<Arguments> validDiscountProvider() {
        return Stream.of(
            Arguments.of(0.0, 100.0),
            Arguments.of(0.01, 99.0),
            Arguments.of(0.20, 80.0),
            Arguments.of(0.24, 76.0),
            Arguments.of(0.25, 75.0)
        );
    }
}
