FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY src ./src

RUN mkdir -p bin data \
    && find src -name "*.java" > sources.txt \
    && javac --add-modules jdk.httpserver -d bin @sources.txt

EXPOSE 10000

CMD ["sh", "-c", "java --add-modules jdk.httpserver -cp bin com.nexusverify.api.ApiServer"]