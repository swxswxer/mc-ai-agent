package com.swx.swxaiagent.ai.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @program: swx-ai-agent
 * @ClassName: McAppVectorStoreConfig
 * @description:
 * @author:
 * @create: 2025/5/29 17:04
 */
@Configuration
public class McAppVectorStoreConfig {

    @Resource
    private McAppDocumentLoader mcAppDocumentLoader;

    @Bean
    VectorStore mcAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        // 加载文档
        List<Document> documents = mcAppDocumentLoader.loadMarkdowns();
        if (documents != null && !documents.isEmpty()){
            simpleVectorStore.add(documents);
        }
        return simpleVectorStore;
    }
}

