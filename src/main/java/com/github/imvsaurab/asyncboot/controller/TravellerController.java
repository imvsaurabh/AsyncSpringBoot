package com.github.imvsaurab.asyncboot.controller;

import com.github.imvsaurab.asyncboot.dto.TravellerResponse;
import com.github.imvsaurab.asyncboot.model.Traveller;
import com.github.imvsaurab.asyncboot.service.TravellerService;
import com.github.imvsaurab.asyncboot.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TravellerController {

    private final TravellerService travellerService;

    public TravellerController(TravellerService travellerService) {
        this.travellerService = travellerService;
    }

    @GetMapping("travellers")
    public ResponseEntity<TravellerResponse> getAllTraveller() {
        try {
            List<Traveller> travellers = Utility.getData("MOCK_DATA.csv");
            HashSet<String> airportCdPairs = new HashSet<>();
            travellers.forEach(traveller -> airportCdPairs.add(traveller.origin + ":" + traveller.destination));

            TravellerResponse travellerResponse = new TravellerResponse();
            List<TravellerResponse.AirportCode> airportCodes = new ArrayList<>();

            long startTm = System.currentTimeMillis();
            airportCdPairs.parallelStream()
                    .forEach(pair -> {
                        try {
                            CompletableFuture<TravellerResponse> travellerResponseCompletableFuture = travellerService.loadTraveller(pair, travellers);
                            airportCodes.addAll(travellerResponseCompletableFuture.get().getAirportCodes());
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
            travellerResponse.setAirportCodes(airportCodes);

            log.info("Service took total {} ms to generate the response.", (System.currentTimeMillis() - startTm));
            return ResponseEntity.ok(travellerResponse);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
