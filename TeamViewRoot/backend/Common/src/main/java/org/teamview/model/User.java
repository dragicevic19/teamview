package org.teamview.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.github.f4b6a3.ulid.Ulid;
import lombok.*;
import org.teamview.enums.SeniorityLevel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@DynamoDBTable(tableName = "SingleTable")

public class User extends Item {

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBIndexRangeKey(attributeName = "id", globalSecondaryIndexName = "EntityTypeGSI")
    private Ulid id; // gsi-sk

    @DynamoDBIndexHashKey(attributeName = "type", globalSecondaryIndexName = "EntityTypeGSI")
    private String type = "user"; // gsi-pk

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    protected String lastName;

    @DynamoDBAttribute
    protected String address;

    @DynamoDBAttribute
    protected String email;

    @DynamoDBAttribute
    private String teamId;

    @DynamoDBAttribute
    private String position;

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    private SeniorityLevel seniority;

    @DynamoDBAttribute
    private boolean teamLead;

    // todo: past projects? json

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPk() {
        return (teamId != null) ? "TEAM#" + teamId : "NOTEAM";
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    public String getSk() {
        return "USER#" + id;
    }
}
