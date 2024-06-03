import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Необходимо реализовать на языке Java (можно использовать 17 версию) класс для работы с API Честного знака. Класс
 * должен быть thread-safe и поддерживать ограничение на количество запросов к API. Ограничение указывается в
 * конструкторе в виде количества запросов в определенный интервал времени. Например:
 * public CrptApi(TimeUnit timeUnit, int requestLimit)
 * timeUnit – указывает промежуток времени – секунда, минута и пр.
 * requestLimit – положительное значение, которое определяет максимальное количество запросов в этом промежутке времени.
 * <p>
 * При превышении лимита запрос вызов должен блокироваться, чтобы не превысить максимальное количество запросов к API
 * и продолжить выполнение, без выбрасывания исключения, когда ограничение на количество вызов API не будет превышено
 * в результате этого вызова. В любой ситуации превышать лимит на количество запросов запрещено для метода.
 * <p>
 * Реализовать нужно единственный метод – Создание документа для ввода в оборот товара, произведенного в РФ. Документ
 * и подпись должны передаваться в метод в виде Java объекта и строки соответственно.
 * <p>
 * Вызывается по HTTPS метод POST следующий URL:
 * https://ismp.crpt.ru/api/v3/lk/documents/create
 * <p>
 * В теле запроса передается в формате JSON документ: {"description": { "participantInn": "string" }, "doc_id":
 * "string", "doc_status": "string", "doc_type": "LP_INTRODUCE_GOODS", 109 "importRequest": true, "owner_inn":
 * "string", "participant_inn": "string", "producer_inn": "string", "production_date": "2020-01-23",
 * "production_type": "string", "products": [ { "certificate_document": "string", "certificate_document_date":
 * "2020-01-23", "certificate_document_number": "string", "owner_inn": "string", "producer_inn": "string",
 * "production_date": "2020-01-23", "tnved_code": "string", "uit_code": "string", "uitu_code": "string" } ],
 * "reg_date": "2020-01-23", "reg_number": "string"}
 * <p>
 * При реализации можно использовать библиотеки HTTP клиента, JSON сериализации. Реализация должна быть максимально
 * удобной для последующего расширения функционала.
 * <p>
 * Решение должно быть оформлено в виде одного файла CrptApi.java. Все дополнительные классы, которые используются
 * должны быть внутренними.
 * <p>
 * Можно прислать ссылку на файл в GitHub.
 * В задании необходимо просто сделать вызов указанного метода, реальный API не должен интересовать.
 */
public class CrptApi {
    private final TimeUnit timeUnit; // Единица измерения времени для управления лимитом запросов
    private final int requestLimit; // Максимальное количество запросов за определенный период времени
    private final Object lock = new Object(); // Объект блокировки для синхронизации потоков
    private final AtomicInteger requestCount = new AtomicInteger(0); // Счетчик количества запросов
    private long lastRequestTime = System.currentTimeMillis(); // Время последнего запроса

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public static void main(String[] args) {
        CrptApi api = new CrptApi(TimeUnit.SECONDS, 5);
        String signature  = "signature";
        //region Document creation emulation
        Document document  = new Document(
                "doc_id",
                "doc_status",
                "LP_INTRODUCE_GOODS",
                true,
                "owner_inn",
                "participant_inn",
                "producer_inn",
                "production_date",
                "production_type",
                List.of(
                        new Document.Product(
                                "certificate_document",
                                "certificate_document_date",
                                "certificate_document_number",
                                "owner_inn",
                                "producer_inn",
                                "production_date",
                                "tnved_code",
                                "uit_code",
                                "uitu_code")
                        ),
                "reg_date",
                "reg_number"
        );
        //endregion
        api.createDocumentAndSign(document, signature, "https://ismp.crpt.ru/api/v3/lk/documents/create");
    }
    public void createDocumentAndSign(Document document, String signature, String uri){
        synchronized (lock) {
            long currentTime = System.currentTimeMillis();
            long timeDiff = currentTime - lastRequestTime;

            // Сброс счетчика
            if (timeUnit.toMillis(timeDiff) >= timeUnit.toMillis(1)) {
                requestCount.set(0);
                lastRequestTime = currentTime;
            }
            // Ожидание
            if (requestCount.get() >= requestLimit) {
                long sleepTime = timeUnit.toMillis(1) - timeDiff;
                if (sleepTime > 0) {
                    try {
                        lock.wait(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            sendPOSTRequestToApi(document, signature, uri);
            requestCount.incrementAndGet();
            lastRequestTime = System.currentTimeMillis();
        }
    }

    private void sendPOSTRequestToApi(Document document, String signature, String uri){
        ObjectMapper objectMapper = new ObjectMapper();

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Authorization", signature)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(document)))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Document {
        private String doc_id;
        private String doc_status;
        private String doc_type;
        private boolean importRequest;
        private String owner_inn;
        private String participant_inn;
        private String producer_inn;
        private String production_date;
        private String production_type;
        private List<Product> products;
        private String reg_date;
        private String reg_number;

        public Document(String doc_id, String doc_status, String doc_type, boolean importRequest, String owner_inn, String participant_inn, String producer_inn, String production_date, String production_type, List<Product> products, String reg_date, String reg_number) {
            this.doc_id = doc_id;
            this.doc_status = doc_status;
            this.doc_type = doc_type;
            this.importRequest = importRequest;
            this.owner_inn = owner_inn;
            this.participant_inn = participant_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.production_type = production_type;
            this.products = products;
            this.reg_date = reg_date;
            this.reg_number = reg_number;
        }

        private static class Product {
            private String certificate_document;
            private String certificate_document_date;
            private String certificate_document_number;
            private String owner_inn;
            private String producer_inn;
            private String production_date;
            private String tnved_code;
            private String uit_code;
            private String uitu_code;

            public Product(String certificate_document, String certificate_document_date, String certificate_document_number, String owner_inn, String producer_inn, String production_date, String tnved_code, String uit_code, String uitu_code) {
                this.certificate_document = certificate_document;
                this.certificate_document_date = certificate_document_date;
                this.certificate_document_number = certificate_document_number;
                this.owner_inn = owner_inn;
                this.producer_inn = producer_inn;
                this.production_date = production_date;
                this.tnved_code = tnved_code;
                this.uit_code = uit_code;
                this.uitu_code = uitu_code;
            }
        }
    }
}
