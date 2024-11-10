package io.fiap.revenda.pessoas.driven.domain.mapper;

import io.fiap.revenda.pessoas.driven.domain.Documento;
import io.fiap.revenda.pessoas.driver.controller.dto.DocumentoDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentoMapper extends BaseMapper<DocumentoDTO, Documento> {
}
