package com.sliva.subdivisionvalidator2;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author whost
 */
public class SubdivisionValidator {

    private Map<String, Set<String>> isoSubdivisionData;

     // Constructor to initialize and load ISO 3166-2 data from JSON resource
    public SubdivisionValidator(String resourceFileName) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Load the JSON file as a resource
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourceFileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource file not found: " + resourceFileName);
            }
            isoSubdivisionData = objectMapper.readValue(inputStream,
                    new TypeReference<Map<String, Set<String>>>() {});
        }
    }

    // Method to validate the subdivision code against the ISO 3166-2 data
    public boolean validateSubdivision(String countryCode, String subdivisionCode) {
        Set<String> subdivisions = isoSubdivisionData.get(countryCode);
        return subdivisions != null && subdivisions.contains(subdivisionCode);
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            SubdivisionValidator validator = new SubdivisionValidator("iso_3166_2_data.json");
            System.out.println(validator.validateSubdivision("US", "CA")); // Example: should return true if valid
            System.out.println(validator.validateSubdivision("EC", "U")); // Example: should return true if valid
            System.out.println(validator.validateSubdivision("CA", "OO")); // Example: should return false if valid
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
