package io.fiap.revenda.pessoas.driver.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import org.immutables.value.Value;

@JsonSerialize(as = ImmutableDocumentoDTO.class)
@JsonDeserialize(as = ImmutableDocumentoDTO.class)
@Value.Immutable
@Value.Style(privateNoargConstructor = true, jdkOnly = true)
public abstract class DocumentoDTO {
    public abstract String getTipo();
    public abstract String getValor();
    public abstract LocalDate getExpiracao();
}