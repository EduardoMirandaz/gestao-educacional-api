package br.com.dbccompany.time7.gestaodeensino.dto.curso;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {

    @Schema(description = "ID do curso")
    private Integer idCurso;

    @Schema(description = "Nome do curso")
    private String nome;
}
