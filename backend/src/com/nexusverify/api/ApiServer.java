
package com.nexusverify.api;

import com.nexusverify.checker.FakeNewsChecker;
import com.nexusverify.checker.LinkChecker;
import com.nexusverify.checker.SpamChecker;
import com.nexusverify.checker.VerificationChecker;
import com.nexusverify.model.VerificationResult;
import com.nexusverify.service.HistoryManager;
import com.nexusverify.service.RiskCalculator;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiServer {

    private static final HistoryManager historyManager =
            new HistoryManager();

    private static final RiskCalculator riskCalculator =
            new RiskCalculator();

    private static final FakeNewsChecker newsChecker =
            new FakeNewsChecker();

    private static final SpamChecker spamChecker =
            new SpamChecker();

    private static final LinkChecker linkChecker =
            new LinkChecker();

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0);

        server.createContext("/api/verify", ApiServer::verify);
        server.createContext("/api/history", ApiServer::history);

        server.start();

        System.out.println("==================================");
        System.out.println("       NEXUS VERIFY API");
        System.out.println("==================================");
        System.out.println("Server running on port 8080");
        System.out.println("API: http://localhost:8080");
        System.out.println("==================================");
    }

    private static void verify(HttpExchange exchange)
            throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendJson(exchange, 405,
                    "{\"error\":\"Method not allowed\"}");
            return;
        }

        try {

            String body = readBody(exchange);

            String type = extractValue(body, "type");
            String input = extractValue(body, "input");

            if (type == null || input == null || input.trim().isEmpty()) {
                sendJson(exchange, 400,
                        "{\"error\":\"Type and input are required\"}");
                return;
            }

            VerificationChecker checker;

            switch (type.toLowerCase()) {

                case "news":
                    checker = newsChecker;
                    break;

                case "spam":
                    checker = spamChecker;
                    break;

                case "link":
                    checker = linkChecker;
                    break;

                default:
                    sendJson(exchange, 400,
                            "{\"error\":\"Invalid verification type\"}");
                    return;
            }

            VerificationResult result = checker.check(input);

            historyManager.saveHistory(input, result);

            int score = result.getRiskScore();

            String json = "{"
                    + "\"category\":\"" + escapeJson(result.getCategory()) + "\","
                    + "\"riskScore\":" + score + ","
                    + "\"suspicious\":" + result.isSuspicious() + ","
                    + "\"message\":\"" + escapeJson(result.getMessage()) + "\","
                    + "\"riskLevel\":\"" + riskCalculator.getRiskLevel(score) + "\","
                    + "\"recommendation\":\""
                    + escapeJson(riskCalculator.getRecommendation(score)) + "\""
                    + "}";

            sendJson(exchange, 200, json);

        } catch (Exception e) {

            sendJson(exchange, 500,
                    "{\"error\":\"Verification failed\"}");
        }
    }

    private static void history(HttpExchange exchange)
            throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            List<String> records = historyManager.getHistory();

            StringBuilder json = new StringBuilder("[");
            
            for (int i = 0; i < records.size(); i++) {

                if (i > 0) {
                    json.append(",");
                }

                json.append("\"")
                    .append(escapeJson(records.get(i)))
                    .append("\"");
            }

            json.append("]");

            sendJson(exchange, 200, json.toString());

        } else if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {

            historyManager.clearHistory();

            sendJson(exchange, 200,
                    "{\"message\":\"History cleared successfully\"}");

        } else {

            sendJson(exchange, 405,
                    "{\"error\":\"Method not allowed\"}");
        }
    }

    private static String readBody(HttpExchange exchange)
            throws IOException {

        InputStream stream = exchange.getRequestBody();

        return new String(
                stream.readAllBytes(),
                StandardCharsets.UTF_8
        );
    }

    private static String extractValue(String json, String key) {

        String regex = "\"" + Pattern.quote(key)
                + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"";

        Matcher matcher = Pattern.compile(regex).matcher(json);

        if (matcher.find()) {
            return unescapeJson(matcher.group(1));
        }

        return null;
    }

    private static String unescapeJson(String value) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < value.length(); i++) {

            char ch = value.charAt(i);

            if (ch == '\\' && i + 1 < value.length()) {

                char next = value.charAt(++i);

                switch (next) {

                    case 'n': result.append('\n'); break;
                    case 'r': result.append('\r'); break;
                    case 't': result.append('\t'); break;
                    case '"': result.append('"'); break;
                    case '\\': result.append('\\'); break;
                    case '/': result.append('/'); break;

                    case 'u':
                        if (i + 4 < value.length()) {
                            String hex = value.substring(i + 1, i + 5);
                            result.append((char) Integer.parseInt(hex, 16));
                            i += 4;
                        }
                        break;

                    default:
                        result.append(next);
                }

            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static void addCorsHeaders(HttpExchange exchange) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin", "*");

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, DELETE, OPTIONS");

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type");
    }

    private static void sendJson(
            HttpExchange exchange,
            int status,
            String response) throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type", "application/json; charset=UTF-8");

        exchange.sendResponseHeaders(status, bytes.length);

        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
