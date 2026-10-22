package br.com.exemplo.avl.model;

public class NoAVL {
    public Solicitacao solicitacao;
    public NoAVL esquerda, direita;
    public int altura;

    public NoAVL(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
        this.altura = 1;
    }
}
