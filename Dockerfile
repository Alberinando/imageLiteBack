# ====================================================
# Etapa 1: Build da aplicação usando imagem com Maven e JDK
# ====================================================
FROM maven:3.8.5-openjdk-17 AS build
# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de configuração do Maven para gerenciar as dependências
COPY pom.xml .
# Baixa as dependências definidas no pom.xml (cache layer para otimização)
RUN mvn dependency:go-offline

# Copia o código-fonte da aplicação para o container
COPY src ./src

# ====================================================
# Etapa 2: Criação da imagem final utilizando apenas o JDK para execução
# ====================================================
FROM openjdk:17-jdk-slim
# Define o diretório de trabalho para a aplicação
WORKDIR /app

# Copia o arquivo .jar gerado na etapa anterior para a imagem final
# Aqui assumimos que o .jar gerado está na pasta target com qualquer nome (use um nome específico se preferir)
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta que a aplicação utilizará (padrão do Spring Boot é 8080)
EXPOSE 8080

# Define a variável de ambiente para que o Spring Boot use o perfil desejado, se necessário
# ENV SPRING_PROFILES_ACTIVE=prod

# Comando de inicialização da aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
