package org.teamview.model;


import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.github.f4b6a3.ulid.Ulid;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "SingleTable")

public class Team extends Item {

    //    @DynamoDBAttribute
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBIndexRangeKey(attributeName = "id", globalSecondaryIndexName = "EntityTypeGSI")
    private Ulid id;

    //    @DynamoDBAttribute
    @DynamoDBIndexHashKey(attributeName = "type", globalSecondaryIndexName = "EntityTypeGSI")
    private String type = "team";

    @DynamoDBAttribute
    private String name;

    @DynamoDBAttribute
    private String teamLead;         // todo: ?

    private boolean deleted = false; // todo: ?

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPk() {
        return "TEAM#" + id;
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    public String getSk() {
        return "TEAM#" + id;
    }
}
