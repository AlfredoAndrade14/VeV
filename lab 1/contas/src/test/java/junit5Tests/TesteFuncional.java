package junit5Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.contas.Conta;
import com.contas.Fatura;
import com.contas.ProcessadorDeContas;

@DisplayName("Testes Funcionais do Sistema de Contas")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TesteFuncional {
    private SimpleDateFormat sdf;
    private ProcessadorDeContas processador;

    @BeforeEach
    void setup() {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        processador = new ProcessadorDeContas();
    }

    @Nested
    @DisplayName("Análise de Valor Limite")
    class AnaliseValorLimite {
        
        @Test
        @DisplayName("AVL01: Valor mínimo válido (0.01)")
        void testAVL01() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 0.01);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 0.01, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(true, fatura.isPaga(), "Fatura deve estar paga com valor mínimo");
        }

        @Test
        @DisplayName("AVL02: Valor zero (inválido)")
        void testAVL02() throws ParseException {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Fatura("Cliente A", sdf.parse("20/02/2023"), 0.00);
            });

            assertEquals("Valor deve ser positivo.", exception.getMessage());
        }

        @Test
        @DisplayName("AVL03: Valor máximo válido (5000.00)")
        public void testAVL03() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.00);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 5000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), true);
        }

        @Test
        @DisplayName("AVL04: Valor acima do máximo (5000.01)")
        public void testAVL04() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.01);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 5000.01, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                processador.processar(contas, tipos);
            });

            assertEquals("Valor do boleto inválido: maior que 5000", exception.getMessage());
        }

        @Test
        @DisplayName("AVL05: Valor válido (1000.00)")
        public void testAVL05() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), true);
        }

        @Test
        @DisplayName("AVL06: Valor com acréscimo de 10% (1100.00)")
        public void testAVL06() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("21/02/2023"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(1100.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01);
        }

        @Test
        @DisplayName("AVL07: Pagamento com cartão (1000.00)")
        public void testAVL07() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("05/02/2023"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("cartão");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), true);
        }

        @Test
        @DisplayName("AVL08: Cartão com menos de 15 dias antes (inválido)")
        public void testAVL08() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("06/02/2023"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("cartão");

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                processador.processar(contas, tipos);
            });

            assertEquals("Cartão não pode ser usado: menos de 15 dias antes", exception.getMessage());
        }

        @Test
        @DisplayName("AVL09: Pagamento com boleto (2000.00)")
        public void testAVL09() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 2000.00);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), false);
        }

        @Test
        @DisplayName("AVL10: Pagamento com boleto (1000.00)")
        public void testAVL10() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), true);
        }
    }

    @Nested
    @DisplayName("Partição de Equivalência")
    class ParticaoEquivalencia {
        
        @Test
        @DisplayName("PE01: Pagamento exato com boleto")
        void testPE01() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(true, fatura.isPaga(), "Fatura deve estar paga com valor exato");
        }

        @Test
        @DisplayName("PE02: Valor mínimo inválido (0.005)")
        public void testPE02() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 0.005);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 0.005, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                processador.processar(contas, tipos);
            });

            assertEquals("Valor do boleto inválido: menor que 0.01", exception.getMessage());
        }

        @Test
        @DisplayName("PE03: Valor acima do máximo (6000.00)")
        public void testPE03() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 6000.00);
            Conta conta = new Conta("001", sdf.parse("20/02/2023"), 6000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                processador.processar(contas, tipos);
            });

            assertEquals("Valor do boleto inválido: maior que 5000", exception.getMessage());
        }

        @Test
        @DisplayName("PE04: Pagamento com dois boletos (2500.00 + 2500.00)")
        public void testPE04() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.00);
            Conta conta1 = new Conta("001", sdf.parse("20/02/2023"), 2500.00, fatura);
            Conta conta2 = new Conta("002", sdf.parse("20/02/2023"), 2500.00, fatura);
            List<Conta> contas = List.of(conta1, conta2);
            List<String> tipos = List.of("boleto", "boleto");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), true);
        }

        @Test
        @DisplayName("PE05: Pagamento com dois boletos (2000.00 + 2000.00)")
        public void testPE05() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 5000.00);
            Conta conta1 = new Conta("001", sdf.parse("20/02/2023"), 2000.00, fatura);
            Conta conta2 = new Conta("002", sdf.parse("20/02/2023"), 2000.00, fatura);
            List<Conta> contas = List.of(conta1, conta2);
            List<String> tipos = List.of("boleto", "boleto");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), false);
        }
    }

    @Nested
    @DisplayName("Tabela de Decisão")
    class TabelaDecisao {
        
        @ParameterizedTest(name = "Pagamento com {0} no valor de {2}, data fatura: {1}, data pagamento: {3}")
        @CsvSource({
            "boleto, 01/02/2024, 2000.00, 01/02/2024, true",
            "boleto, 01/02/2024, 2000.00, 10/02/2024, false"
        })
        void testPagamentosComDiferentesDatas(String tipoPagamento, String dataFatura, 
                                             double valor, String dataPagamento, 
                                             boolean deveEstarPaga) throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse(dataFatura), valor);
            Conta conta = new Conta("001", sdf.parse(dataPagamento), valor, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of(tipoPagamento);

            processador.processar(contas, tipos);

            assertEquals(deveEstarPaga, fatura.isPaga(), 
                "Verificando se fatura está paga corretamente");
        }
        
        @Test
        @DisplayName("TD02: Boleto com pagamento atrasado (acréscimo de 10%)")
        void testTD02() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 2000.00);
            Conta conta = new Conta("001", sdf.parse("10/02/2024"), 2000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

            processador.processar(contas, tipos);

            assertEquals(2200.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01,
                "Valor pago deve ter acréscimo de 10%");
            assertEquals(false, fatura.isPaga(), "Fatura não deve estar paga");
        }
        
        @Test
        @DisplayName("TD03: Pagamento com transferência")
        public void testTD03() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 2000.00);
            Conta conta = new Conta("001", sdf.parse("01/02/2024"), 2000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("tranferencia");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), true);
        }

        @Test
        @DisplayName("TD04: Pagamento com cartão (1000.00)")
        public void testTD04() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("01/02/2024"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("cartão");

            processador.processar(contas, tipos);

            assertEquals(fatura.isPaga(), false);
        }

        @Test
        @DisplayName("TD05: Cartão com menos de 15 dias antes (inválido)")
        public void testTD05() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 1000.00);
            Conta conta = new Conta("001", sdf.parse("20/01/2024"), 1000.00, fatura);
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("cartão");

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                processador.processar(contas, tipos);
            });

            assertEquals("Cartão não pode ser usado: menos de 15 dias antes", exception.getMessage());
        }

        @Test
        @DisplayName("TD06: Pagamento com dois boletos e um cartão (2000.00 + 1000.00)")
        public void testTD06() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 3000.00);
            Conta conta1 = new Conta("001", sdf.parse("10/02/2024"), 2000.00, fatura);
            Conta conta2 = new Conta("002", sdf.parse("01/02/2024"), 1000.00, fatura);
            List<Conta> contas = List.of(conta1, conta2);
            List<String> tipos = List.of("boleto", "cartão");

            processador.processar(contas, tipos);

            assertEquals(2200.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01);
            assertEquals(fatura.isPaga(), false);
        }

        @Test
        @DisplayName("TD07: Pagamento com três boletos, transferência e cartão (2000.00 + 2000.00 + 1000.00)")
        public void testTD07() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", sdf.parse("01/02/2024"), 5000.00);
            Conta conta1 = new Conta("001", sdf.parse("10/02/2024"), 2000.00, fatura);
            Conta conta2 = new Conta("002", sdf.parse("01/02/2024"), 2000.00, fatura);
            Conta conta3 = new Conta("003", sdf.parse("01/02/2024"), 1000.00, fatura);
            List<Conta> contas = List.of(conta1, conta2, conta3);
            List<String> tipos = List.of("boleto", "transferência", "cartão");

            processador.processar(contas, tipos);

            assertEquals(2200.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01);
            assertEquals(fatura.isPaga(), true);
        }
    }
}