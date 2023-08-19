package org.teamview.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
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
    private String id;

    @DynamoDBIndexHashKey(attributeName = "itemType", globalSecondaryIndexName = "EntityTypeGSI")
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
    private ProjectStatus projectStatus;

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return (teamId != null) ? "TEAM#" + teamId : "NOTEAM";
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    String getSK() {
        return "PROJECT#" + id;
    }

    @Override
    void setPK(String pk) {
        this.PK = pk;
    }

    @Override
    void setSK(String sk) {
        this.SK = sk;
    }
}
