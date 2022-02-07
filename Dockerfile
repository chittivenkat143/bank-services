FROM adoptopenjdk/openjdk11 as builder
WORKDIR bank-services
EXPOSE 8083
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} bank-services.jar
RUN java -Djarmode=layertools -jar bank-services.jar extract

FROM adoptopenjdk/openjdk11
WORKDIR bank-services
COPY --from=builder bank-services/dependencies/ ./
COPY --from=builder bank-services/spring-boot-loader/ ./
COPY --from=builder bank-services/snapshot-dependencies/ ./
COPY --from=builder bank-services/application/ ./
ENTRYPOINT [ "java", "org.springframework.boot.loader.JarLauncher" ]