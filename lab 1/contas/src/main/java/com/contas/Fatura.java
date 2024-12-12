package com.contas;

import java.util.Date;

public class Fatura {
    private String cliente;
    private Date data;
    private double valor;
    private boolean paga;

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
        this.paga = false;
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
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }
}
