package com.example.coronatracker.services;

import com.example.coronatracker.models.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service        //To make this the spring service, we need this
public class CoronaVirusDataService {
    private static String DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> everyStats = new ArrayList<>();

    public List<LocationStats> getEveryStats() {
        return everyStats;
    }

    @PostConstruct
    //This is telling spring that when you construct this instance of service, after it is done, execute this method.
    @Scheduled(cron = "* * 1 * * *")     //This will update the method. Goes like: seconds, minute, hour, day, month, year. This does first hour of every day
    // * * * * * * means every seconds.
    public void fetchCovidData() throws IOException, InterruptedException {         //To make a get request to the URL

        List<LocationStats> newStats = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        StringReader csvReader = new StringReader(httpResponse.body());


        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvReader);
        for (CSVRecord record : records) {

            LocationStats locationStats = new LocationStats();

            locationStats.setState(record.get("Province/State"));
            locationStats.setCountry(record.get("Country/Region"));
            int latestCases = Integer.parseInt(record.get(record.size()-1));
            int prevDayCases = Integer.parseInt(record.get(record.size()-2));
            locationStats.setLatestReportedCases(latestCases);
            locationStats.setDelta(latestCases - prevDayCases);

//            System.out.println(locationStats.toString());

            newStats.add(locationStats);
        }
        this.everyStats = newStats;
    }

}
