package io.fiap.revenda.pessoas.driver.controller;

import io.fiap.revenda.pessoas.driven.domain.mapper.PessoaMapper;
import io.fiap.revenda.pessoas.driven.service.PessoaService;
import io.fiap.revenda.pessoas.driver.controller.dto.PessoaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.security.Principal;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "OAuth2")
@RestController
@RequestMapping(value = "/pessoas", produces = MediaType.APPLICATION_JSON_VALUE)
public class PessoaController {

    private final PessoaService pessoaService;
    private final PessoaMapper pessoaMapper;

    public PessoaController(PessoaService pessoaService, PessoaMapper pessoaMapper) {
        this.pessoaService = pessoaService;
        this.pessoaMapper = pessoaMapper;
    }

    @PostMapping
    @Operation(description = "Cria uma nova pessoa")
    public Mono<Void> save(@RequestBody PessoaDTO pessoa) {
        return Mono.fromSupplier(() -> pessoaMapper.domainFromDto(pessoa))
            .flatMap(pessoaService::save);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Deleta uma pessoa por seu ID")
    public Mono<Void> deleteById(@PathVariable String id) {
        return pessoaService.deleteById(id);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Operation(description = "Busca todas as pessoas")
    public Flux<PessoaDTO> fetch(@AuthenticationPrincipal Principal principal) {
        return pessoaService.fetch()
            .map(pessoaMapper::dtoFromDomain);
    }

    @GetMapping("/{id}")
    @Operation(description = "Busca uma pessoa por seu ID")
    public Mono<PessoaDTO> fetchById(@PathVariable String id) {
        return pessoaService.findById(id)
            .map(pessoaMapper::dtoFromDomain);
    }
}
