package account_manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ElasticSearchClient implements SearchClient<ClientDocument> {

    private final RestHighLevelClient restHighLevelClient;

    public ElasticSearchClient(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String[] FIELDS = {"lastName", "firstName", "socialNumber", "city", "street"};
    private static final String[] INDEXES = {"clients"};
    private static final String INDEX = "clients";
    private static final String TYPE = "doc";

    public void bulk(List<ClientDocument> documents) {
        BulkRequest bulkRequest = new BulkRequest();
        documents.stream()
                .map(client -> new IndexRequest(INDEX, TYPE, client.getId().toString())
                        .source(MAPPER.convertValue(client, Map.class)))
                .forEach(bulkRequest::add);
        try {
            BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            log.info("Bulk response status: {}, has failures: {}, indexed documents count: {}",
                    response.status(), response.hasFailures(), response.getItems().length);
        } catch (IOException e) {
            log.error("Error in bulk request: {}", e.getMessage());
        }
    }

    public List<ClientDocument> search(String searchText) {
        SearchSourceBuilder query = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery(searchText, FIELDS));
        SearchRequest searchRequest = new SearchRequest(INDEXES, query);
        try {
            SearchResponse searchResponse = restHighLevelClient
                    .search(searchRequest, RequestOptions.DEFAULT);
            log.info("Total hits found: {}", searchResponse.getHits().getTotalHits());
            return Arrays.stream(searchResponse.getHits().getHits())
                    .map(SearchHit::getSourceAsString)
                    .map(this::convert)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error in search request: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    private ClientDocument convert(String source) {
        try {
            return MAPPER.readValue(source, ClientDocument.class);
        } catch (JsonProcessingException e) {
            log.error("Could not convert {} to Client", source);
        }
        return null;
    }

    public void index(ClientDocument document) {
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, document.getId().toString())
                .source(MAPPER.convertValue(document, Map.class));
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("Index response status: {}", response.status());
        } catch (IOException e) {
            log.error("Error in index request: {}", e.getMessage());
        }
    }

    public void update(ClientDocument document) {
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, document.getId().toString())
                .doc(MAPPER.convertValue(document, Map.class));
        try {
            UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
            log.info("Update response result: {}, document id :{}, ", update.getResult(), document.getId());
        } catch (IOException e) {
            log.error("Error in update request: {}", e.getMessage());
        }
    }

    public void delete(String documentId) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, documentId);
        try {
            DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            log.info("Delete response result: {}, document id :{}, ", delete.getResult(), documentId);
        } catch (IOException e) {
            log.error("Error in delete request: {}", e.getMessage());
        }
    }

    public void refresh() {
        try {
            restHighLevelClient.indices().refresh(new RefreshRequest(INDEX), RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("Error while refreshing indices: {}", e.getMessage());
        }
    }
}
