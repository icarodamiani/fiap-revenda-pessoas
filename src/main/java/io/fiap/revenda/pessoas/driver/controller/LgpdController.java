package io.fiap.revenda.pessoas.driver.controller;

import io.fiap.revenda.pessoas.driven.service.LgpdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "OAuth2")
@RestController
@RequestMapping(value = "/lgpd/pessoas", produces = MediaType.APPLICATION_JSON_VALUE)
public class LgpdController {

    private final LgpdService service;

    public LgpdController(LgpdService service) {
        this.service = service;
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Dispara a remoção completa dos dados da pessoa no sistema.")
    public Mono<Void> deleteById(@PathVariable String id) {
        return service.eliminarDadosPessoais(id);
    }
}
