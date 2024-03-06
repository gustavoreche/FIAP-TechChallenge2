# FIAP-TechChallenge2
Segundo desafio da pós graduação da FIAP

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