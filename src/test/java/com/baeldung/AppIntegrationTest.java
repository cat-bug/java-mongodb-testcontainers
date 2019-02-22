package com.baeldung;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.assertEquals;

public class AppIntegrationTest {

    private static final String DB_NAME = "myMongoDb";
    private MongoDatabase db;
    private MongoCollection collection;
    private boolean isExecutedAgainstContainer = true;

    @ClassRule
    public static GenericContainer mongodb = new GenericContainer("mongo:latest").withExposedPorts(27017);

    @Before
    public void setup() {
        MongoClient mongoClient;

        if (isExecutedAgainstContainer) {
            mongoClient = new MongoClient(mongodb.getContainerIpAddress(), mongodb.getFirstMappedPort());
        } else {
            mongoClient = new MongoClient("localhost", 27017);
        }

        db = mongoClient.getDatabase(DB_NAME);
        collection = db.getCollection("customers");
    }

    @Test
    public void testAddressPersistence() {
        Document contact = new Document();
        contact.put("name", "John");
        contact.put("company", "Baeldung");

        collection.insertOne(contact);
        MongoCursor<Document> cursor = collection.find().iterator();
        Document contact1 = new Document();
        while (cursor.hasNext()) {
            contact1 = cursor.next();
            System.out.println(contact1);
        }
        assertEquals(contact1.get("name"), "John");
    }
}