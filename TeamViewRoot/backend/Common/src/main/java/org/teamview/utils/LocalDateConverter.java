package org.teamview.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDate;

public class LocalDateConverter implements DynamoDBTypeConverter<String, LocalDate> {

    @Override
    public String convert(LocalDate localDate) {
        return localDate.toString();
    }

    @Override
    public LocalDate unconvert(String localDate) {
        return LocalDate.parse(localDate);
    }
}