package test_tasks.idea_platform;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Задание
 * Напишите программу на языке программирования java, которая прочитает файл tickets.json и рассчитает:
 * - Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика
 * - Разницу между средней ценой  и медианой для полета между городами Владивосток и Тель-Авив
 * <p>
 * Программа должна вызываться из командной строки Linux, результаты должны быть представлены в текстовом виде.
 */
public class TestTaskForIdeaPlatform {

    public static final String FILE_PATH = "./resources/tickets.json";
    public static final String ROOT_NODE = "tickets";
    public static final String ORIGIN = "VVO";
    public static final String DESTINATION = "TLV";

    public static void main(String[] args) throws IOException {
        List<Ticket> tickets = jsonToTicketsList(FILE_PATH);

        List<Ticket> filteredTickets =
                tickets.stream().filter(ticket -> ticket.getOrigin().equals(ORIGIN) && ticket.getDestination().equals(DESTINATION)).toList();

        Map<String, Long> minFlightTimesByCarrier =
                filteredTickets.stream()
                        .collect(Collectors.toMap(
                                Ticket::getCarrier,
                                Ticket::getFlightDuration,
                                Long::min));

        double priceDifference = getPriceDifference(filteredTickets);

        String firstCityName = filteredTickets.stream()
                .filter(ticket  -> ticket.getOrigin().equals(ORIGIN))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("City with code " + ORIGIN + " not found"))
                .getOriginName();

        String secondCityName = filteredTickets.stream()
                .filter(ticket  -> ticket.getDestination().equals(DESTINATION))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("City with code " + DESTINATION + " not found"))
                .getDestinationName();

        System.out.println("Минимальное время полета между городами " + firstCityName  +  " и "  + secondCityName  +  " для каждого авиаперевозчика:");
        System.out.println(minFlightTimesByCarrier);

        System.out.println("Разница между средней ценой и медианой для полета между городами  " + firstCityName  +  " и "  + secondCityName  +  ": " + priceDifference);
    }

    private static double getPriceDifference(List<Ticket> filteredTickets) {

        double averagePrice = filteredTickets.stream()
                .mapToInt(Ticket::getPrice)
                .average()
                .orElse(0);

        double medianPrice;

        List<Integer> prices = filteredTickets.stream()
                .mapToInt(Ticket::getPrice)
                .boxed()
                .sorted()
                .toList();

        int size = prices.size();

        if (size % 2 == 0)
            medianPrice = (prices.get(size / 2 - 1) + prices.get(size / 2)) / 2.0;
        else medianPrice = prices.get(size / 2);

        return averagePrice - medianPrice;
    }

    private static List<Ticket> jsonToTicketsList(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = mapper.readTree(new File(filePath));
        JsonNode ticketsNode = rootNode.path(ROOT_NODE);

        return mapper.readValue(ticketsNode.toString(), mapper.getTypeFactory().constructCollectionType(List.class,
                Ticket.class));
    }

}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
class Ticket {
    @JsonProperty("origin")
    private String origin;
    @JsonProperty("origin_name")
    private String originName;
    @JsonProperty("destination")
    private String destination;
    @JsonProperty("destination_name")
    private String destinationName;
    @JsonProperty("departure_date")
    private String departureDate;
    @JsonProperty("departure_time")
    private String departureTime;
    @JsonProperty("arrival_date")
    private String arrivalDate;
    @JsonProperty("arrival_time")
    private String arrivalTime;
    @JsonProperty("carrier")
    private String carrier;
    @JsonProperty("stops")
    private int stops;
    @JsonProperty("price")
    private int price;


    public long getFlightDuration() {
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM.yy");
        LocalTime departure = LocalTime.parse(this.departureTime, formatterTime(this.departureTime));
        LocalDate departureDate = LocalDate.parse(this.departureDate, formatterDate);
        LocalTime arrival = LocalTime.parse(this.arrivalTime, formatterTime(this.departureTime));
        LocalDate arrivalDate = LocalDate.parse(this.arrivalDate, formatterDate);

        long days = arrivalDate.toEpochDay() - departureDate.toEpochDay();
        long minutes = departure.until(arrival, java.time.temporal.ChronoUnit.MINUTES);

        return (days * 24 * 60) + minutes;
    }

    private DateTimeFormatter formatterTime(String departureTime) {
        int hoursDigits = departureTime.split(":")[0].length();
        int minutesDigits = departureTime.split(":")[1].length();
        String h = "H";
        String m = "M";
        if (hoursDigits == 2) h = "HH";
        if (minutesDigits == 2) m = "MM";
        return DateTimeFormatter.ofPattern(h + ":" + m);
    }
}