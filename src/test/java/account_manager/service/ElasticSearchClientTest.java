package account_manager.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.elasticsearch.rest.RestClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RestClientAutoConfiguration.class, ElasticSearchClient.class})
@ContextConfiguration(initializers = {ElasticSearchClientTest.Initializer.class})
@Slf4j
public class ElasticSearchClientTest {
    private static final String ELASTICSEARCH_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch:6.8.7";
    private static ElasticsearchContainer container;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String INDEX = "clients";
    private static final String TYPE = "doc";
    private static final ClientDocument CLIENT_1 = new ClientDocument(1, "Doe", "John",
            "123456789", "Some City", "Some Street", "123", "123");
    private static final ClientDocument CLIENT_2 = new ClientDocument(2, "Doe", "Jane",
            "987654321", "Some City", "Some Street", "321", "321");

    @Autowired
    private ElasticSearchClient elasticSearchClient;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @BeforeClass
    public static void startContainer() {
        container = new ElasticsearchContainer(ELASTICSEARCH_IMAGE);
        container.start();
        log.info("Elasticsearch container started");
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.elasticsearch.rest.uris=" + container.getHttpHostAddress()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void index() {
        elasticSearchClient.index(CLIENT_1);
        ClientDocument documentFound = findById(CLIENT_1.getId());
        assertEquals(CLIENT_1, documentFound);
    }

    @Test
    public void update() {
        elasticSearchClient.index(CLIENT_1);
        ClientDocument expectedClient = new ClientDocument(1, "Doe", "Jane",
                "123456789", "Some City", "Some Street", "123", "123");
        elasticSearchClient.update(expectedClient);
        ClientDocument updatedDocument = findById(CLIENT_1.getId());
        assertEquals(expectedClient, updatedDocument);
    }

    @Test
    public void delete() {
        elasticSearchClient.index(CLIENT_1);
        elasticSearchClient.delete(CLIENT_1.getId().toString());
        assertNull(findById(CLIENT_1.getId()));
    }

    @Test
    public void bulk() {
        List<ClientDocument> clients = List.of(CLIENT_1, CLIENT_2);
        elasticSearchClient.bulk(clients);
        assertEquals(CLIENT_1, findById(CLIENT_1.getId()));
        assertEquals(CLIENT_2, findById(CLIENT_2.getId()));
    }

    @Test
    public void search() {
        elasticSearchClient.index(CLIENT_1);
        elasticSearchClient.index(CLIENT_2);
        elasticSearchClient.refresh();
        List<ClientDocument> clients = elasticSearchClient.search("Doe");
        assertThat(clients).containsExactlyInAnyOrder(CLIENT_1, CLIENT_2);
    }

    @SneakyThrows
    private ClientDocument findById(Integer id) {
        GetRequest getRequest = new GetRequest(INDEX, TYPE, id.toString());
        Map<String, Object> source = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT).getSource();
        return MAPPER.convertValue(source, ClientDocument.class);
    }
}