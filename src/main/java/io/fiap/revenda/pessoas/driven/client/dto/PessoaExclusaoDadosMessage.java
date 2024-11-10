package io.fiap.revenda.pessoas.driven.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutablePessoaExclusaoDadosMessage.class)
@JsonDeserialize(as = ImmutablePessoaExclusaoDadosMessage.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class PessoaExclusaoDadosMessage {
    public abstract String getId();
}