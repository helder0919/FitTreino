# FitTreino

Aplicativo Android desenvolvido em Kotlin com Jetpack Compose para atender os requisitos do trabalho de grupo sobre persistência local com Room e consumo de API externa com Retrofit.

## Objetivo

O projeto apresenta quatro telas principais:

- Luiz Edson — dados locais com Room
- Luiz Edson — dados externos com Retrofit
- Hélder Vinícius — dados locais com Room
- Hélder Vinícius — dados externos com Retrofit

Cada integrante usa sua própria tabela no banco local e um endpoint diferente da API externa.

## Requisitos atendidos

### 1) Desenvolvimento da tela
- O aplicativo contém telas separadas para cada integrante.
- Cada membro implementa sua própria interface visual.
- As telas não são de cadastro nem atualização de dados.

### 2) Banco de dados local (Room)
- Cada membro usa sua própria tabela no banco SQLite local.
- Acesso ao banco realizado com Room.
- O acesso é feito por coroutines.

### 3) API externa (Retrofit)
- Consumo de API externa com Retrofit.
- Cada integrante usa uma rota diferente da API.
- O consumo da API também é realizado em coroutines.

### 4) Coroutines
- Os acessos ao banco local e às chamadas de rede foram feitos com coroutines.

## Tecnologias usadas

- Kotlin
- Jetpack Compose
- Room
- Retrofit
- Gson
- Coroutines

## Estrutura do projeto

- `app/src/main/java/com/example/fittreino/MainActivity.kt` — activity principal
- `app/src/main/java/com/example/fittreino/ui/FitTreinoInterface.kt` — telas do app
- `app/src/main/java/com/example/fittreino/data/FitTreinoDatabase.kt` — banco Room
- `app/src/main/java/com/example/fittreino/data/api/FitTreinoApiService.kt` — configuração da API

## Como rodar

1. Abra o projeto no Android Studio.
2. Aguarde o Gradle sincronizar.
3. Conecte um emulador ou dispositivo Android.
4. Clique em Run > Run 'app'.

## API utilizada

Foi usada a API pública JSONPlaceholder:

- https://jsonplaceholder.typicode.com/

Rotas usadas:

- Luiz Edson: `/todos`
- Hélder Vinícius: `/posts`

## Observações

- O projeto foi validado com build do tipo `assembleDebug`.
- O app está pronto para execução local no ambiente Android Studio.

## GitHub

Para enviar o projeto pelo Classroom, basta publicar este repositório no GitHub e colar o link no ambiente de entrega.
