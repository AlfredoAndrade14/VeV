package com.contas;

import java.util.Date;

public class Fatura {
    private String cliente;
    private Date data;
    private double valor;
    private StatusFatura status;

    public enum StatusFatura {
        PAGA, PENDENTE
    }

    public Fatura(String cliente, Date data, double valor) {
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("Cliente não pode ser nulo ou vazio.");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data não pode ser nula.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo.");
        }
        
        this.cliente = cliente;
        this.data = data;
        this.valor = valor;
        this.status = StatusFatura.PENDENTE;
    }

    public String getCliente() {
        return cliente;
    }

    public Date getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }

    public boolean isPaga() {
        if (this.status.equals(StatusFatura.PAGA)) {
            return true;
        }
        return false;
    }

    public void setPaga() {
        this.status = StatusFatura.PAGA;
    }
}
