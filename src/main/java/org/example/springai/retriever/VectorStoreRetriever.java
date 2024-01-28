package org.example.springai.retriever;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

/**
 * spring-ai 에서 삭제된 클래스 들고옴
 * 아래는 해당 커밋메시지
 * b35c71061ed1560241a4e3708775cd703acf2553
 */
public class VectorStoreRetriever implements DocumentRetriever {

    private VectorStore vectorStore;

    int k;

    Optional<Double> threshold = Optional.empty();

    public VectorStoreRetriever(VectorStore vectorStore) {
        this(vectorStore, 4);
    }

    public VectorStoreRetriever(VectorStore vectorStore, int k) {
        Objects.requireNonNull(vectorStore, "VectorStore must not be null");
        this.vectorStore = vectorStore;
        this.k = k;
    }

    public VectorStoreRetriever(VectorStore vectorStore, int k, double threshold) {
        Objects.requireNonNull(vectorStore, "VectorStore must not be null");
        this.vectorStore = vectorStore;
        this.k = k;
        this.threshold = Optional.of(threshold);
    }

    public VectorStore getVectorStore() {
        return vectorStore;
    }

    public int getK() {
        return k;
    }

    public Optional<Double> getThreshold() {
        return threshold;
    }

    @Override
    public List<Document> retrieve(String query) {

        SearchRequest request = SearchRequest.query(query).withTopK(this.k);
        if (threshold.isPresent()) {
            request.withSimilarityThreshold(this.threshold.get());
        }

        return this.vectorStore.similaritySearch(request);
    }

}
