package com.github.imvsaurab.asyncboot.utility;

import com.github.imvsaurab.asyncboot.model.Traveller;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class Utility {

    public static List<Traveller> getData(String fileName) throws FileNotFoundException {
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);
        return new CsvToBeanBuilder(new FileReader(file))
                .withType(Traveller.class)
                .withSkipLines(1)
                .build()
                .parse();
    }
}
