package com.skyscanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {
        // No initialization needed for now
    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<SearchResult> searchResults = new ArrayList<>();

        // Load rental cars JSON
        try (InputStream carStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json")) {
            if (carStream != null) {
                List<SearchResult> carResults = mapper.readValue(carStream, new TypeReference<List<SearchResult>>() {});
                searchResults.addAll(carResults);
            }
        }

        // Load hotels JSON
        try (InputStream hotelStream = getClass().getClassLoader().getResourceAsStream("hotels.json")) {
            if (hotelStream != null) {
                List<SearchResult> hotelResults = mapper.readValue(hotelStream, new TypeReference<List<SearchResult>>() {});
                searchResults.addAll(hotelResults);
            }
        }

        // Register SearchResource
        final SearchResource resource = new SearchResource(searchResults);
        environment.jersey().register(resource);
    }
}
