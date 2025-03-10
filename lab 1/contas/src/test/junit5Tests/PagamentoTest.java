import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.contas.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Testes da classe Pagamento")
public class PagamentoTest {
    private SimpleDateFormat sdf;
    private Fatura fatura;
    private Conta conta;
    private Date dataValida;

    @BeforeEach
    void setup() throws ParseException {
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataValida = sdf.parse("20/02/2023");
        fatura = new Fatura("Cliente A", dataValida, 1500.00);
        conta = new Conta("001", dataValida, 500.00, fatura);
    }

    @Nested
    @DisplayName("Testes de criação válida")
    class CriacaoValida {
        @ParameterizedTest
        @EnumSource(Pagamento.TipoPagamento.class)
        @DisplayName("Deve criar pagamento com diferentes tipos válidos")
        void testCreatePagamentoComTiposValidos(Pagamento.TipoPagamento tipo) {
            Pagamento pagamento = new Pagamento(tipo, conta);

            assertEquals(500.00, pagamento.getValor(), 0.01, "Valor deve ser igual ao da conta");
            assertEquals(dataValida, pagamento.getData(), "Data deve ser igual à da conta");
            assertEquals(tipo, pagamento.getTipo(), "Tipo deve ser igual ao informado");
            assertEquals(conta, pagamento.getConta(), "Conta deve ser igual à informada");
        }
    }

    @Nested
    @DisplayName("Testes de validação de parâmetros")
    class ValidacaoParametros {
        @Test
        @DisplayName("Deve lançar exceção para tipo nulo")
        void testCreatePagamentoWithNullTipo() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Pagamento(null, conta);
            }, "Deve lançar IllegalArgumentException para tipo nulo");

            assertEquals("Tipo de pagamento não pode ser nulo.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para conta nula")
        void testCreatePagamentoWithNullConta() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new Pagamento(Pagamento.TipoPagamento.BOLETO, null);
            }, "Deve lançar IllegalArgumentException para conta nula");

            assertEquals("Conta não pode ser nula.", exception.getMessage());
        }
    }
}