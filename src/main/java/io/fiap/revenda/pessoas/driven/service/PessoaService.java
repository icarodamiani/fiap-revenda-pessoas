package io.fiap.revenda.pessoas.driven.service;

import io.fiap.revenda.pessoas.driven.domain.Pessoa;
import io.fiap.revenda.pessoas.driven.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PessoaService {
    private PessoaRepository repository;

    public PessoaService(PessoaRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> save(Pessoa pessoa) {
        return repository.save(pessoa);
    }


    public Mono<Void> deleteById(String id) {
        return repository.deleteById(id);
    }

    public Flux<Pessoa> fetch() {
        return repository.fetch();
    }

    public Mono<Pessoa> findById(String id) {
        return repository.fetchById(id);
    }
}
