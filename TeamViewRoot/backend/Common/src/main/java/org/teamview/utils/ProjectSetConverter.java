package org.teamview.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.teamview.model.Project;

import java.io.IOException;
import java.util.Set;


public class ProjectSetConverter implements DynamoDBTypeConverter<String, Set<Project>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(Set<Project> projects) {
        try {
            return objectMapper.writeValueAsString(projects);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting Set<Project> to JSON", e);
        }
    }

    @Override
    public Set<Project> unconvert(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Set<Project>>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to Set<Project>", e);
        }
    }
}