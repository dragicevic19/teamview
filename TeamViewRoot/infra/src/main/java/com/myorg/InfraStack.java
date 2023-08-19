package com.myorg;

import software.amazon.awscdk.*;
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
                        .name("type")
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

        Table table = new Table(this, "singleTable", tableProps);
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
