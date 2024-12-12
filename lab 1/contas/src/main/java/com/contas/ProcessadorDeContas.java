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
                    if (contas.get(i).getValorPago() < 0.01 || contas.get(i).getValorPago() > 5000.00) {
                        throw new IllegalArgumentException("Pagamentos por boleto devem estar entre R$ 0,01 e R$ 5.000,00.");
                    }
        
                    if (contas.get(i).getData().after(contas.get(i).getFatura().getData())) {
                        double valorAcrescido = contas.get(i).getValorPago() * 1.10;
                        Conta contaAtualizada = new Conta(
                            contas.get(i).getCodigo(),
                            contas.get(i).getData(),
                            valorAcrescido,
                            contas.get(i).getFatura()
                        );
                    
                        contas.set(i, contaAtualizada);
                    }
                    tipo = Pagamento.TipoPagamento.BOLETO;
                    break; 
                case "cartão":
                    tipo = Pagamento.TipoPagamento.CARTAO_CREDITO;
                    break;
                case "tranferencia":
                    tipo = Pagamento.TipoPagamento.TRANSFERENCIA_BANCARIA;
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de pagamento inválido: " + tipos.get(i));
            }

            pagamentos.add(new Pagamento(tipo, contas.get(i)));
        }
    }

    public List<Pagamento> getPagamentos() {
        return pagamentos;
    }
}
