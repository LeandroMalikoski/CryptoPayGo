## ⚠️ Aviso

Este projeto foi desenvolvido apenas para fins **educacionais** e **experimentação com tecnologias** como Java, Spring Boot e integração com APIs externas.

Não se destina a uso em produção nem a transações reais com criptomoedas.

# 🚀 Crypto Pay Go

**Crypto Pay Go** é uma aplicação de gerenciamento de estoque para uma loja geral, com diferencial: permite realizar registros de movimentações de produtos utilizando **criptomoedas** como forma de pagamento 💸🪙. A conversão é feita em tempo real via integração com a **API da CoinMarketCap**.

---

## 🛠️ Tecnologias utilizadas

- ☕ **Java 21**
- 🌱 **Spring Boot**
- 🗃️ **MySQL**
- 🔐 **JWT** para autenticação
- 🔄 **Flyway** para versionamento de banco de dados
- 📘 **Swagger** para documentação da API
- 📦 **Lombok** para reduzir boilerplate
- 🌐 **CoinMarketCap API** para conversão de moedas em tempo real

📡 API Utilizada
Este projeto utiliza a API da CoinMarketCap para obtenção de informações em tempo real sobre criptomoedas, como taxas de conversão e preços atualizados.

🔗 Documentação oficial: https://coinmarketcap.com/api/documentation/

---

## 🧠 Funcionalidades

- 📦 Cadastro de produtos
- 📈 Entrada e saída de estoque
- 💰 Pagamento com criptomoedas
- 🔄 Conversão de valores em tempo real
- 👤 Autenticação e autorização com JWT
- 🔍 Consulta de histórico de movimentações
- 🧾 Documentação da API com Swagger UI

---

## 🛠️ Como rodar o projeto

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/crypto-pay-go.git
   cd crypto-pay-go

2. **Configure o application.properties**
3. ```bash
    spring.datasource.url=jdbc:mysql://${SPRING_DATASOURCE_HOST}/cryptotest?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
    spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
    coinmarketcap.apikey=${API_KEY}
Crie sua chave API gratuita em: https://coinmarketcap.com/api/

## 📸 Swagger UI

Após rodar o projeto, acesse:  
`http://localhost:8080/swagger-ui.html`

---

## 👨‍💻 Desenvolvedor
Feito por Leandro, estudante de Análise e Desenvolvimento de Sistemas.
