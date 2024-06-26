package test_tasks.selsup;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс для работы с API Честного знака
 */
public class CrptApi {
    /**
     * URL адрес для API-запроса типа POST на создание документа для ввода в оборот товара, произведенного в РФ.
     */
    public static final String API_URL_CREATE_DOCUMENT_FOR_PRODUCTS_MADE_IN_RF =
            "https://ismp.crpt.ru/api/v3/lk/documents/create";
    /**
     * Промежуток времени.
     */
    private final TimeUnit timeUnit;
    /**
     * Максимальное количество запросов.
     */
    private final int requestLimit;
    /**
     * Счетчик количества запросов.
     */
    private int requestCount = 0;
    /**
     * Время последнего запроса.
     */
    private long lastRequestTime = System.currentTimeMillis();
    /**
     * Блокиратор выполнения.
     */
    public static final Lock lock = new ReentrantLock();

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public static void main(String[] args) {
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 5);

        for (int i = 0; i < 10; i++) {
            new Thread(new Worker(crptApi)).start();
        }
    }

    record Worker(CrptApi crptApi) implements Runnable {
        @Override
        public void run() {
            String signature = crptApi.getSignatureFromSomewhere();
            Document document = crptApi.getDocumentFromSomewhere();
            crptApi.createDocumentForProductMadeInRF(document, signature);
        }
    }

    private void createDocumentForProductMadeInRF(Document document, String signature) {
        lock.lock();
        // если счётчик превышен
        if (requestCount >= requestLimit) {
            long sleepTime = timeUnit.toMillis(1) - (System.currentTimeMillis() - lastRequestTime);
            // проверим время и поспим если ещё мало прошло
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            // можно обнулить счётчик
            requestCount = 0;
        }
        sendPOSTRequestToApi(document, signature, API_URL_CREATE_DOCUMENT_FOR_PRODUCTS_MADE_IN_RF);
        requestCount++;
        lastRequestTime = System.currentTimeMillis();
        lock.unlock();
    }

    private Document getDocumentFromSomewhere() {
        return new Document(new Document.Description("string"), "string", "string", "LP_INTRODUCE_GOODS", true,
                "string", "string", "string", "2020-01-23", "string", List.of(new Product("string", "2020-01-23",
                "string", "string", "string", "2020-01-23", "string", "string", "string")), "2020-01-23", "string");
    }

    private String getSignatureFromSomewhere() {
        return "signature";
    }

    private void sendPOSTRequestToApi(Document document, String signature, String uri) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Authorization", signature)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers
                            .ofString(objectMapper.writeValueAsString(document)))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DTO документа для работы с API Честного знака
     */
    public record Document(Description description, String doc_id, String doc_status, String doc_type,
                           boolean importRequest, String owner_inn, String participant_inn, String producer_inn,
                           String production_date, String production_type, List<Product> products, String reg_date,
                           String reg_number) {
        public record Description(String participantInn) {
        }
    }

    /**
     * DTO продукта для работы с API Честного знака
     */
    public record Product(String certificate_document, String certificate_document_date,
                          String certificate_document_number, String owner_inn, String producer_inn,
                          String production_date, String tnved_code, String uit_code, String uitu_code) {
    }

}