package io.fiap.revenda.pessoas.driven.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableDocumento.class)
@JsonDeserialize(as = ImmutableDocumento.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class Documento {
    public abstract String getTipo();
    public abstract String getValor();
    public abstract LocalDate getExpiracao();
}
