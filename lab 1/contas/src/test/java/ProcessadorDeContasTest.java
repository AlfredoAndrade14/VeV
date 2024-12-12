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
        processador.processar(contas, tipos);

        assertEquals(0, processador.getPagamentos().size());
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
    public void testProcessPagamentosComBoletoValorInvalido() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("19/02/2023"), 0.00, fatura);
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("boleto");

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Pagamentos por boleto devem estar entre R$ 0,01 e R$ 5.000,00.", exception.getMessage());
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
    public void testProcessPagamentosComCartaoCreditoDataInvalida() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 500.00);
        Conta conta = new Conta("001", sdf.parse("15/02/2023"), 500.00, fatura);  // Menos de 15 dias
        List<Conta> contas = List.of(conta);
        List<String> tipos = List.of("cartão");

        ProcessadorDeContas processador = new ProcessadorDeContas();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            processador.processar(contas, tipos);
        });

        assertEquals("Pagamentos por cartão de crédito só podem ser incluídos se a data da conta for pelo menos 15 dias anteriores à data da fatura.", exception.getMessage());
    }

}