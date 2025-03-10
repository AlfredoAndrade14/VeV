package junit5Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Testes do Processador de Contas")
public class ProcessadorDeContasTest {
    private SimpleDateFormat sdf;
    private ProcessadorDeContas processador;
    private Date dataFatura;
    
    @BeforeEach
    void setup() throws ParseException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        processador = new ProcessadorDeContas();
        dataFatura = sdf.parse("20/02/2023");
    }
    
    @Nested
    @DisplayName("Testes de processamento válido")
    class ProcessamentoValido {
    @Test
        @DisplayName("Deve processar pagamento com boleto")
        void testProcessPagamentos() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 500.00);
            Conta conta = new Conta("001", dataFatura, 500.00, fatura);
        Pagamento pagamento = new Pagamento(Pagamento.TipoPagamento.BOLETO, conta);
        List<Pagamento> expected = new ArrayList<>();
        expected.add(pagamento);
        
            List<Conta> contas = List.of(conta);
            List<String> tipos = List.of("boleto");

        processador.processar(contas, tipos);
            assertEquals(expected, processador.getPagamentos(), "Pagamentos processados devem corresponder aos esperados");
        }

        @Test
        @DisplayName("Deve processar boleto com acréscimo de 10% quando atrasado")
        void testProcessPagamentosComBoletoAtrasado() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 500.00);
            Conta conta = new Conta("001", sdf.parse("21/02/2023"), 500.00, fatura);

            List<Conta> contas = new ArrayList<>();
            contas.add(conta);
            List<String> tipos = new ArrayList<>();
            tipos.add("boleto");

            processador.processar(contas, tipos);

            assertEquals(1, processador.getPagamentos().size(), "Deve ter um pagamento processado");
            assertEquals(550.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01, 
                    "Valor pago deve ter acréscimo de 10%");
    }

     @Test
        @DisplayName("Deve processar pagamento com cartão de crédito válido (15+ dias antes)")
        void testProcessPagamentosComCartaoCreditoValido() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 500.00);
            Conta conta = new Conta("001", sdf.parse("05/02/2023"), 500.00, fatura);

            processador.processar(List.of(conta), List.of("cartão"));

            assertEquals(1, processador.getPagamentos().size(), "Deve ter um pagamento processado");
            assertEquals(Pagamento.TipoPagamento.CARTAO_CREDITO, processador.getPagamentos().get(0).getTipo(),
                    "Tipo de pagamento deve ser cartão de crédito");
        }
    }
    
    @Nested
    @DisplayName("Testes de validação de parâmetros")
    class ValidacaoParametros {
        @Test
        @DisplayName("Deve lançar exceção quando listas têm tamanhos diferentes")
        void testProcessPagamentosWithMismatchedSizes() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 500.00);
            Conta conta = new Conta("001", dataFatura, 500.00, fatura);

            List<Conta> contas = List.of(conta, conta); // Duas contas
            List<String> tipos = List.of("boleto"); // Um tipo

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
            }, "Deve lançar exceção quando listas têm tamanhos diferentes");

        assertEquals("A lista de contas e a lista de tipos devem ter o mesmo tamanho.", exception.getMessage());
    }

    @Test
        @DisplayName("Deve lançar exceção quando listas estão vazias")
        void testProcessPagamentosWithEmptyLists() {
        List<Conta> contas = new ArrayList<>();
        List<String> tipos = new ArrayList<>();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
            }, "Deve lançar exceção quando listas estão vazias");

        assertEquals("A lista de contas e a lista de tipos não podem ser vazias", exception.getMessage());
    }

    @Test
        @DisplayName("Deve lançar exceção quando tipo de pagamento é inválido")
        void testProcessPagamentosWithInvalidTipo() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 500.00);
            Conta conta = new Conta("001", dataFatura, 500.00, fatura);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                processador.processar(List.of(conta), List.of("invalido"));
            }, "Deve lançar exceção quando tipo de pagamento é inválido");

        assertEquals("Tipo de pagamento inválido: invalido", exception.getMessage());
    }

    @Test
        @DisplayName("Deve lançar exceção quando contas pertencem a faturas diferentes")
        void testContasDeFaturasDiferentes() throws ParseException {
            Fatura fatura1 = new Fatura("Cliente A", dataFatura, 500.00);
        Fatura fatura2 = new Fatura("Cliente B", sdf.parse("21/02/2023"), 500.00);
            Conta conta1 = new Conta("001", dataFatura, 500.00, fatura1);
        Conta conta2 = new Conta("002", sdf.parse("21/02/2023"), 500.00, fatura2);

            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                processador.processar(List.of(conta1, conta2), List.of("boleto", "boleto"));
            }, "Deve lançar exceção quando contas pertencem a faturas diferentes");
            
            assertEquals("Todas as contas devem pertencer à mesma fatura.", exception.getMessage());
        }
    }
    
    @Nested
    @DisplayName("Testes de status da fatura")
    class StatusFatura {
    @Test
        @DisplayName("Fatura deve estar paga quando valor total é suficiente")
        void testFaturaPaga() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 500.00);
            Conta conta = new Conta("001", dataFatura, 500.00, fatura);
            
            processador.processar(List.of(conta), List.of("boleto"));
            
            assertEquals(true, fatura.isPaga(), "Fatura deve estar paga quando valor total é suficiente");
    }

    @Test
        @DisplayName("Fatura deve estar pendente quando valor total é insuficiente")
        void testFaturaPendente() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 500.00);
        Conta conta = new Conta("001", sdf.parse("19/02/2023"), 300.00, fatura);
        
            processador.processar(List.of(conta), List.of("boleto"));
            
            assertEquals(false, fatura.isPaga(), "Fatura deve estar pendente quando valor total é insuficiente");
        }
    }
    
    @Nested
    @DisplayName("Cenários de exemplo")
    class CenariosExemplo {
    @Test
        @DisplayName("Exemplo 1: Pagamento com múltiplos boletos")
        void testExemplo1() throws ParseException {
            Fatura fatura = new Fatura("Cliente A", dataFatura, 1500.00);
            Conta conta1 = new Conta("001", dataFatura, 500.00, fatura);
            Conta conta2 = new Conta("002", dataFatura, 400.00, fatura);
            Conta conta3 = new Conta("003", dataFatura, 600.00, fatura);

            processador.processar(
                List.of(conta1, conta2, conta3), 
                List.of("boleto", "boleto", "boleto")
            );

            assertEquals(true, fatura.isPaga(), "Fatura deve estar paga com múltiplos boletos");
        }

        @ParameterizedTest
        @CsvSource({
            "05/02/2023, 17/02/2023, cartão, tranferencia, true, 'Pagamento com cartão válido e transferência'",
            "06/02/2023, 17/02/2023, cartão, tranferencia, false, 'Pagamento com cartão inválido e transferência'"
        })
        @DisplayName("Exemplos com combinação de métodos de pagamento")
        void testExemplosCombinadosMetodosPagamento(
                String dataConta1, String dataConta2, 
                String tipo1, String tipo2, 
                boolean deveEstarPaga, String descricao) throws ParseException {
            
            Fatura fatura = new Fatura("Cliente A", dataFatura, 1500.00);
            Conta conta1 = new Conta("001", sdf.parse(dataConta1), 700.00, fatura);
            Conta conta2 = new Conta("002", sdf.parse(dataConta2), 800.00, fatura);

            try {
                processador.processar(List.of(conta1, conta2), List.of(tipo1, tipo2));
                assertEquals(deveEstarPaga, fatura.isPaga(), descricao);
            } catch (IllegalArgumentException e) {
                // Se esperamos que a fatura não seja paga devido a uma exceção
                assertEquals(false, deveEstarPaga, descricao);
            }
        }
    }
}