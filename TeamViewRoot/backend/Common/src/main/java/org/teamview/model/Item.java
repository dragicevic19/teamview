package org.teamview.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Setter;

public abstract class Item {

    protected String PK;
    protected String SK;

    abstract String getPK();

    abstract String getSK();

    abstract void setPK(String pk);

    abstract void setSK(String sk);

}
