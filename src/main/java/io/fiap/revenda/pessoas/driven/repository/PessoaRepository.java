package io.fiap.revenda.pessoas.driven.repository;

import io.fiap.revenda.pessoas.driven.domain.ImmutableDocumento;
import io.fiap.revenda.pessoas.driven.domain.ImmutablePessoa;
import io.fiap.revenda.pessoas.driven.domain.Pessoa;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

@Repository
public class PessoaRepository {
    private static final String TABLE_NAME = "pessoas_tb";

    private final DynamoDbAsyncClient client;

    public PessoaRepository(DynamoDbAsyncClient client) {
        this.client = client;
    }

    public Mono<Void> save(Pessoa pessoa) {
        var atributos = new HashMap<String, AttributeValueUpdate>();
        atributos.put("NOME",
            AttributeValueUpdate.builder().value(v -> v.s(pessoa.getNome()).build()).build());
        atributos.put("SOBRENOME",
            AttributeValueUpdate.builder().value(v -> v.s(pessoa.getSobrenome()).build()).build());

        var documento = new HashMap<String, AttributeValue>();
        documento.put("TIPO", AttributeValue.builder().s(pessoa.getDocumento().getTipo()).build());
        documento.put("VALOR", AttributeValue.builder().s(pessoa.getDocumento().getValor()).build());
        documento.put("EXPIRACAO",
            AttributeValue.builder().s(String.valueOf(pessoa.getDocumento().getExpiracao().toEpochDay())).build());

        atributos.put("DOCUMENTO",
            AttributeValueUpdate.builder().value(v -> v.m(documento).build()).build());

        var request = UpdateItemRequest.builder()
            .attributeUpdates(atributos)
            .tableName(TABLE_NAME)
            .key(Map.of("ID", AttributeValue.fromS(pessoa.getId())))
            .build();

        return Mono.fromFuture(client.updateItem(request))
            .then();
    }

    public Mono<Void> deleteById(String id) {
        var key = new HashMap<String, AttributeValue>();
        key.put("ID", AttributeValue.fromS(id));

        var request = DeleteItemRequest.builder()
            .key(key)
            .tableName(TABLE_NAME)
            .build();

        return Mono.fromFuture(client.deleteItem(request))
            .then();
    }

    public Flux<Pessoa> fetch() {
        return Mono.fromFuture(client.scan(ScanRequest.builder().tableName(TABLE_NAME).build()))
            .filter(ScanResponse::hasItems)
            .map(response -> response.items()
                .stream()
                .map(this::convertItem)
                .toList()
            )
            .flatMapIterable(l -> l);
    }

    public Mono<Pessoa> fetchById(String id) {
        var request = QueryRequest.builder()
            .tableName(TABLE_NAME)
            .keyConditionExpression("#id = :id")
            .expressionAttributeNames(Map.of("#id", "ID"))
            .expressionAttributeValues(Map.of(":id", AttributeValue.fromS(id)))
            .build();

        return Mono.fromFuture(client.query(request))
            .filter(QueryResponse::hasItems)
            .map(response -> response.items().get(0))
            .map(this::convertItem);
    }

    private ImmutablePessoa convertItem(Map<String, AttributeValue> item) {
        return ImmutablePessoa.builder()
            .id(item.get("ID").s())
            .nome(item.get("NOME").s())
            .sobrenome(item.get("SOBRENOME").s())
            .documento(ImmutableDocumento.builder()
                .tipo(item.get("DOCUMENTO").m().get("TIPO").s())
                .valor(item.get("DOCUMENTO").m().get("VALOR").s())
                .expiracao(LocalDate.ofEpochDay(
                        Long.parseLong(item.get("DOCUMENTO").m().get("EXPIRACAO").s())
                    )
                )
                .build()
            )
            .build();
    }
}
