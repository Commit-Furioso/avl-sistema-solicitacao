package br.com.exemplo.avl.dto;

public class ApiResponse {

    private boolean sucesso;
    private String mensagem;

    public ApiResponse(boolean sucesso, String mensagem) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }
}
