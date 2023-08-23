package org.teamview.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.teamview.enums.ProjectStatus;
import org.teamview.utils.LocalDateConverter;

import java.util.Date;

@DynamoDBTable(tableName = "SingleTable")
@NoArgsConstructor
@AllArgsConstructor
public class UserProject extends Project {
    private String userId;
//    private String projectId;
//    private String title;
//    private String client;
//    private ProjectStatus projectStatus;
//    private Date startDate;
//    private Date endDate;
//    private String teamId;
//    private String description;

    public UserProject(String userId, Project project) {
        super(project);
        this.userId = userId;
    }

    @Override
    @DynamoDBHashKey(attributeName = "PK")
    public String getPK() {
        return "USER#" + userId;
    }

    @Override
    @DynamoDBRangeKey(attributeName = "SK")
    public String getSK() {
        return "PROJECT#" + id;
    }

    @Override
    public void setPK(String PK) {
        this.PK = PK;
    }

    @Override
    public void setSK(String SK) {
        this.SK = SK;
    }

    @DynamoDBIndexHashKey(attributeName = "itemType", globalSecondaryIndexName = "EntityTypeGSI")
    @Override
    public String getType() {
        return null;
    }

    @DynamoDBAttribute
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
//
//    @DynamoDBAttribute
//    public String getProjectId() {
//        return projectId;
//    }
//
//    public void setProjectId(String projectId) {
//        this.projectId = projectId;
//    }
//
//    @DynamoDBAttribute
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    @DynamoDBAttribute
//    public String getClient() {
//        return client;
//    }
//
//    public void setClient(String client) {
//        this.client = client;
//    }
//
//    @DynamoDBAttribute
//    @DynamoDBTypeConvertedEnum
//    public ProjectStatus getProjectStatus() {
//        return projectStatus;
//    }
//
//    public void setProjectStatus(ProjectStatus projectStatus) {
//        this.projectStatus = projectStatus;
//    }
//
//    @DynamoDBAttribute
//    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
//    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    @DynamoDBAttribute
//    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
//    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
//    public Date getEndDate() {
//        return endDate;
//    }
//
//    public void setEndDate(Date endDate) {
//        this.endDate = endDate;
//    }
//
//    @DynamoDBAttribute
//    public String getTeamId() {
//        return teamId;
//    }
//
//    public void setTeamId(String teamId) {
//        this.teamId = teamId;
//    }
//
//    @DynamoDBAttribute
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
