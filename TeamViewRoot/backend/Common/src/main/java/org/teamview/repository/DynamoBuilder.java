package org.teamview.repository;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.github.f4b6a3.ulid.UlidCreator;
import org.teamview.model.Item;
import org.teamview.model.Team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoBuilder {

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
                .withRegion("eu-north-1")
                .build();


        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder().build();
        instance = new DynamoBuilder(mapperConfig, client);
        return instance;
    }

    public void saveTeamClient() {
        // 1) nacin
        Map<String, AttributeValue> attrs = new HashMap<>();
//        attrs.put("PK", new AttributeValue("TEAM#1"));
//        attrs.put("SK", new AttributeValue("TEAM#1"));
        attrs.put("id", new AttributeValue(UlidCreator.getUlid().toString()));
        attrs.put("name", new AttributeValue("NewName"));
        attrs.put("type", new AttributeValue("team"));
        attrs.put("teamLead", new AttributeValue("marko@gmail.com"));
        attrs.put("deleted", new AttributeValue(String.valueOf(true)));

        try {
            client.putItem("SingleTable", attrs);
        } catch (ResourceNotFoundException e) {
            System.err.format("Error: The table \"%s\" can't be found.\n", "SingleTable");
            System.err.println("Be sure that it exists and that you've typed its name correctly!");
//            System.exit(1);
        } catch (AmazonServiceException e) {
            System.err.println(e.getMessage());
//            System.exit(1);
        }

    }

    public void saveTeamMapper(Team team) {
        mapper.save(team);
    }

    public List<Item> getAll() {
        return mapper.scan(Item.class, new DynamoDBScanExpression());
    }

}
