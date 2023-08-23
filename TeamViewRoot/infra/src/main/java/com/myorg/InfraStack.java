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
import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketAccessControl;
import software.amazon.awscdk.services.s3.deployment.BucketDeployment;
import software.amazon.awscdk.services.s3.deployment.ISource;
import software.amazon.awscdk.services.s3.deployment.Source;
import software.constructs.Construct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public class InfraStack extends Stack {
    public InfraStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfraStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        Bucket siteBucket = buildS3Bucket();

        Table singleTable = buildSingleTable();

        Function cognitoPostConfirmationLambda = buildCognitoPostConfirmationLambda();
        singleTable.grantReadWriteData(cognitoPostConfirmationLambda);

        UserPool userPool = buildUserPool(cognitoPostConfirmationLambda);

        Function getTeamsLambda = createLambdaFunction("GetTeamsLambda", 1024);
        singleTable.grantReadWriteData(getTeamsLambda);
        Function newTeamLambda = createLambdaFunction("NewTeamLambda", 1024);
        singleTable.grantReadWriteData(newTeamLambda);
        Function deleteTeamLambda = createLambdaFunction("DeleteTeamLambda", 1024);
        singleTable.grantReadWriteData(deleteTeamLambda);

        Function getEmployeesLambda = createLambdaFunction("GetEmployeesLambda", 1024);
        singleTable.grantReadWriteData(getEmployeesLambda);
        Function getEmpsProjectsLambda = createLambdaFunction("GetEmployeesProjectsLambda", 1024);
        singleTable.grantReadWriteData(getEmpsProjectsLambda);
        Function newEmployeeLambda = createLambdaFunction("NewEmployeeLambda", 1024);
        singleTable.grantReadWriteData(newEmployeeLambda);
        Function deleteEmployeeLambda = createLambdaFunction("DeleteEmployeeLambda", 1024);
        singleTable.grantReadWriteData(deleteEmployeeLambda);

        Function getProjectsLambda = createLambdaFunction("GetProjectsLambda", 1024);
        singleTable.grantReadWriteData(getProjectsLambda);
        Function newProjectLambda = createLambdaFunction("NewProjectLambda", 1024);
        singleTable.grantReadWriteData(newProjectLambda);
        Function deleteProjectLambda = createLambdaFunction("DeleteProjectLambda", 1024);
        singleTable.grantReadWriteData(deleteProjectLambda);

        Function authorizerFunction = buildAuthorizerLambda();
        RequestAuthorizer customAuthorizer = RequestAuthorizer.Builder.create(this, "CustomAuthorizer")
                .handler(authorizerFunction.getCurrentVersion())
                .identitySources(singletonList("method.request.header.Authorization"))
                .resultsCacheTtl(Duration.hours(1))
                .build();

        RestApi api = buildApiGateway();

        addEndpointsForTeams(api, getTeamsLambda, newTeamLambda, deleteTeamLambda, customAuthorizer);
        addEndpointsForEmployees(api, getEmployeesLambda, newEmployeeLambda, deleteEmployeeLambda, customAuthorizer);
        addEndpointsForProjects(api, getProjectsLambda, newProjectLambda, deleteProjectLambda, getEmpsProjectsLambda, customAuthorizer);
    }

    private Bucket buildS3Bucket() {
        Bucket siteBucket = Bucket.Builder.create(this, "AngularBucket")
                .websiteIndexDocument("index.html")
                .websiteErrorDocument("index.html")
                .publicReadAccess(true)
                .blockPublicAccess(BlockPublicAccess.BLOCK_ACLS)
                .accessControl(BucketAccessControl.BUCKET_OWNER_FULL_CONTROL)
                .removalPolicy(RemovalPolicy.DESTROY)
                .autoDeleteObjects(true)
                .build();

        List<ISource> sources = new ArrayList<>(1);
        sources.add(Source.asset("../../front/dist/front"));

        BucketDeployment.Builder.create(this, "DeployAngularApp")
                .sources(sources)
                .destinationBucket(siteBucket).build();

        return siteBucket;
    }

    private Function buildAuthorizerLambda() {
        // Create the custom authorizer Lambda function
        Function authorizerFunction = Function.Builder.create(this, "AuthorizerFunction")
                .runtime(Runtime.JAVA_17)
                .code(Code.fromAsset("../assets/LambdaAuthorizer.jar"))
                .handler("org.example.LambdaAuthorizer")
                .timeout(Duration.seconds(30))
                .memorySize(1024)
                .build();

        // Enable Snapstart
        CfnFunction cfnGetFunction = (CfnFunction) authorizerFunction.getNode().getDefaultChild();
        cfnGetFunction.addPropertyOverride("SnapStart", Map.of("ApplyOn", "PublishedVersions"));

        return authorizerFunction;
    }

    private void addEndpointsForProjects(RestApi api, Function getProjectsLambda, Function newProjectLambda, Function deleteProjectLambda, Function getEmpsProjectsLambda, RequestAuthorizer customAuthorizer) {
        api.getRoot()
                .addResource("user-projects")
                .addMethod("GET", new LambdaIntegration(getEmpsProjectsLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
        api.getRoot()
                .addResource("projects")
                .addMethod("GET", new LambdaIntegration(getProjectsLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
        api.getRoot()
                .getResource("projects")
                .addMethod("POST", new LambdaIntegration(newProjectLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
        api.getRoot()
                .getResource("projects")
                .addMethod("DELETE", new LambdaIntegration(deleteProjectLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
    }

    private void addEndpointsForEmployees(RestApi api, Function getEmployeesLambda, Function newEmployeeLambda, Function deleteEmployeeLambda, RequestAuthorizer customAuthorizer) {
        api.getRoot()
                .addResource("employees")
                .addMethod("GET", new LambdaIntegration(getEmployeesLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
        api.getRoot()
                .getResource("employees")
                .addMethod("POST", new LambdaIntegration(newEmployeeLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
        api.getRoot()
                .getResource("employees")
                .addMethod("DELETE", new LambdaIntegration(deleteEmployeeLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
    }

    private void addEndpointsForTeams(RestApi api, Function getTeamsLambda, Function newTeamLambda, Function deleteTeamLambda, RequestAuthorizer customAuthorizer) {
        api.getRoot()
                .addResource("teams")
                .addMethod("GET", new LambdaIntegration(getTeamsLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
        api.getRoot()
                .getResource("teams")
                .addMethod("POST", new LambdaIntegration(newTeamLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
        api.getRoot()
                .getResource("teams")
                .addMethod("DELETE", new LambdaIntegration(deleteTeamLambda.getCurrentVersion()),
                        MethodOptions.builder()
                                .authorizationType(AuthorizationType.CUSTOM)
                                .authorizer(customAuthorizer)
                                .build());
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
