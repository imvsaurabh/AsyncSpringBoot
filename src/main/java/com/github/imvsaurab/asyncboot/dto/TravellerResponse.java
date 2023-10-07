package com.github.imvsaurab.asyncboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Setter
@Getter
public class TravellerResponse {

    public List<AirportCode> airportCodes;

    @ToString
    @Setter
    @Getter
    public static class AirportCode {
        public String origin;
        public String destination;
        public Passengers passengers;
    }

    @ToString
    @Setter
    @Getter
    public static class Passengers {
        public List<TravellerDTO> travellers;
    }
}
