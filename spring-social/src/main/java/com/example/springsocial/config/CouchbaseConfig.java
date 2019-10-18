package com.example.springsocial.config;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.cluster.ClusterInfo;
import com.couchbase.client.java.query.N1qlQuery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.config.BeanNames;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import org.springframework.data.couchbase.repository.support.IndexManager;

import java.util.List;

@Configuration
@EnableCouchbaseRepositories
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("#{'${spring.couchbase.bootstrap-hosts}'.split(',')}")
    private List<String> couchbaseBootstrapHosts;

    @Value("${spring.couchbase.bucket.name}")
    private String bucketName;

    @Value("${spring.couchbase.password}")
    private String couchbasePassword;

    @Value("${spring.couchbase.username}")
    private String couchbaseUsername;

    @Override
    protected List<String> getBootstrapHosts() {
        return couchbaseBootstrapHosts;
    }

    @Override
    protected String getBucketName() {
        return bucketName;
    }

    @Override
    protected String getBucketPassword() {
        return couchbasePassword;
    }

    @Override
    @Bean(name = BeanNames.COUCHBASE_CLUSTER_INFO)
    public ClusterInfo couchbaseClusterInfo() throws Exception {
        return couchbaseCluster().authenticate(couchbaseUsername, couchbasePassword).clusterManager().info();
    }

    @Override
    @ConditionalOnMissingBean(name = BeanNames.COUCHBASE_INDEX_MANAGER)
    @Bean(name = BeanNames.COUCHBASE_INDEX_MANAGER)
    public IndexManager indexManager() {
        return new IndexManager(true, true, true);
    }

    /**
     * Return the {@link Bucket} instance to connect to.
     *
     * @throws Exception on Bean construction failure.
     */
    @Override
    @Bean(destroyMethod = "close", name = BeanNames.COUCHBASE_BUCKET)
    public Bucket couchbaseClient() throws Exception {
        // @Bean method can use another @Bean method in the same @Configuration by directly invoking it
        Bucket bucket = couchbaseCluster().openBucket(getBucketName());

        bucket.query(N1qlQuery.simple("CREATE INDEX index_email ON `crewappbucket`(email)"));

        return bucket;
    }
}

