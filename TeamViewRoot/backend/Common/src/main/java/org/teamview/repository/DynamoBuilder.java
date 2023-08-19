package org.teamview.repository;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.teamview.model.Item;
import org.teamview.model.Team;

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

}
