let modalEdicao, modalRemocao, numeroParaRemover;

document.addEventListener("DOMContentLoaded", () => {
    modalEdicao = new bootstrap.Modal(document.getElementById('modalEditar'));
    modalRemocao = new bootstrap.Modal(document.getElementById('modalRemover'));
    
    document.getElementById('formCadastro').addEventListener('submit', async (e) => {
        e.preventDefault();
        const payload = {
            numero: parseInt(document.getElementById('numero').value),
            solicitante: document.getElementById('solicitante').value,
            descricao: document.getElementById('descricao').value
        };
        await enviarRequisicao(payload, "Solicitação cadastrada com sucesso!");
    });

    carregarTudo();
});

function mostrarToast(mensagem) {
    document.getElementById('toastMessage').innerText = mensagem;
    new bootstrap.Toast(document.getElementById('liveToast')).show();
}

function formatarData(dataStr) {
    if (!dataStr) return '';
    const d = new Date(dataStr);
    if (isNaN(d)) return dataStr;
    return d.toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
}

async function carregarTudo() {
    await Promise.all([carregarSolicitacoes(), atualizarArvore()]);
}

async function sincronizarManual() {
    const btn = document.getElementById('btnSincronizar');
    const originalText = btn.innerHTML;
    
    btn.innerHTML = `<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Sincronizando...`;
    btn.disabled = true;
    document.getElementById('tabelaSolicitacoes').innerHTML = `<tr><td colspan="5" class="text-center text-muted py-3">Sincronizando com o servidor...</td></tr>`;
    document.getElementById('arvoreVisualizacao').innerHTML = `<span class="text-muted">Buscando estrutura AVL...</span>`;

    await new Promise(r => setTimeout(r, 600));
    await carregarTudo();

    btn.innerHTML = originalText;
    btn.disabled = false;
    mostrarToast("Painel sincronizado com o servidor!");
}

async function carregarSolicitacoes() {
    const tbody = document.getElementById('tabelaSolicitacoes');
    try {
        const res = await fetch('/solicitacoes');
        if (!res.ok) throw new Error("Servidor offline");
        const dados = await res.json();
        
        if (dados.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">Nenhuma solicitação cadastrada.</td></tr>`;
            return;
        }

        tbody.innerHTML = dados.map(s => `
            <tr>
                <td class="fw-bold">${s.numero}</td>
                <td>${s.solicitante}</td>
                <td class="text-muted">${formatarData(s.dataAbertura)}</td>
                <td>${s.descricao}</td>
                <td class="text-end">
                    <button class="btn btn-sm btn-outline-primary me-1" onclick="abrirEdicao(${s.numero}, '${s.solicitante}', '${s.descricao}')">Editar</button>
                    <button class="btn btn-sm btn-outline-danger" onclick="abrirModalRemocao(${s.numero})">Remover</button>
                </td>
            </tr>
        `).join('');
    } catch (e) {
        tbody.innerHTML = `<tr><td colspan="5" class="text-center text-danger fw-bold">Erro de conexão. O servidor Spring Boot está rodando?</td></tr>`;
    }
}

async function atualizarArvore() {
    const container = document.getElementById('arvoreVisualizacao');
    try {
        const res = await fetch('/solicitacoes/arvore');
        if (!res.ok) throw new Error("Servidor offline");
        let texto = await res.text();
        
        if (!texto || texto.trim() === "(árvore vazia)") {
            container.innerHTML = `<span class="text-muted">(árvore vazia)</span>`;
            return;
        }

        let htmlFormatado = texto
            .replace(/(│|├──|└──)/g, '<span class="arvore-linha">$1</span>')
            .replace(/<span class="arvore-linha">(├──|└──)<\/span> (\d+)/g, '<span class="arvore-linha">$1</span> <span class="badge bg-primary fs-6">$2</span>')
            .replace(/^(\d+) \[/gm, '<span class="badge bg-primary fs-6">$1</span> [')
            .replace(/\[FB=([^\]]+)\]/g, function(match, fb) {
                let cor = fb === '0' ? 'success' : (fb === '1' || fb === '-1' ? 'warning text-dark' : 'danger');
                return `<span class="badge bg-${cor} ms-2 me-2 arvore-texto">FB: ${fb}</span>`;
            });

        container.innerHTML = htmlFormatado;
    } catch (e) {
        container.innerHTML = `<span class="text-danger fw-bold">Erro: Servidor inacessível. Inicie o AvlApplication.java!</span>`;
    }
}

async function enviarLote(lista, mensagemSucesso) {
    let houveErro = false;
    for (let item of lista) {
        try {
            const res = await fetch('/solicitacoes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(item)
            });
            if (!res.ok) houveErro = true;
        } catch(e) {
            houveErro = true;
        }
    }
    
    if (houveErro) {
        mostrarToast("Aviso: Falha ou chaves repetidas ignoradas.");
    } else {
        mostrarToast(mensagemSucesso);
    }
    document.getElementById('formCadastro').reset();
    carregarTudo();
}

async function enviarRequisicao(payload, msg) {
    try {
        const res = await fetch('/solicitacoes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        const resultado = await res.json();
        mostrarToast(resultado.mensagem || msg);
        if (res.ok) {
            document.getElementById('formCadastro').reset();
            carregarTudo();
        }
    } catch (error) {
        mostrarToast("Erro: Verifique se o servidor está rodando.");
    }
}

function carregarDadosExercicio() {
    const exemplos = [
        { numero: 50, solicitante: "Paulo Lima", descricao: "Problema no servidor" },
        { numero: 30, solicitante: "Maria Silva", descricao: "Lentidão na rede" },
        { numero: 70, solicitante: "João Souza", descricao: "Sistema fora do ar" }
    ];
    enviarLote(exemplos, "Dados do exercício inseridos!");
}

function carregarDadosGrupo() {
    const equipe = [
        { numero: 80, solicitante: "Davi Sandro", descricao: "RA: 4393308" },
        { numero: 20, solicitante: "Vitória Nunes", descricao: "RA: 45594872" },
        { numero: 90, solicitante: "Nicolas Miranda", descricao: "RA: 41244109" }
    ];
    enviarLote(equipe, "Dados da equipe Commit Furioso carregados!");
}

function carregarMaterias() {
    const materias = [
        { numero: 200, solicitante: "Estruturas de Dados II", descricao: "PTA UCS - Vários Instrutores" },
        { numero: 100, solicitante: "Engenharia de Software", descricao: "PTA UCS - Vários Instrutores" },
        { numero: 300, solicitante: "Redes de Computadores", descricao: "Edidio Rubens Dantas Lima" },
        { numero: 40, solicitante: "Análise e Projeto de Sist.", descricao: "PTA UCS - Vários Instrutores" },
        { numero: 150, solicitante: "Banco de Dados", descricao: "Adriano Arrivabene" }
    ];
    enviarLote(materias, "Grade curricular do semestre carregada!");
}

function abrirEdicao(numero, solicitante, descricao) {
    document.getElementById('editNumero').value = numero;
    document.getElementById('editSolicitante').value = solicitante;
    document.getElementById('editDescricao').value = descricao;
    modalEdicao.show();
}

async function salvarEdicao() {
    const numero = document.getElementById('editNumero').value;
    const payload = {
        solicitante: document.getElementById('editSolicitante').value,
        descricao: document.getElementById('editDescricao').value
    };

    const res = await fetch(`/solicitacoes/${numero}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    const resultado = await res.json();
    mostrarToast(resultado.mensagem);

    if (res.ok) {
        modalEdicao.hide();
        carregarTudo();
    }
}

function abrirModalRemocao(numero) {
    numeroParaRemover = numero;
    document.getElementById('remocaoNumeroTxt').innerText = numero;
    modalRemocao.show();
}

async function confirmarRemocao() {
    const res = await fetch(`/solicitacoes/${numeroParaRemover}`, { method: 'DELETE' });
    const resultado = await res.json();
    mostrarToast(resultado.mensagem);
    
    modalRemocao.hide();
    if (res.ok) carregarTudo();
}