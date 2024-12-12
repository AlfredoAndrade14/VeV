import org.junit.jupiter.api.Test;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PagamentoTest {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void TestCreateConta() throws ParseException {
        Fatura fatura = new Fatura("Cliente A", sdf.parse("20/02/2023"), 1500.00);
        Conta conta = new Conta("001", sdf.parse("20/02/2023"), 500.00, fatura);
        Pagamento pagamento = new Pagamento(Pagamento.TipoPagamento.BOLETO, conta);

        assertEquals(500.00, pagamento.getValor());
        assertEquals(sdf.parse("20/02/2023"), pagamento.getData());
        assertEquals(Pagamento.TipoPagamento.BOLETO, pagamento.getTipo());
        assertEquals(pagamento.getConta(), conta);
    }
    
}