package org.ifsul.carhired.api.system.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;

/**
 * A generic class for serializing and deserializing objects to/from JSON using the Jackson library.
 *
 * @param <T> The type of objects that will be serialized and deserialized.
 */
@Component
public class JsonSerializer<T> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonFactory factory = objectMapper.getFactory();

    /**
     * Serializes an object to a JSON string.
     *
     * @param payload The object to be serialized.
     * @return A JSON string representation of the input object.
     * @throws RuntimeException if there's an issue with JSON serialization, such as a JsonProcessingException.
     */
    public String encode(T payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserializes a JSON string into an object of type T.
     *
     * @param payload The JSON string to be deserialized.
     * @return An object of type T created from the JSON input.
     * @throws RuntimeException if there's an issue with JSON deserialization, such as an IOException.
     */
    public T decode(String payload) {
        TypeReference<T> typeReference = new TypeReference<>() {
        };
        return decode(payload, typeReference);
    }

    /**
     * Deserializes a JSON string into an object of type T using a specified TypeReference.
     *
     * @param payload       The JSON string to be deserialized.
     * @param typeReference The TypeReference specifying the type to which the JSON should be deserialized.
     * @return An object of type T created from the JSON input.
     * @throws RuntimeException if there's an issue with JSON deserialization, such as an IOException.
     */
    public T decode(String payload, TypeReference<T> typeReference) {
        try (com.fasterxml.jackson.core.JsonParser parser = factory.createParser(new StringReader(payload))) {
            return objectMapper.readValue(parser, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
