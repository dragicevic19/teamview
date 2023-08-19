package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.events.CognitoUserPoolPostConfirmationEvent;
import com.github.f4b6a3.ulid.UlidCreator;
import org.teamview.model.Team;
import org.teamview.repository.DynamoBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupResponse;

import java.util.Map;

public class PostConfirmationLambdaHandler implements RequestHandler<CognitoUserPoolPostConfirmationEvent, CognitoUserPoolPostConfirmationEvent> {


    private static final String EMPLOYEE_GROUP_NAME = "EmployeeGroup";
    private final CognitoIdentityProviderClient cognitoIdentityProviderClient = CognitoIdentityProviderClient.builder()
            .region(Region.EU_NORTH_1)
            .build();

    public PostConfirmationLambdaHandler() {
    }

    @Override
    public CognitoUserPoolPostConfirmationEvent handleRequest(CognitoUserPoolPostConfirmationEvent cognitoUserPoolPostConfirmationEvent, Context context) {

        Map<String, String> userAttributes = cognitoUserPoolPostConfirmationEvent.getRequest().getUserAttributes();
        addUserToGroup(userAttributes.get("sub"), cognitoUserPoolPostConfirmationEvent.getUserPoolId());

//        Employee employee = new Employee(userAttributes);
//        employeeRepo.save(employee);

//        try (Connection conn = DriverManager.getConnection()) {
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        System.out.println("11111111111111111111111111\n\n");

        Team team = new Team(UlidCreator.getUlid(), "team", "TeamFromLambda", "marko@gmail.com", false);
        DynamoBuilder.createBuilder().saveTeamMapper(team);
        System.out.println("2222222222222222222222222\n\n");

        DynamoBuilder.createBuilder().saveTeamClient();
        System.out.println("3333333333333333333333\n\n");


        return cognitoUserPoolPostConfirmationEvent;
    }

    private void addUserToGroup(String username, String userPoolId) {
        AdminAddUserToGroupRequest addUserToGroupRequest = AdminAddUserToGroupRequest.builder()
                .userPoolId(userPoolId)
                .groupName(EMPLOYEE_GROUP_NAME)
                .username(username)
                .build();

        AdminAddUserToGroupResponse response = cognitoIdentityProviderClient.adminAddUserToGroup(addUserToGroupRequest);

        // Check if the operation was successful, handle errors if needed
        // You can access the response using response. methods
    }
}
