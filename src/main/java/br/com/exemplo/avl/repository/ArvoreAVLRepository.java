package br.com.exemplo.avl.repository;

import java.util.*;
import org.springframework.stereotype.Repository;
import br.com.exemplo.avl.model.*;

@Repository
public class ArvoreAVLRepository implements SolicitacaoRepository {
    private NoAVL raiz;

    private int altura(NoAVL n) {
        return n == null ? 0 : n.altura;
    }

    private void atualizarAltura(NoAVL n) {
        n.altura = 1 + Math.max(altura(n.esquerda), altura(n.direita));
    }

    private int fb(NoAVL n) {
        return n == null ? 0 : altura(n.esquerda) - altura(n.direita);
    }

    private NoAVL rotacaoDireita(NoAVL y) {
        NoAVL x = y.esquerda, t = x.direita;
        x.direita = y;
        y.esquerda = t;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    private NoAVL rotacaoEsquerda(NoAVL x) {
        NoAVL y = x.direita, t = y.esquerda;
        y.esquerda = x;
        x.direita = t;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    private NoAVL balancear(NoAVL n) {
        atualizarAltura(n);
        int f = fb(n);
        if (f > 1) {
            if (fb(n.esquerda) < 0)
                n.esquerda = rotacaoEsquerda(n.esquerda);
            return rotacaoDireita(n);
        }
        if (f < -1) {
            if (fb(n.direita) > 0)
                n.direita = rotacaoDireita(n.direita);
            return rotacaoEsquerda(n);
        }
        return n;
    }

    public void inserir(Solicitacao s) {
        raiz = inserir(raiz, s);
    }

    private NoAVL inserir(NoAVL n, Solicitacao s) {
        if (n == null)
            return new NoAVL(s);
        if (s.getNumero() < n.solicitacao.getNumero())
            n.esquerda = inserir(n.esquerda, s);
        else if (s.getNumero() > n.solicitacao.getNumero())
            n.direita = inserir(n.direita, s);
        else
            throw new IllegalArgumentException("Solicitação já cadastrada");
        return balancear(n);
    }

    public Solicitacao buscar(int numero) {
        NoAVL a = raiz;
        while (a != null) {
            if (numero == a.solicitacao.getNumero())
                return a.solicitacao;
            a = numero < a.solicitacao.getNumero() ? a.esquerda : a.direita;
        }
        return null;
    }

    public boolean remover(int numero) {
        if (buscar(numero) == null)
            return false;
        raiz = remover(raiz, numero);
        return true;
    }

    private NoAVL remover(NoAVL n, int numero) {
        if (n == null)
            return null;
        if (numero < n.solicitacao.getNumero())
            n.esquerda = remover(n.esquerda, numero);
        else if (numero > n.solicitacao.getNumero())
            n.direita = remover(n.direita, numero);
        else {
            if (n.esquerda == null && n.direita == null)
                return null;
            if (n.esquerda == null)
                return n.direita;
            if (n.direita == null)
                return n.esquerda;
            NoAVL s = menor(n.direita);
            n.solicitacao = s.solicitacao;
            n.direita = remover(n.direita, s.solicitacao.getNumero());
        }
        return balancear(n);
    }

    @Override
    public boolean alterar(int numero, String solicitante, String descricao) {
        NoAVL atual = raiz;

        while (atual != null) {
            if (numero == atual.solicitacao.getNumero()) {
                atual.solicitacao.setSolicitante(solicitante);
                atual.solicitacao.setDescricao(descricao);
                return true;
            }
            atual = numero < atual.solicitacao.getNumero() ? atual.esquerda : atual.direita;
        }

        return false;
    }

    private NoAVL menor(NoAVL n) {
        while (n.esquerda != null)
            n = n.esquerda;
        return n;
    }

    public List<Solicitacao> listar() {
        List<Solicitacao> l = new ArrayList<>();
        emOrdem(raiz, l);
        return l;
    }

    private void emOrdem(NoAVL n, List<Solicitacao> l) {
        if (n != null) {
            emOrdem(n.esquerda, l);
            l.add(n.solicitacao);
            emOrdem(n.direita, l);
        }
    }

    public String exibirArvore() {
        if (raiz == null)
            return "(árvore vazia)\n";
        StringBuilder sb = new StringBuilder();
        exibir(raiz, "", true, sb);
        return sb.toString();
    }

    private void exibir(NoAVL n, String p, boolean ultimo, StringBuilder sb) {
        if (n == null)
            return;
        sb.append(p).append(ultimo ? "└── " : "├── ").append(n.solicitacao.getNumero()).append(" [FB=").append(fb(n))
                .append("] ").append(n.solicitacao.getDescricao()).append("\n");
        String np = p + (ultimo ? "    " : "│   ");
        if (n.esquerda != null)
            exibir(n.esquerda, np, n.direita == null, sb);
        if (n.direita != null)
            exibir(n.direita, np, true, sb);
    }
}
