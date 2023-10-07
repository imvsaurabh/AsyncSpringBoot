package com.github.imvsaurab.asyncboot.service;

import com.github.imvsaurab.asyncboot.dto.TravellerDTO;
import com.github.imvsaurab.asyncboot.dto.TravellerResponse;
import com.github.imvsaurab.asyncboot.model.Traveller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TravellerService {

    @Async
    public CompletableFuture<TravellerResponse.AirportCode> mapPassengersForAirports(String origin, String destination, List<Traveller> travellers) {
        log.info("Mapping passenger for origin={} and destination={}", origin, destination);

        List<TravellerDTO> passengers = travellers.stream()
                .filter(traveller -> traveller.origin.equals(origin) && traveller.destination.equals(destination))
                .map(traveller -> {
                    TravellerDTO travellerDTO = new TravellerDTO();
                    travellerDTO.fullName = traveller.fullName;
                    travellerDTO.email = traveller.email;
                    travellerDTO.gender = traveller.gender;
                    return travellerDTO;
                })
                .collect(Collectors.toList());

        TravellerResponse.AirportCode airportCode = new TravellerResponse.AirportCode();
        airportCode.setOrigin(origin);
        airportCode.setDestination(destination);
        TravellerResponse.Passengers dtoPassengers = new TravellerResponse.Passengers();
        dtoPassengers.setTravellers(passengers);
        airportCode.setPassengers(dtoPassengers);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(airportCode);
    }

    @Async
    public CompletableFuture<TravellerResponse> loadTraveller(String airportCodePair, List<Traveller> travellers) throws ExecutionException, InterruptedException {
        TravellerResponse travellerResponse = new TravellerResponse();
        List<TravellerResponse.AirportCode> airportCodeList = new ArrayList<>();

        List<String> airPair = Arrays.asList(airportCodePair.split(":"));
        CompletableFuture<TravellerResponse.AirportCode> airportCodeCompletableFuture = mapPassengersForAirports(airPair.get(0), airPair.get(1), travellers);
        airportCodeList.add(airportCodeCompletableFuture.get());

        travellerResponse.setAirportCodes(airportCodeList);
        return CompletableFuture.completedFuture(travellerResponse);
    }
}
