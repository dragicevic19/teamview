package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

import org.example.util.PolicyDocument;
import org.example.util.Response;
import org.example.util.Statement;
import org.teamview.utils.JWTUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LambdaAuthorizer implements RequestHandler<APIGatewayProxyRequestEvent, Response> {
    private static final String EMPLOYEE_GROUP = "EmployeeGroup";
    private static final String LEAD_GROUP = "LeadGroup";
    private static final String ADMIN_GROUP = "AdminGroup";


    @Override
    public Response handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String jwtToken = event.getHeaders().get("Authorization");

        String sub = JWTUtil.getSub(jwtToken, "sub");
        if (sub == null) {
            throw new RuntimeException("Unauthorized");
        }
        System.out.println("AUTHORIZER: SUB: " + sub);
        List<String> userGroup = JWTUtil.getCognitoGroups(jwtToken, "cognito:groups");

        assert userGroup != null;
        userGroup.forEach(System.out::println);
        Map<String, String> ctx = new HashMap<>();
        ctx.put("sub", sub);

        List<Statement> statements = new ArrayList<>();

        APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext = event.getRequestContext();
        APIGatewayProxyRequestEvent.RequestIdentity identity = proxyContext.getIdentity();

        if (userGroup.contains(ADMIN_GROUP)) {
            addStatement(statements, "teams", "GET", proxyContext);
            addStatement(statements, "teams", "POST", proxyContext);
            addStatement(statements, "teams", "DELETE", proxyContext);

            addStatement(statements, "projects", "GET", proxyContext);
            addStatement(statements, "projects", "POST", proxyContext);
            addStatement(statements, "projects", "DELETE", proxyContext);

            addStatement(statements, "employees", "GET", proxyContext);
            addStatement(statements, "employees", "POST", proxyContext);
            addStatement(statements, "employees", "DELETE", proxyContext);

            addStatement(statements, "user-projects", "GET", proxyContext);
//        } else if (userGroup.contains(LEAD_GROUP)) {
//
//        } else if (userGroup.contains(EMPLOYEE_GROUP)) {
        }

        PolicyDocument policyDocument = PolicyDocument.builder().statements(statements)
                .build();

        return Response.builder().principalId(identity.getAccountId()).policyDocument(policyDocument)
                .context(ctx).build();
    }

    private void addStatement(List<Statement> statements, String endpoint, String httpMethod,
                              APIGatewayProxyRequestEvent.ProxyRequestContext proxyContext) {
        String auth = "Allow";
        String arn = String.format("arn:aws:execute-api:%s:%s:%s/%s/%s/%s",
                "eu-north-1", proxyContext.getAccountId(),
                proxyContext.getApiId(), proxyContext.getStage(), httpMethod, endpoint);
        statements.add(Statement.builder().effect(auth).resource(arn).build());
    }
}