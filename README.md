# 🌳 Sistema de Solicitações — Árvore AVL

[![Status](https://img.shields.io/badge/Status-Online-success?style=for-the-badge&color=E14017)](https://avl-sistema-solicitacao.onrender.com/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)]()
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)]()

> **🌐 Aceda à aplicação online:** [avl-sistema-solicitacao.onrender.com](https://avl-sistema-solicitacao.onrender.com/)

Este projeto é uma aplicação web desenvolvida para a disciplina de Estruturas de Dados. Implementa uma **Árvore AVL** (Adelson-Velsky e Landis) no backend para o armazenamento, balanceamento dinâmico e gestão de solicitações de uma organização.

## 🚀 Funcionalidades

*   **Inserção Dinâmica:** Registo de novas solicitações com chave única, nome do solicitante e descrição.
*   **Auto-balanceamento:** O motor Java recalcula os Fatores de Balanceamento (FB) e executa rotações (LL, RR, LR, RL) automaticamente a cada inserção.
*   **Visualização em Tempo Real:** Interface gráfica responsiva (Dark Mode) que renderiza a estrutura de diretórios da árvore com as cores dos nós baseadas no seu Fator de Balanceamento.
*   **Percurso Em Ordem:** Listagem tabular e em tempo real de todos os nós ordenados pela chave (Nº da Solicitação).

## 🛠️ Tecnologias Utilizadas

*   **Backend:** Java 17, Spring Boot, Maven
*   **Frontend:** HTML5, CSS3 (Custom Dark Theme "Commit Furioso"), JavaScript, Bootstrap 5
*   **Deploy:** Render (Docker)

## 💻 Equipa: Commit Furioso
*   Davi Sandro
*   Nicolas Miranda
*   Vitória Nunes
---
*Projeto académico desenvolvido em outubro de 2026.*

**Equipe:** Commit Furioso  
*Onde todo problema vira projeto.*

## 🎨 Identidade Visual
O design da interface foi elaborado seguindo a identidade **Commit Furioso**:
- Preto Profundo (`#0D0A0F`)
- Laranja Claro (`#EB7438`)
- Vermelho (`#95160C`)
- Cinza Aço (`#484D56`)
- Tipografia: Inter e Courier New (para a renderização textual da AVL).

## 🛠️ Funcionalidades Desenvolvidas (Projeto 2)
1. Inserção de novos atributos (`solicitante` e `dataAbertura` gerada automaticamente).
2. Endpoint `PUT` para atualização de dados satélites (`solicitante` e `descricao`) sem modificar a chave.
3. Visualização da Árvore AVL em tempo real na interface Web.
4. Tabela com percurso `Em Ordem` contendo as ações de edição e remoção.

---

## 📚 Análise Teórica da Árvore AVL (Exercício 14)

**Teste realizado:** Alteração dos dados (solicitante e descrição) do nó referente à solicitação **Nº 50**.

**a) A posição do nó 50 foi modificada?**  
**Não.** Como o número da solicitação (a chave da árvore) permaneceu o mesmo, o nó continua exatamente na mesma posição estrutural na árvore de busca.

**b) A altura da árvore foi modificada?**  
**Não.** A altura só é alterada quando a estrutura da árvore muda (inserção de novos níveis ou remoção de nós). Apenas os dados internos do nó existente foram atualizados.

**c) O fator de balanceamento de algum nó foi alterado?**  
**Não.** O fator de balanceamento (FB) é calculado exclusivamente pela diferença de altura entre as subárvores esquerda e direita. Como a estrutura permaneceu intacta, os fatores não sofrem alteração.

**d) Foi necessária alguma rotação?**  
**Não.** As rotações (LL, RR, LR, RL) são necessárias apenas quando uma operação de inserção ou remoção desbalanceia a árvore (deixando o FB de algum nó maior que 1 ou menor que -1).

**e) Por que podemos alterar `solicitante` e `descricao` sem reorganizar a AVL?**  
Porque esses atributos são considerados **dados satélites**. A Árvore AVL utiliza exclusivamente a **chave** (neste caso, o `numero` da solicitação) para realizar as comparações de `maior que` ou `menor que` e definir a organização estrutural. Alterar o conteúdo extra do nó não afeta a propriedade de busca binária.

**f) Por que a alteração do atributo `numero` seria diferente?**  
Porque o `numero` dita a regra de organização (menores à esquerda, maiores à direita). Se tentássemos alterar o número de um nó diretamente, ele poderia acabar em uma posição inválida na árvore, corrompendo a estrutura de busca. Para "alterar" a chave, é obrigatório **remover** o nó atual e **inserir** um novo nó, o que desencadearia novos cálculos de altura, fatores de balanceamento e possíveis rotações.