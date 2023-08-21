package org.teamview.repository;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.teamview.model.Item;
import org.teamview.model.Project;
import org.teamview.model.Team;
import org.teamview.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoBuilder {

    private static final String TABLE_NAME = "SingleTable";
    private static final String REGION = "eu-north-1";

    private final DynamoDBMapper mapper;
    private final AmazonDynamoDB client;

    private static DynamoBuilder instance = null;

    private DynamoBuilder(DynamoDBMapperConfig config, AmazonDynamoDB client) {
        this.mapper = new DynamoDBMapper(client, config);
        this.client = client;
    }

    public static DynamoBuilder createBuilder() {
        if (instance != null) return instance;
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(REGION)
                .build();

        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder().build();
        instance = new DynamoBuilder(mapperConfig, client);
        return instance;
    }

    public void saveTeamClient(Team team) {
        Map<String, AttributeValue> attrs = new HashMap<>();
        attrs.put("PK", new AttributeValue(team.getPK()));
        attrs.put("SK", new AttributeValue(team.getSK()));
        attrs.put("id", new AttributeValue(String.valueOf(team.getId())));
        attrs.put("teamName", new AttributeValue(team.getTeamName()));
        attrs.put("itemType", new AttributeValue(team.getType()));
//        attrs.put("teamLead", new AttributeValue(team.getTeamLead()));
//        attrs.put("deleted", new AttributeValue(String.valueOf(true)));

        try {
            client.putItem(TABLE_NAME, attrs);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", TABLE_NAME);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
        }
    }

    public void saveTeamMapper(Team team) {
        mapper.save(team);
    }

    public List<Item> getAll() {
        return mapper.scan(Item.class, new DynamoDBScanExpression());
    }

    public List<Team> getAllTeams() {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":t", new AttributeValue().withS("team"));

        DynamoDBQueryExpression<Team> queryExpression = new DynamoDBQueryExpression<Team>()
                .withIndexName("EntityTypeGSI")
                .withKeyConditionExpression("itemType = :t")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);

        return mapper.query(Team.class, queryExpression);
    }

    public User getUser(String userId, String teamId) {
        User user = null;
        String pk = (teamId == null) ? "NOTEAM" : "TEAM#" + teamId;
        String sk = "USER#" + userId;

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS(pk));
        expressionAttributeValues.put(":sk", new AttributeValue().withS(sk));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withKeyConditionExpression("(PK = :pk) AND (SK = :sk)")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);

        PaginatedQueryList<User> result = this.mapper.query(User.class, queryExpression);
        if (!result.isEmpty()) {
            user = result.get(0);
        }
        return user;
    }

    public Team getTeam(String teamId) {
        Team team = null;

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS("TEAM#" + teamId));
        expressionAttributeValues.put(":sk", new AttributeValue().withS("TEAM#" + teamId));

        DynamoDBQueryExpression<Team> queryExpression = new DynamoDBQueryExpression<Team>()
                .withKeyConditionExpression("(PK = :pk) AND (SK = :sk)")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);

        PaginatedQueryList<Team> result = this.mapper.query(Team.class, queryExpression);
        if (!result.isEmpty()) {
            team = result.get(0);
        }
        return team;
    }

    public void deleteUser(User user) {
        mapper.delete(user);
    }

    public void saveUser(User user) {
        mapper.save(user);
    }

    public void saveTeam(Team team) {
        mapper.save(team);
    }

    public List<User> getAllUsers() {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":t", new AttributeValue().withS("user"));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("EntityTypeGSI")
                .withKeyConditionExpression("itemType = :t")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);

        return mapper.query(User.class, queryExpression);
    }

    public Project getProjectForTeam(String teamId) {
        Project project = null;

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS("TEAM#" + teamId));
        expressionAttributeValues.put(":sk", new AttributeValue().withS("PROJECT"));

        DynamoDBQueryExpression<Project> queryExpression = new DynamoDBQueryExpression<Project>()
                .withKeyConditionExpression("(PK = :pk) AND (SK = :sk)")
                .withExpressionAttributeValues(expressionAttributeValues)
                .withConsistentRead(false);

        PaginatedQueryList<Project> result = this.mapper.query(Project.class, queryExpression);
        if (!result.isEmpty()) {
            project = result.get(0);
        }
        return project;
    }

    public List<User> getMembersOfTheTeam(String teamId) {

        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":pk", new AttributeValue().withS("TEAM#" + teamId));
        expressionAttributeValues.put(":sk", new AttributeValue().withS("USER#"));

        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withConsistentRead(false)
                .withKeyConditionExpression("(PK = :pk) AND begins_with(SK, :sk)")
                .withExpressionAttributeValues(expressionAttributeValues);

        return mapper.query(User.class, queryExpression);
    }
}
