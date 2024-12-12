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
}