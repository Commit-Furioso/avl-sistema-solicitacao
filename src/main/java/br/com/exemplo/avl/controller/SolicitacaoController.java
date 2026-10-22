package br.com.exemplo.avl.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.exemplo.avl.dto.ApiResponse;
import br.com.exemplo.avl.dto.AtualizarSolicitacaoRequest;
import br.com.exemplo.avl.dto.SolicitacaoRequest;
import br.com.exemplo.avl.model.Solicitacao;
import br.com.exemplo.avl.service.SolicitacaoService;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> cadastrar(@RequestBody SolicitacaoRequest request) {
        try {
            service.cadastrar(request.getNumero(), request.getDescricao(), request.getSolicitante());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Solicitação cadastrada com sucesso."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping
    public List<Solicitacao> listar() {
        return service.listar();
    }

    @GetMapping("/{numero}")
    public ResponseEntity<Solicitacao> buscar(@PathVariable int numero) {
        Solicitacao solicitacao = service.buscar(numero);

        if (solicitacao == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(solicitacao);
    }

    @DeleteMapping("/{numero}")
    public ResponseEntity<ApiResponse> remover(@PathVariable int numero) {
        if (!service.remover(numero)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Solicitação não encontrada."));
        }

        return ResponseEntity.ok(
                new ApiResponse(true, "Solicitação " + numero + " removida com sucesso."));
    }

    @PutMapping("/{numero}")
    public ResponseEntity<ApiResponse> alterar(
            @PathVariable int numero,
            @RequestBody AtualizarSolicitacaoRequest request) {
        try {
            if (!service.alterar(numero, request.getSolicitante(), request.getDescricao())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Solicitação não encontrada."));
            }

            return ResponseEntity.ok(
                    new ApiResponse(true, "Solicitação alterada com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping(value = "/arvore", produces = MediaType.TEXT_PLAIN_VALUE)
    public String arvore() {
        return service.exibirArvore();
    }
}
