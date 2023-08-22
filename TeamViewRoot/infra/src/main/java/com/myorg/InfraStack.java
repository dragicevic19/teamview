package com.myorg;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.cognito.*;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.amazon.awscdk.services.lambda.CfnFunction;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public class InfraStack extends Stack {
    public InfraStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfraStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Table singleTable = buildSingleTable();

        Function cognitoPostConfirmationLambda = buildCognitoPostConfirmationLambda();
        singleTable.grantReadWriteData(cognitoPostConfirmationLambda);

        UserPool userPool = buildUserPool(cognitoPostConfirmationLambda);

        Function getTeamsLambda = createLambdaFunction("GetTeamsLambda", 1024);
        singleTable.grantReadWriteData(getTeamsLambda);
        Function newTeamLambda = createLambdaFunction("NewTeamLambda", 1024);
        singleTable.grantReadWriteData(newTeamLambda);

        Function getEmployeesLambda = createLambdaFunction("GetEmployeesLambda", 1024);
        singleTable.grantReadWriteData(getEmployeesLambda);
        Function getEmpsProjectsLambda = createLambdaFunction("GetEmployeesProjectsLambda", 1024);
        singleTable.grantReadWriteData(getEmpsProjectsLambda);
        Function newEmployeeLambda = createLambdaFunction("NewEmployeeLambda", 1024);
        singleTable.grantReadWriteData(newEmployeeLambda);

        Function getProjectsLambda = createLambdaFunction("GetProjectsLambda", 1024);
        singleTable.grantReadWriteData(getProjectsLambda);
        Function newProjectLambda = createLambdaFunction("NewProjectLambda", 1024);
        singleTable.grantReadWriteData(newProjectLambda);

        RestApi api = buildApiGateway();

        api.getRoot()
                .addResource("teams")
                .addMethod("GET", new LambdaIntegration(getTeamsLambda));
//                        MethodOptions.builder()
//                                .authorizationType(AuthorizationType.CUSTOM)
//                                .authorizer(customAuthorizer)
//                                .build());
        api.getRoot()
                .getResource("teams")
                .addMethod("POST", new LambdaIntegration(newTeamLambda));
//                        MethodOptions.builder()
//                                .authorizationType(AuthorizationType.CUSTOM)
//                                .authorizer(customAuthorizer)
//                                .build());

        api.getRoot()
                .addResource("employees")
                .addMethod("GET", new LambdaIntegration(getEmployeesLambda));
//                        MethodOptions.builder()
//                                .authorizationType(AuthorizationType.CUSTOM)
//                                .authorizer(customAuthorizer)
//                                .build());

        api.getRoot()
                .getResource("employees")
                .addMethod("POST", new LambdaIntegration(newEmployeeLambda));
//                        MethodOptions.builder()
//                                .authorizationType(AuthorizationType.CUSTOM)
//                                .authorizer(customAuthorizer)
//                                .build());

        api.getRoot()
                .addResource("projects")
                .addMethod("GET", new LambdaIntegration(getProjectsLambda));
//                        MethodOptions.builder()
//                                .authorizationType(AuthorizationType.CUSTOM)
//                                .authorizer(customAuthorizer)
//                                .build());
        api.getRoot()
                .getResource("projects")
                .addMethod("POST", new LambdaIntegration(newProjectLambda));
//                        MethodOptions.builder()
//                                .authorizationType(AuthorizationType.CUSTOM)
//                                .authorizer(customAuthorizer)
//                                .build());

        api.getRoot()
                .addResource("user-projects")
                .addMethod("GET", new LambdaIntegration(getEmpsProjectsLambda));
//                        MethodOptions.builder()
//                                .authorizationType(AuthorizationType.CUSTOM)
//                                .authorizer(customAuthorizer)
//                                .build());
    }

    private RestApi buildApiGateway() {
        return RestApi.Builder.create(this, "MyRestApi")
                .description("This is REST API")
                .defaultCorsPreflightOptions(CorsOptions.builder()
                        .allowCredentials(true)
                        .allowOrigins(singletonList("*")).build())
                .build();

// Deploy the REST API to a stage
//        Deployment deployment = Deployment.Builder.create(this, "MyDeployment")
//                .api(api)
//                .description("Initial deployment")
//                .build();
//
//        Stage stage = Stage.Builder.create(this, "MyStage")
//                .deployment(deployment)
//                .stageName("testStage")
//                .build();
    }

    private Function createLambdaFunction(String name, Integer memorySize) {

        Function func = Function.Builder.create(this, name)
                .handler("org.example.StreamLambdaHandler")
                .runtime(Runtime.JAVA_17)
                .memorySize(memorySize)
                .timeout(Duration.seconds(20))
                .code(Code.fromAsset("../assets/" + name + ".jar"))
                .build();

        // Enable Snapstart
        CfnFunction cfnGetFunction = (CfnFunction) func.getNode().getDefaultChild();
        cfnGetFunction.addPropertyOverride("SnapStart", Map.of("ApplyOn", "PublishedVersions"));

        return func;
    }

    private Table buildSingleTable() {
        TableProps tableProps = TableProps.builder()
                .partitionKey(Attribute.builder()
                        .name("PK")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("SK")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PROVISIONED)
                .readCapacity(1)
                .writeCapacity(1)
                .removalPolicy(RemovalPolicy.DESTROY)
                .tableName("SingleTable")
                .build();


        GlobalSecondaryIndexProps gsi = GlobalSecondaryIndexProps.builder()
                .indexName("EntityTypeGSI")
                .partitionKey(Attribute.builder()
                        .name("itemType")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("id")
                        .type(AttributeType.STRING)
                        .build())
                .projectionType(ProjectionType.ALL)
                .readCapacity(1)
                .writeCapacity(1)
                .build();

        Table table = new Table(this, "SingleTable", tableProps);
        table.addGlobalSecondaryIndex(gsi);

        return table;
    }

    private Function buildCognitoPostConfirmationLambda() {
        Function lambda = Function.Builder.create(this, "CognitoPostConfigurationLambda")
                .handler("org.example.PostConfirmationLambdaHandler")
                .runtime(Runtime.JAVA_17)
                .memorySize(1024)
                .timeout(Duration.seconds(20))
                .code(Code.fromAsset("../assets/CognitoPostConfigurationLambda.jar"))
                .initialPolicy(singletonList(PolicyStatement.Builder.create()
                        .actions(List.of("cognito-idp:AdminAddUserToGroup"))
                        .resources(List.of("aws:ResourceTag/"))
                        .resources(List.of("*"))
                        .build()))
                .build();

        // Enable Snapstart
        CfnFunction cfnGetFunction = (CfnFunction) lambda.getNode().getDefaultChild();
        cfnGetFunction.addPropertyOverride("SnapStart", Map.of("ApplyOn", "PublishedVersions"));

        return lambda;
    }

    private UserPool buildUserPool(Function cognitoPostConfirmationLambda) {
        UserPool userPool = UserPool.Builder.create(this, "user-pool-1")
                .selfSignUpEnabled(true)
                .signInAliases(SignInAliases.builder().email(true).username(false).build())
                .autoVerify(AutoVerifiedAttrs.builder().email(true).build())
                .userVerification(UserVerificationConfig.builder()
                        .emailSubject("Verify your email.")
                        .emailBody("Thanks for signing up to our awesome app! Your verification code is {####}")
                        .emailStyle(VerificationEmailStyle.CODE)
                        .build())
                .standardAttributes(StandardAttributes.builder()
                        .givenName(StandardAttribute.builder().required(true).mutable(true).build())
                        .familyName(StandardAttribute.builder().required(true).mutable(true).build())
                        .email(StandardAttribute.builder().required(true).mutable(true).build())
                        .build())
                .passwordPolicy(PasswordPolicy.builder()
                        .minLength(8)
                        .requireDigits(true)
                        .requireUppercase(true)
                        .requireLowercase(true)
                        .requireSymbols(false)
                        .build())
                .removalPolicy(RemovalPolicy.DESTROY)
                .accountRecovery(AccountRecovery.EMAIL_ONLY)
                .lambdaTriggers(UserPoolTriggers.builder()
                        .postConfirmation(cognitoPostConfirmationLambda).build())
                .build();

        UserPoolClient userPoolClient = UserPoolClient.Builder.create(this, "user_pool_client")
                .userPool(userPool)
                .authFlows(AuthFlow.builder().userSrp(true).userPassword(true).adminUserPassword(true).build())
                .build();

        CfnUserPoolGroup employeeGroup = CfnUserPoolGroup.Builder.create(this, "employee_group")
                .userPoolId(userPool.getUserPoolId())
                .groupName("EmployeeGroup")
                .build();

        CfnUserPoolGroup leadGroup = CfnUserPoolGroup.Builder.create(this, "team_lead_group")
                .userPoolId(userPool.getUserPoolId())
                .groupName("TeamLeadGroup")
                .build();

//        addLeadToGroup(leadGroup);

        CfnUserPoolGroup adminGroup = CfnUserPoolGroup.Builder.create(this, "admin_group")
                .userPoolId(userPool.getUserPoolId())
                .groupName("AdminGroup")
                .build();

//        addAdminToGroup(adminGroup);

        return userPool;
    }
}
