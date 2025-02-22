import org.junit.jupiter.api.Test;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProcessadorDeContasTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    
    @Test
    public void TestProcessPagamentos() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);
        Pagamento pagamento = new Pagamento(Pagamento.TipoPagamento.BOLETO, conta);
        List<Pagamento> expected = new ArrayList<>();
        expected.add(pagamento);
        
        List<Conta> contas = new ArrayList<>();
        contas.add(conta);
        List<String> tipos = new ArrayList<>();
        tipos.add("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);
        assertEquals(processador.getPagamentos(), expected);
    }

     @Test
    public void testProcessPagamentosWithMismatchedSizes() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);

        List<Conta> contas = new ArrayList<>();
        contas.add(conta);
        contas.add(conta); // Extra conta for mismatch
        List<String> tipos = new ArrayList<>();
        tipos.add("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("A lista de contas e a lista de tipos devem ter o mesmo tamanho.", exception.getMessage());
    }

    @Test
    public void testProcessPagamentosWithEmptyLists() {
        List<Conta> contas = new ArrayList<>();
        List<String> tipos = new ArrayList<>();

        ProcessadorDeContas processador = new ProcessadorDeContas();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("A lista de contas e a lista de tipos não podem ser vazias", exception.getMessage());
    }

    @Test
    public void testProcessPagamentosWithInvalidTipo() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);

        List<Conta> contas = new ArrayList<>();
        contas.add(conta);
        List<String> tipos = new ArrayList<>();
        tipos.add("invalido");

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Tipo de pagamento inválido: invalido", exception.getMessage());
    }

    @Test
    public void testProcessPagamentosComBoletoAtrasado() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("21/02/2023"), 500.00, fatura);

        List<Conta> contas = new ArrayList<>();
        contas.add(conta);
        List<String> tipos = new ArrayList<>();
        tipos.add("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(1, processador.getPagamentos().size());
        assertEquals(550.00, processador.getPagamentos().get(0).getConta().getValorPago(), 0.01); // Acrescido 10%.
    }

    @Test
    public void testProcessPagamentosComCartaoCreditoValido() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("05/02/2023"), 500.00, fatura);  // 15 dias de antecedência
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(1, processador.getPagamentos().size());
        assertEquals(Pagamento.TipoPagamento.CARTAO_CREDITO, processador.getPagamentos().get(0).getTipo());
    }

    @Test
    public void TestContasDeFaturasDiferentes() throws ParseException {
        Fatura fatura1 = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Fatura fatura2 = new Fatura("Cliente B", sdf.parse("21/02/2023"), 500.00);
        Conta conta1 = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura1);
        Conta conta2 = new Conta("002", sdf.parse("21/02/2023"), 500.00, fatura2);

        List<Conta> contas = new ArrayList<>();
        contas.add(conta1);
        contas.add(conta2);
        
        List<String> tipos = new ArrayList<>();
        tipos.add("boleto");
        tipos.add("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });
        
        assertEquals("Todas as contas devem pertencer à mesma fatura.", thrown.getMessage());
    }

    @Test
    public void TestFaturaPaga() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);
        List<Conta> contas = new ArrayList<>();
        contas.add(conta);
        
        List<String> tipos = new ArrayList<>();
        tipos.add("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);
        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void TestFaturaPendente() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);

        Conta conta = new Conta("001", sdf.parse("19/02/2023"), 300.00, fatura);
        
        List<Conta> contas = new ArrayList<>();
        contas.add(conta);
        
        List<String> tipos = new ArrayList<>();
        tipos.add("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);
        
        assertEquals(fatura.isPaga(), false);
    }

    @Test
    public void TestExemplo1() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);
        Conta conta1 = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);
        Conta conta2 = new Conta("002", sdf.parse("20/02/2023"), 400.00, fatura);
        Conta conta3 = new Conta("003", sdf.parse("20/02/2023"), 600.00, fatura);

        List<Conta> contas = List.of(conta1, conta2, conta3);
        List<String> tipos = List.of("boleto", "boleto", "boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void TestExemplo2() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);
        Conta conta1 = new Conta("001", sdf.parse("05/02/2023"), 700.00, fatura);
        Conta conta2 = new Conta("002", sdf.parse("17/02/2023"), 800.00, fatura);

        List<Conta> contas = List.of(conta1, conta2);
        List<String> tipos = List.of("cartão", "tranferencia");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), true);
    }

    @Test
    public void TestExemplo3() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);
        Conta conta1 = new Conta("001", sdf.parse("06/02/2023"), 700.00, fatura);
        Conta conta2 = new Conta("002", sdf.parse("17/02/2023"), 800.00, fatura);

        List<Conta> contas = List.of(conta1, conta2);
        List<String> tipos = List.of("cartão", "tranferencia");

        ProcessadorDeContas processador = new ProcessadorDeContas();
        processador.processar(contas, tipos);

        assertEquals(fatura.isPaga(), false);
    }
}