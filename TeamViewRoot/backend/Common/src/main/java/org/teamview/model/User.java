package org.teamview.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.github.f4b6a3.ulid.Ulid;
import lombok.*;
import org.teamview.enums.SeniorityLevel;
import org.teamview.utils.ProjectSetConverter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "SingleTable")
public class User extends Item {

    private String id; // gsi-sk
    private String type = "user"; // gsi-pk
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String teamId;
    private String position;
    private SeniorityLevel seniority;
    private boolean teamLead;

    private Set<Project> pastProjects = new HashSet<>();

    public User(String id, String teamId) {
        super();
        this.id = id;
        this.teamId = teamId;
    }

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return (teamId != null) ? "TEAM#" + teamId : "NOTEAM";
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        return "USER#" + id;
    }

    public void setPK(String PK) {
        this.PK = PK;
    }

    public void setSK(String SK) {
        this.SK = SK;
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
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @DynamoDBAttribute
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @DynamoDBAttribute
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @DynamoDBAttribute
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute
    public SeniorityLevel getSeniority() {
        return seniority;
    }

    public void setSeniority(SeniorityLevel seniority) {
        this.seniority = seniority;
    }

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.BOOL)
    @DynamoDBAttribute
    public boolean getTeamLead() {
        return teamLead;
    }

    public void setTeamLead(boolean teamLead) {
        this.teamLead = teamLead;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = ProjectSetConverter.class)
    public Set<Project> getPastProjects() {
        return pastProjects;
    }

    public void setPastProjects(Set<Project> pastProjects) {
        this.pastProjects = pastProjects;
    }
}
