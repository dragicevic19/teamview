package org.teamview.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.github.f4b6a3.ulid.Ulid;
import lombok.*;
import org.teamview.enums.ProjectStatus;
import org.teamview.utils.LocalDateConverter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "SingleTable")

public class Project extends Item {

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBIndexRangeKey(attributeName = "id", globalSecondaryIndexName = "EntityTypeGSI")
    private Ulid id;

    @DynamoDBIndexHashKey(attributeName = "type", globalSecondaryIndexName = "EntityTypeGSI")
    private String type = "project";
    @DynamoDBAttribute
    private String title;

    @DynamoDBAttribute
    private String description;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private Date startDate;

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    private Date endDate;

    @DynamoDBAttribute
    private String client;

//    private Team team;

    @DynamoDBAttribute
    private String teamId;

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    private ProjectStatus status;

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPk() {
        return (teamId != null) ? "TEAM#" + teamId : "NOTEAM";
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    String getSk() {
        return "PROJECT#" + id;
    }

}
