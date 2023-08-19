package org.teamview.model;


import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "SingleTable")

public class Team extends Item {

    private String id;

    private String type = "team";

    private String teamName;

    private User teamLead;         // todo: ?

    private boolean deleted = false; // todo: ?

    public Team(String id, String teamName, User teamLead) {
        this.id = id;
        this.teamName = teamName;
        this.teamLead = teamLead;
    }

    @DynamoDBIndexRangeKey(attributeName = "id", globalSecondaryIndexName = "EntityTypeGSI")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBIndexHashKey(attributeName = "itemType", globalSecondaryIndexName = "EntityTypeGSI")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @DynamoDBAttribute
    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    @DynamoDBTypeConvertedJson
    @DynamoDBAttribute
    public User getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(User teamLead) {
        this.teamLead = teamLead;
    }


    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    @DynamoDBAttribute
    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return "TEAM#" + id;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        return "TEAM#" + id;
    }

    public void setSK(String SK) {
        this.SK = SK;
    }
}
