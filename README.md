# FacebookRobot

### Funcionalidades do robo:

- Percorre grupos e páginas do facebook e curti postagens.
- Salva logs e tira screenshot das publicações curtidas.
- Salva localmente a quantidade de curtidas em banco sqlite.
- Compartilha uma publicação específica em vários grupos pré-definidos

### Parametros:

Recebe dois parâmetros:

- Primeiro parâmetro é opcional quando não passa o segundo parâmetro, tem valor padrão igual EXECUTAR_NAVEGADOR_ABERTO.
  Aceita valores:
    - SOMENTE_LOGAR
        - Apenas faz login com o navegador aberto de acordo com a configuração do segundo parâmetro.
    - EXECUTAR_NAVEGADOR_ABERTO
        - Faz login com navegador aberto e executa ações de acordo com a configuração do segundo parâmetro.
    - EXECUTAR_NAVEGADOR_FECHADO
        - Faz login com navegador fechado e executa ações de acordo com a configuração do segundo parâmetro.
- Segundo parâmetro é um arquivo json obrigatório, caso seja omitido no comando, abrirá um popup pedindo o arquivo.json.
  Veja sessão Modelo de arquivo json

### Modelo de arquivo json para curtir:

- Descrição das chaves do json:
    - email: E-mail da conta do facebook.
    - passwd: Senha da conta do facebook, senha em texto aberto.
    - paginas: Lista de objetos que indicam a página ou grupo que será percorrido.
        - nome: Nome da página ou grupo. Utilizado apenas em logs informativos.
        - url: Url da página ou grupo do facebook.
        - publicacoesXpath: É o identificador utilizado para selecionar as publicações na árvore dom da página facebook.
          Hoje a xpath de exemplo funciona. Mas o facebook atualiza o visual da página, provavelmente esse xpath para de
          funcionar, o que obriga encontrar outro.
        - naoCurtirPalavras: São palavras excludentes ao decidir curtir a publicação. Sugiro manter as três do exemplo.
          É possível adicionar mais. Por exemplo, em periodo eleitoral pode ser interresante colocar nomes dos
          candidados, caso não queira curtir esse tipo de conteúdo.

```
{
    "email": "contafacebook@mail.com",
    "passwd": "senhaDaContaEmTextoAbertoMesmo",
    "paginas": [
        {
            "nome": "Nome do grupo ou página do facebook",
            "url": "https://www.facebook.com/groups/123456789",
            "publicacoesXpath": "//*[@class=\"du4w35lb k4urcfbm l9j0dhe7 sjgh65i0\"]",
            "naoCurtirPalavras": [ "patrocinado", "sugerido", "dinheiro"]
        },
        {
            "nome": "Outro grupo",
            "url": "https://www.facebook.com/groups/abcdefghij",
            "publicacoesXpath": "//*[@class=\"du4w35lb k4urcfbm l9j0dhe7 sjgh65i0\"]",
            "naoCurtirPalavras": [ "patrocinado", "sugerido", "dinheiro"]
        }
    ]
}
```

### Modelo de arquivo json para compartilhar:

- Descrição das chaves do json:
    - email: E-mail da conta do facebook.
    - passwd: Senha da conta do facebook, senha em texto aberto.
    - compartilhavel: Objeto que define o que será compartilhado e em quais grupos será compartilhado.
        - url: Url permanente da publicação que será compartilhado.
        - incluirPubOriginal: Indica se é para checar a opção incluir publicação original.
        - textoPublicacao: Nem sempre é possível incluir publicação original, este texto será usado quando não for
          possível incluir publicação original.
        - compartilharComo: Nome do usuário ou da página que será compartilhado como.
        - nomesGrupos: Array com os nomes dos grupos que a publicação será compartilhada.

```
{
    "email": "contafacebook@mail.com",
    "passwd": "senhaDaContaEmTextoAbertoMesmo",
    "compartilhavel": {
        "url": "https://www.facebook.com/story.php?story_fbid=123456789012345&id=987654321098765",
        "incluirPubOriginal": true,
        "textoPublicacao": "Homem-Aranha Verde feito com o Krita!!!",
        "compartilharComo": "NomeUsuarioOuPagina",
        "nomesGrupos": [
            "Nome do grupo 1",
            "Nome do grupo 2"
        ]
    }
}
```

### Como usar:

#### Para compilar o arquivo jar:

- Execute na raiz do projeto:

```
mvn clean compile assembly:single
```

#### Para executar o arquivo jar:

- Via comando passando os dois parâmetros.

```
java -jar facebookRobot-1.0-SNAPSHOT-jar-with-dependencies.jar SOMENTE_LOGAR conta.json
```

- Via comando sem nenhum parâmetro, neste caso abre um popup solicitando o segundo parametro, que é o arquivo json.

```
java -jar facebookRobot-1.0-SNAPSHOT-jar-with-dependencies.jar
```