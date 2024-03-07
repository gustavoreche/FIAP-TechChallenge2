# FIAP-TechChallenge2
Segundo desafio da pós graduação da FIAP

---

## Existe 3 formas de executar o projeto
OBSERVAÇÃO: Antes e a cada execução do DOCKER, é recomendado "limpar" as coisas do seu 
DOCKER(CERTIFIQUE-SE QUE NÃO TENHA PROBLEMA EM APAGAR SUAS COISAS DO DOCKER). Execute o 
comando *"docker system prune"* para limpar seu DOCKER.

1- Localmente, podendo alterar o código(via IDE - classe TechChallenge2Application)
- O único pré requisito dessa forma de executar a aplicação, é subir o banco de dados via docker.
  - Entre no diretório **"docker-banco-de-dados"** e execute o comando *"docker-compose up --build"*

2- Localmente, podendo alterar o código(via docker)
- O único pré requisito dessa forma de executar a aplicação, é subir o banco de dados e a aplicação via docker.
  - Porém dessa forma, você tem que "buildar" a aplicação.
  - Entre no diretório **"raiz"**(onde se encontram os arquivos **Dockerfile**, **pom.xml**, etc...)
    - Execute o comando *"mvn install"* -- para buildar a aplicação.
    - Depois execute o comando *"docker-compose up --build"*.

3- Via docker(com a imagem da aplicação já pronta)
- O único pré requisito dessa forma de executar a aplicação, é subir o banco de dados e a aplicação via docker.
  - Entre no diretório **"docker-image"** e execute o comando "*docker-compose up --build*"

---

## RELATÓRIO TÉCNICO:

### Tecnologias e Ferramentas utilizadas:
- Java 17
- Maven
- Spring Boot 3.2.2
- MariaDB
- H2
- Spring Data JPA
- Spring Bean Validation
- Spring Doc Open API
- Lombok
- JUnit
- Mockito
- Docker

### Desafios encontrados
- Controlar fiscalização de multa por dia.
  - Problema: Um carro tem registro de estacionamento pago no dia 1, porém no dia 2 ele não tinha 
registro pago. Na hora de realizar a fiscalização, o carro não era multado. 
  - Solução implementada: Trabalhar com o LocalDate e LocalDateTime do Java facilitou muito esse 
controle, podendo saber se o registro que tem no banco de dados é do dia atual ou não. Podendo aplicar
corretamente a multa ou não.


- Deixar consulta de relatório de fiscalização dinâmico.
  - Problema: Para gerar um relatório das fiscalizações, os parâmetros não são obrigatórios, podendo
escolher um, dois, vários ou até mesmo nenhum. E criar apenas uma consulta no banco de dados para isso.
  - Solução implementada: Trabalhar com "Specification" do Spring Data JPA, para deixar uma única
consulta dinâmica.