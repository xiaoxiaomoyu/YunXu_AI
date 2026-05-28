package com.example.yunxu_ai.rag;

import com.example.yunxu_ai.config.YunxuAgentProperties;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
@ConditionalOnProperty(prefix = "yunxu.agent.rag.pgvector", name = "enabled", havingValue = "true")
public class PgVectorVectorStoreConfig {

    @Bean
    public DataSource pgVectorDataSource(YunxuAgentProperties properties) {
        YunxuAgentProperties.Pgvector pgvector = properties.getRag().getPgvector();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(pgvector.getUrl());
        dataSource.setUsername(pgvector.getUsername());
        dataSource.setPassword(pgvector.getPassword());
        return dataSource;
    }

    @Bean
    public JdbcTemplate pgVectorJdbcTemplate(@Qualifier("pgVectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public VectorStore pgVectorVectorStore(
            @Qualifier("pgVectorJdbcTemplate") JdbcTemplate jdbcTemplate,
            EmbeddingModel dashscopeEmbeddingModel,
            LoveAppDocumentLoader loveAppDocumentLoader,
            YunxuAgentProperties properties) {
        YunxuAgentProperties.Pgvector pgvector = properties.getRag().getPgvector();
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1536)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName(pgvector.getSchemaName())
                .vectorTableName(pgvector.getTableName())
                .maxDocumentBatchSize(10000)
                .build();
        List<Document> documents = loveAppDocumentLoader.loadMarkdowns();
        vectorStore.add(documents);
        return vectorStore;
    }
}
