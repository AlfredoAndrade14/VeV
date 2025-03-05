package junit5Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.Ingresso;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes para Ingresso")
class IngressoTest {

    private Ingresso ingressoVip;
    private Ingresso ingressoNormal;
    private Ingresso ingressoMeiaEntrada;

    @BeforeEach
    void setUp() {
        ingressoVip = new Ingresso(TipoIngresso.VIP, 200.00, 0.0);
        ingressoNormal = new Ingresso(TipoIngresso.NORMAL, 100.00, 0.0);
        ingressoMeiaEntrada = new Ingresso(TipoIngresso.MEIA_ENTRADA, 50.00, 0.0);
    }

    @Test
    @DisplayName("Deve criar ingressos corretamente")
    void testCriarIngresso() {
        assertAll("Valida criação de ingressos",
            () -> {
                assertEquals(TipoIngresso.VIP, ingressoVip.getTipo());
                assertFalse(ingressoVip.isVendido());
            },
            () -> {
                assertEquals(TipoIngresso.NORMAL, ingressoNormal.getTipo());
                assertFalse(ingressoNormal.isVendido());
            },
            () -> {
                assertEquals(TipoIngresso.MEIA_ENTRADA, ingressoMeiaEntrada.getTipo());
                assertFalse(ingressoMeiaEntrada.isVendido());
            }
        );
    }

    @ParameterizedTest
    @DisplayName("Deve lançar exceção ao criar ingresso com tipo nulo")
    @NullSource
    void testCriarIngressoComTipoNulo(TipoIngresso tipoIngresso) {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new Ingresso(tipoIngresso, 200.00, 0.0);
        });
        assertEquals("O tipo do ingresso não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Deve marcar ingresso como vendido")
    void testSetVendido() {
        assertFalse(ingressoVip.isVendido());
        ingressoVip.vender();
        assertTrue(ingressoVip.isVendido());
    }

    @Test
    @DisplayName("Deve disponibilizar ingresso vendido")
    void testDisponibilizar() {
        ingressoMeiaEntrada.vender();
        assertTrue(ingressoMeiaEntrada.isVendido());
        ingressoMeiaEntrada.disponibilizar();
        assertFalse(ingressoMeiaEntrada.isVendido());
    }

    @Test
    @DisplayName("Não deve permitir vender ingresso já vendido")
    void testNaoPermitirVenderIngressosJaVendidos() {
        ingressoNormal.vender();
        IllegalStateException exception = assertThrows(IllegalStateException.class, ingressoNormal::vender);
        assertEquals("O ingresso já foi vendido", exception.getMessage());
    }

    @Test
    @DisplayName("Não deve permitir disponibilizar ingresso já disponível")
    void testNaoPermitirDisponibilizarIngressosJaDisponiveis() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, ingressoVip::disponibilizar);
        assertEquals("O ingresso já está disponível", exception.getMessage());
    }
}
