package com.github.imvsaurab.asyncboot.model;

import com.opencsv.bean.CsvBindByPosition;
import lombok.ToString;

@ToString
public class Traveller {
    @CsvBindByPosition(position = 0)
    public String id;
    @CsvBindByPosition(position = 1)
    public String fullName;
    @CsvBindByPosition(position = 2)
    public String email;
    @CsvBindByPosition(position = 3)
    public String gender;
    @CsvBindByPosition(position = 4)
    public String origin;
    @CsvBindByPosition(position = 5)
    public String destination;
}
