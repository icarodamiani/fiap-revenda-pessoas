package io.fiap.revenda.pessoas.driven.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fiap.revenda.pessoas.driven.client.dto.PessoaExclusaoDadosMessage;
import io.fiap.revenda.pessoas.driven.exception.BusinessException;
import io.fiap.revenda.pessoas.driven.port.MessagingPort;
import io.vavr.CheckedFunction1;
import io.vavr.Function1;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

@Service
public class LgpdService {
    private final PessoaService pessoaService;
    private final MessagingPort messagingPort;
    private final ObjectMapper objectMapper;
    private final String queue;

    public LgpdService(PessoaService pessoaService,
                       MessagingPort messagingPort,
                       ObjectMapper objectMapper,
                       @Value("${aws.sqs.pessoaExclusaoDados.queue}")
                       String queue) {
        this.pessoaService = pessoaService;
        this.messagingPort = messagingPort;
        this.objectMapper = objectMapper;
        this.queue = queue;
    }

    public Mono<Void> eliminarDadosPessoais(String pessoaId) {
        return pessoaService.findById(pessoaId)
            .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException("Pessoa não localizada."))))
            .flatMap(request -> messagingPort.send(queue, request, serializePayload()))
            .then();
    }

    private <T> CheckedFunction1<T, String> serializePayload() {
        return objectMapper::writeValueAsString;
    }


    public Flux<Message> handleEliminarDadosPessoais() {
        return messagingPort.read(queue, handle(), readEvent());
    }

    private CheckedFunction1<Message, PessoaExclusaoDadosMessage> readEvent() {
        return message -> objectMapper.readValue(message.body(), PessoaExclusaoDadosMessage.class);
    }

    private Function1<PessoaExclusaoDadosMessage, Mono<PessoaExclusaoDadosMessage>> handle() {
        return request -> Mono.just(request)
            .flatMap(r -> pessoaService.deleteById(r.getId()))
            .then(Mono.just(request));
    }
}
