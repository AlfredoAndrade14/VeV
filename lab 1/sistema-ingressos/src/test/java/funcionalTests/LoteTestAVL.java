package funcionalTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.ingressos.enums.TipoIngresso;
import com.ingressos.models.Ingresso;
import com.ingressos.models.Lote;

class LoteTestAVL {

    // CT-AL-01: %VIP = 19, %MEIA = 10 → ERRO
    @Test
    void testDistribuicaoVIP_CT_AL_01() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.19, 50.00, 0.25);
        });
        assertEquals("O percentual de ingressos VIP deve estar entre 20% e 30%", exception.getMessage());
    }

    // CT-AL-02: %VIP = 20, %MEIA = 10 → VÁLIDO
    @Test
    void testDistribuicaoVIP_CT_AL_02() {
        Lote lote = new Lote(100, 0.20, 50.00, 0.25);
        List<Ingresso> ingressos = lote.getIngressos();
        long vipCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count();
        long meiaCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count();
        long normalCount = ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count();

        assertEquals(20, vipCount);
        assertEquals(10, meiaCount);
        assertEquals(70, normalCount);
    }

    // CT-AL-03: %VIP = 21, %MEIA = 10 → VÁLIDO
    @Test
    void testDistribuicaoVIP_CT_AL_03() {
        Lote lote = new Lote(100, 0.21, 50.00, 0.25);
        List<Ingresso> ingressos = lote.getIngressos();

        assertEquals(21, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count());
        assertEquals(10, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count());
        assertEquals(69, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count());
    }

    // CT-AL-04: %VIP = 25, %MEIA = 10 → VÁLIDO
    @Test
    void testDistribuicaoVIP_CT_AL_04() {
        Lote lote = new Lote(100, 0.25, 50.00, 0.25);
        List<Ingresso> ingressos = lote.getIngressos();

        assertEquals(25, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count());
        assertEquals(10, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count());
        assertEquals(65, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count());
    }

    // CT-AL-05: %VIP = 29, %MEIA = 10 → VÁLIDO
    @Test
    void testDistribuicaoVIP_CT_AL_05() {
        Lote lote = new Lote(100, 0.29, 50.00, 0.25);
        List<Ingresso> ingressos = lote.getIngressos();

        assertEquals(29, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count());
        assertEquals(10, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count());
        assertEquals(61, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count());
    }

    // CT-AL-06: %VIP = 30, %MEIA = 10 → VÁLIDO
    @Test
    void testDistribuicaoVIP_CT_AL_06() {
        Lote lote = new Lote(100, 0.30, 50.00, 0.25);
        List<Ingresso> ingressos = lote.getIngressos();

        assertEquals(30, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.VIP).count());
        assertEquals(10, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.MEIA_ENTRADA).count());
        assertEquals(60, ingressos.stream().filter(i -> i.getTipo() == TipoIngresso.NORMAL).count());
    }

    // CT-AL-07: %VIP = 31, %MEIA = 10 → ERRO
    @Test
    void testDistribuicaoVIP_CT_AL_07() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.31, 50.00, 0.25);
        });
        assertEquals("O percentual de ingressos VIP deve estar entre 20% e 30%", exception.getMessage());
    }

    // CT-AL-11: %DESCONTO = -1, Valor do Ingresso = 100, Tipo VIP → ERRO
    @Test
    void testDescontoVIP_CT_AL_08() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.20, 50.0, -0.01);
        });
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }

    // CT-AL-12: %DESCONTO = 0, Valor do Ingresso = 100, Tipo VIP → VÁLIDO
    @Test
    void testDescontoVIP_CT_AL_09() {
        Lote lote = new Lote(100, 0.20, 50.0, 0.0);
        Ingresso ingressoVIP = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.VIP)
                .findFirst().orElseThrow();
        assertEquals(100.0, ingressoVIP.getPrecoFinal(), 0.001);
    }

    // CT-AL-13: %DESCONTO = 1, Valor do Ingresso = 100, Tipo VIP → VÁLIDO (final =
    // 99)
    @Test
    void testDescontoVIP_CT_AL_10() {
        Lote lote = new Lote(100, 0.20, 50.0, 0.01);
        Ingresso ingressoVIP = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.VIP)
                .findFirst().orElseThrow();
        assertEquals(99.0, ingressoVIP.getPrecoFinal(), 0.001);
    }

    // CT-AL-14: %DESCONTO = 20, Valor do Ingresso = 100, Tipo VIP → VÁLIDO (final =
    // 80)
    @Test
    void testDescontoVIP_CT_AL_11() {
        Lote lote = new Lote(100, 0.20, 50.0, 0.20);
        Ingresso ingressoVIP = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.VIP)
                .findFirst().orElseThrow();
        assertEquals(80.0, ingressoVIP.getPrecoFinal(), 0.001);
    }

    // CT-AL-15: %DESCONTO = 24, Valor do Ingresso = 100, Tipo VIP → VÁLIDO (final =
    // 76)
    @Test
    void testDescontoVIP_CT_AL_12() {
        Lote lote = new Lote(100, 0.20, 50.0, 0.24);
        Ingresso ingressoVIP = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.VIP)
                .findFirst().orElseThrow();
        assertEquals(76.0, ingressoVIP.getPrecoFinal(), 0.001);
    }

    // CT-AL-16: %DESCONTO = 25, Valor do Ingresso = 100, Tipo VIP → VÁLIDO (final =
    // 75)
    @Test
    void testDescontoVIP_CT_AL_13() {
        Lote lote = new Lote(100, 0.20, 50.0, 0.25);
        Ingresso ingressoVIP = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.VIP)
                .findFirst().orElseThrow();
        assertEquals(75.0, ingressoVIP.getPrecoFinal(), 0.001);
    }

    // CT-AL-17: %DESCONTO = 26, Valor do Ingresso = 100, Tipo VIP → ERRO
    @Test
    void testDescontoVIP_CT_AL_14() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.20, 50.0, 0.26);
        });
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }

    // CT-AL-11: %DESCONTO = -1, Valor do Ingresso = 100, Tipo NORMAL → ERRO
    @Test
    void testDescontoNormal_CT_AL_15() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Para NORMAL, usamos precoNormal=100
            new Lote(100, 0.20, 100.0, -0.01);
        });
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }

    // CT-AL-12: %DESCONTO = 0, Valor do Ingresso = 100, Tipo NORMAL → VÁLIDO
    @Test
    void testDescontoNormal_CT_AL_16() {
        Lote lote = new Lote(100, 0.20, 100.0, 0.0);
        Ingresso ingressoNormal = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL)
                .findFirst().orElseThrow();
        assertEquals(100.0, ingressoNormal.getPrecoFinal(), 0.001);
    }

    // CT-AL-13: %DESCONTO = 1, Valor do Ingresso = 100, Tipo NORMAL → VÁLIDO (final
    // = 99)
    @Test
    void testDescontoNormal_CT_AL_17() {
        Lote lote = new Lote(100, 0.20, 100.0, 0.01);
        Ingresso ingressoNormal = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL)
                .findFirst().orElseThrow();
        assertEquals(99.0, ingressoNormal.getPrecoFinal(), 0.001);
    }

    // CT-AL-14: %DESCONTO = 20, Valor do Ingresso = 100, Tipo NORMAL → VÁLIDO
    // (final = 80)
    @Test
    void testDescontoNormal_CT_AL_18() {
        Lote lote = new Lote(100, 0.20, 100.0, 0.20);
        Ingresso ingressoNormal = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL)
                .findFirst().orElseThrow();
        assertEquals(80.0, ingressoNormal.getPrecoFinal(), 0.001);
    }

    // CT-AL-15: %DESCONTO = 24, Valor do Ingresso = 100, Tipo NORMAL → VÁLIDO
    // (final = 76)
    @Test
    void testDescontoNormal_CT_AL_19() {
        Lote lote = new Lote(100, 0.20, 100.0, 0.24);
        Ingresso ingressoNormal = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL)
                .findFirst().orElseThrow();
        assertEquals(76.0, ingressoNormal.getPrecoFinal(), 0.001);
    }

    // CT-AL-16: %DESCONTO = 25, Valor do Ingresso = 100, Tipo NORMAL → VÁLIDO
    // (final = 75)
    @Test
    void testDescontoNormal_CT_AL_20() {
        Lote lote = new Lote(100, 0.20, 100.0, 0.25);
        Ingresso ingressoNormal = lote.getIngressos().stream()
                .filter(i -> i.getTipo() == TipoIngresso.NORMAL)
                .findFirst().orElseThrow();
        assertEquals(75.0, ingressoNormal.getPrecoFinal(), 0.001);
    }

    // CT-AL-17: %DESCONTO = 26, Valor do Ingresso = 100, Tipo NORMAL → ERRO
    @Test
    void testDescontoNormal_CT_AL_21() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Lote(100, 0.20, 100.0, 0.26);
        });
        assertEquals("Desconto deve estar entre 0% e 25%", exception.getMessage());
    }
}
