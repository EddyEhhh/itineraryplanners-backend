package com.scrapheap.itineraryplanner.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scrapheap.itineraryplanner.dto.AirportDetailDTO;
import com.scrapheap.itineraryplanner.model.Airport;
import com.scrapheap.itineraryplanner.repository.AirportRepository;
import com.scrapheap.itineraryplanner.util.LocalDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@Service
@Slf4j
public class FlightService {

    @Value("${api.flightOAG.key}")
    private String FLIGHT_OAG_KEY;

    @Value("${api.airportInfo.key}")
    private String AIRPORT_INFO_API;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AirportRepository airportRepository;


    public String getFlightDetails(String iataCode, String flightNumber, String departureDateString){
        LocalDate departureDate = LocalDateUtil.parseDate(departureDateString);
        String flightCodeNumber = String.join(iataCode, flightNumber);
        try {
//            String url = "https://api.oag.com/flight-instances/";
            String url = String.format("https://api.oag.com/flight-instances/?DepartureDateTime=%s&CarrierCode=%s&FlightNumber=%s&CodeType=IATA&version=v2"
                    , departureDateString, iataCode, flightNumber);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Subscription-Key", FLIGHT_OAG_KEY)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }catch (Exception e){

        }

        return null;

    }

    public AirportDetailDTO getAirportDetails(String iataCode){

        Airport airport = airportRepository.findByIata(iataCode);
        if(airport != null){
            return convertToDTO(airport);
        }

        try {
            String url = String.format("https://airport-info.p.rapidapi.com/airport?iata=%s", iataCode);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("X-RapidAPI-Key", AIRPORT_INFO_API)
                    .header("X-RapidAPI-Host", "airport-info.p.rapidapi.com")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            log.info("AIRPORT body: " + response.body().toString());
            airport = objectMapper.readValue(response.body(), Airport.class);
            log.info("AIRPORT: " + airport.toString());
            airportRepository.save(airport);

            return convertToDTO(airport);
        }catch (Exception e){

        }

        return null;
    }

    public AirportDetailDTO convertToDTO(Airport airport){
        AirportDetailDTO airportDetailDTO = AirportDetailDTO.builder()
                .id(airport.getId())
                .latitude(airport.getLatitude())
                .uct(airport.getUct())
                .city(airport.getCity())
                .country_iso(airport.getCountry_iso())
                .country(airport.getCountry())
                .county(airport.getCounty())
                .iata(airport.getIata())
                .icao(airport.getIcao())
                .location(airport.getLocation())
                .longitude(airport.getLongitude())
                .name(airport.getName())
                .state(airport.getState())
                .phone(airport.getPhone())
                .postal_code(airport.getPostal_code())
                .street_number(airport.getStreet_number())
                .street(airport.getStreet())
                .website(airport.getWebsite())
                .street(airport.getStreet())
                .build();

        return airportDetailDTO;
    }


}
