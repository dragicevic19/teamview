package org.teamview.utils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class LocalDateConverter implements DynamoDBTypeConverter<String, Date> {

    @Override
    public String convert(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }

    @Override
    public Date unconvert(String date) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}