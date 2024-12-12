package com.contas;

import java.util.ArrayList;
import java.util.List;

import com.contas.Pagamento.TipoPagamento;

public class ProcessadorDeContas {
    private List<Pagamento> pagamentos;

    public ProcessadorDeContas() {
        this.pagamentos = new ArrayList<>();
    }

    public void processar(List<Conta> contas, List<String> tipos) {
        if (contas.size() != tipos.size()) {
            throw new IllegalArgumentException("A lista de contas e a lista de tipos devem ter o mesmo tamanho.");
        }
        
        for (int i = 0; i < contas.size(); i++) {
            TipoPagamento tipo;
            switch (tipos.get(i)) {
                case "boleto":
                    tipo = Pagamento.TipoPagamento.BOLETO;
                    break; 
                case "cartão":
                    tipo = Pagamento.TipoPagamento.CARTAO_CREDITO;
                    break;
                case "tranferencia":
                    tipo = Pagamento.TipoPagamento.TRANSFERENCIA_BANCARIA;
                    break;
                default:
                    tipo = Pagamento.TipoPagamento.TRANSFERENCIA_BANCARIA;
            }

            pagamentos.add(new Pagamento(tipo, contas.get(i)));
        }
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }
}
