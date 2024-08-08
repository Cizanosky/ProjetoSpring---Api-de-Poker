<div align="center">
  <h1> PROJETO SPRING E MOCKITO </h1>
  <h4> API Poker </h4> 
</div>

<div align="center">
  <img src="https://github.com/Cizanosky/ProjetoSpring---Api-de-Poker/blob/main/gifPoker.gif" alt="Foto das maos do poker" width="1000">
</div>

# API de Controle de Partida de Poker Texas Hold'em  ♣ ♥ ♠ ♦

Este projeto consiste em uma API desenvolvida em Spring para gerenciar partidas de poker Texas Hold'em entre dois jogadores, incluindo o controle de rodadas, apostas e a determinação do vencedor. O sistema permite que os jogadores enfrentem-se em uma partida dinâmica e envolvente.

## Funcionamento do Sistema

### Estrutura do Jogo

No Texas Hold'em, cada jogador recebe duas cartas e utiliza essas cartas em combinação com as cartas comunitárias para formar a melhor mão possível. Tradicionalmente, são distribuídas cinco cartas comunitárias, mas este projeto utiliza apenas três cartas comunitárias. Essas cartas, combinadas com as cartas na mão do jogador, totalizam cinco cartas para a formação da mão.

### Sequência do Jogo

1. **Criação da Partida**: Ao iniciar uma partida, são distribuídas as cartas iniciais para os jogadores e as cartas comunitárias são preparadas, porém ainda não visíveis.
2. **Rodadas e Apostas**: Cada jogador, em sua vez, pode fazer uma aposta, passar a vez, desistir ou dar all-in. Se um jogador fizer uma aposta, o adversário deve cobrir a aposta, desistir ou aumentar a aposta. Após todas as apostas serem feitas, a primeira carta comunitária é revelada.
3. **Revelação das Cartas Comunitárias**: Após cada rodada de apostas, a próxima carta comunitária é revelada, e os jogadores podem fazer novas apostas.
4. **Determinação do Vencedor**: Quando todas as cartas comunitárias forem reveladas, o sistema verifica as mãos dos jogadores e determina o vencedor da rodada, atribuindo todas as fichas apostadas ao vencedor. A partida continua até que um dos jogadores perca todas as suas fichas, momento em que a partida é encerrada.

### Combinações de Mãos no Poker

<table style="width: 100%; table-layout: fixed;">
  <tr>
    <td style="width: 70%;">
      <section>
        <h3>As combinações de mãos no poker, listadas do mais alto para o mais baixo valor, são:</h3>
        <h5>
          1. Royal Flush: Sequência de cinco cartas do mesmo naipe do 10 ao Ás.<br><br>
          2. Straight Flush: Sequência de cinco cartas do mesmo naipe.<br><br>
          3. Four of a Kind: Quatro cartas do mesmo valor.<br><br>
          4. Full House: Três cartas de um valor e duas cartas de outro valor.<br><br>
          5. Flush: Cinco cartas do mesmo naipe, não em sequência.<br><br>
          6. Straight: Cinco cartas em sequência, não do mesmo naipe.<br><br>
          7. Three of a Kind: Três cartas do mesmo valor.<br><br>
          8. Two Pair: Dois pares de cartas do mesmo valor.<br><br>
          9. One Pair: Um par de cartas do mesmo valor.<br><br>         
          10. High Card: A carta de maior valor quando nenhuma das combinações acima é formada.<br><br>
        <h5>
      </section>
    </td>
    <td style="width: 30%;">
      <img src="ProjetoSpring---Api-de-Poker/pokerhands.jpg" alt="Foto das maos do poker" width="500">
    </td>
  </tr>
</table>

### Segurança

A segurança das informações dos jogadores é garantida por meio do Spring Security, assegurando que a mão de um jogador não seja visível para o adversário. Além disso, o sistema implementa as seguintes medidas e funcionalidades para garantir uma experiência segura e justa:

- Visibilidade Restrita das Mãos: Cada jogador só pode ver suas próprias cartas. As cartas dos adversários são completamente ocultas para manter a integridade do jogo.

- Controle de Ações: Jogadores não têm permissão para atualizar, deletar ou visualizar as mãos dos adversários. Todas as ações sensíveis são restritas apenas às operações permitidas.

- Administração de Partidas: Apenas usuários com a função de administrador (ADMIN) têm permissão para controlar e administrar partidas, incluindo a criação, início e gerenciamento de jogos. Isso assegura que as partidas sejam administradas de forma justa e imparcial.

- Validação Rigorosa de Apostas: O sistema inclui uma lógica robusta para validação de apostas, garantindo que todas as apostas sejam legítimas e sigam as regras definidas. Isso inclui a verificação de valores negativos, valores menores que a aposta atual e valores que são pelo menos o dobro da aposta atual.

- Controle de Rodadas e Apostas: O sistema controla rigorosamente as rodadas e apostas, assegurando que a sequência de jogadas siga as regras estabelecidas. As variáveis como currentPlayer, potValue e currentAmount são monitoradas e atualizadas conforme necessário.

- Revelação Gradual das Cartas Comunitárias: As cartas comunitárias são reveladas progressivamente após as apostas dos jogadores, seguindo as fases do jogo de poker (flop, turn e river), garantindo uma dinâmica de jogo justa.

- Autenticação e Autorização: O sistema utiliza Spring Security para autenticação e autorização dos usuários. Apenas usuários autenticados podem acessar as funcionalidades do sistema, e as permissões são rigorosamente controladas com base nas funções dos usuários (jogadores e administradores).

- Proteção de Dados Pessoais: Informações sensíveis, como senhas, são armazenadas de forma segura usando técnicas de hashing (e.g., BCrypt), garantindo que os dados dos usuários estejam protegidos contra acessos não autorizados.

- Testes Abrangentes: O sistema inclui testes unitários abrangentes para todas as funcionalidades principais, garantindo que o comportamento esperado seja mantido e que o código seja robusto e confiável.

### Integração com API Externa

Para determinar o vencedor de cada rodada, o sistema utiliza uma API externa que realiza a verificação das mãos e retorna o ganhador da rodada.

Para rodar a API, siga os passos abaixo:

- Acesse o Repositório: Baixe o código do servidor no [Link Api GitHub](https://github.com/DaviidGilB/API_Poker).

- Abra o Projeto: Utilize uma ferramenta de sua preferência, como o VS Code. Navegue até a pasta do projeto.

- Instale Dependências: Antes de iniciar o servidor, é necessário instalar o oas-generator. As instruções para essa instalação estão detalhadas no repositório do GitHub.

- Inicie o Servidor: Dentro da pasta do projeto, execute cd server para acessar o diretório do servidor. Em seguida, rode npm start para iniciar a API.

---

---

### Tecnologias & Ferramentas Utilizadas

#### **☕ Linguagens Back-End**
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)

#### **⚒️ Ferramentas**
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

#### **🍃 Frameworks**
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

### Diagrama do Projeto
<div align="center">
  <img src="https://github.com/Cizanosky/ProjetoSpring---Api-de-Poker/blob/main/Poker_Diagrama.png" alt="Foto das maos do poker" width="1000">
</div>
