package br.com.exemplo.avl.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.exemplo.avl.model.Solicitacao;
import br.com.exemplo.avl.repository.SolicitacaoRepository;

@Service
public class SolicitacaoService {
    private final SolicitacaoRepository repository;

    public SolicitacaoService(SolicitacaoRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(int numero, String descricao, String solicitante) {
        validarDados(solicitante, descricao);
        LocalDateTime dataAbertura = LocalDateTime.now();
        repository.inserir(new Solicitacao(numero, solicitante, descricao));
    }

    public Solicitacao buscar(int numero) {
        return repository.buscar(numero);
    }

    public boolean remover(int numero) {
        return repository.remover(numero);
    }

    public boolean alterar(int numero, String solicitante, String descricao) {
        validarDados(solicitante, descricao);
        return repository.alterar(numero, solicitante, descricao);
    }

    private void validarDados(String solicitante, String descricao) {
        if (solicitante == null || solicitante.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o nome do solicitante.");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe a descrição da solicitação.");
        }
    }

    public List<Solicitacao> listar() {
        return repository.listar();
    }

    public String exibirArvore() {
        return repository.exibirArvore();
    }
}
