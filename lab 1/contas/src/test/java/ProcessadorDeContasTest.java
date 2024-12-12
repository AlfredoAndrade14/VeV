import org.junit.jupiter.api.Test;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}