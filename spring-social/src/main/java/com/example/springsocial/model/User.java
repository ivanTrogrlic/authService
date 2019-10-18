package com.example.springsocial.model;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GeneratedValue;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;
import org.springframework.data.couchbase.core.mapping.id.IdAttribute;
import org.springframework.data.couchbase.core.mapping.id.IdPrefix;

@Data
@Builder
@Document
public class User {

    @Id
    @GeneratedValue(delimiter = "::", strategy = GenerationStrategy.USE_ATTRIBUTES)
    String id;

    @Builder.Default
    @IdPrefix
    String docIdPrefix = "familyProfile";

    @Field
    @IdAttribute
    private String userId;

    @Field
    private String name;

    @Field
    private String email;

    @Field
    private String base64image;

}
