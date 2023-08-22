package org.teamview.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;
import org.teamview.enums.ProjectStatus;
import org.teamview.utils.LocalDateConverter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "SingleTable")
public class Project extends Item {

    protected String id;
    protected String type = "project";
    protected String title;
    protected String description;
    protected Date startDate;
    protected Date endDate;
    protected String client;
    protected String teamId;
    protected ProjectStatus projectStatus;

    public Project(Project project) {
        super();
        this.id = project.getId();
        this.title = project.getTitle();
        this.client = project.getClient();
        this.projectStatus = project.getProjectStatus();
        this.startDate = project.getStartDate();
        this.endDate = project.getEndDate();
        this.teamId = project.getTeamId();
        this.description = project.getDescription();
    }

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return (teamId != null) ? "TEAM#" + teamId : "NOTEAM";
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        return "PROJECT#" + id;
    }

    @Override
    public void setPK(String pk) {
        this.PK = pk;
    }

    @Override
    public void setSK(String sk) {
        this.SK = sk;
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @DynamoDBAttribute
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    @DynamoDBAttribute
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }
}
