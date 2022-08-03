package br.com.dbccompany.time7.gestaodeensino.dto.notas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotaUpdateDTO {

    @Schema(description = "Nota 1 do Aluno")
    private Double nota1;

    @Schema(description = "Nota 2 do Aluno")
    private Double nota2;

    @Schema(description = "Nota 3 do Aluno")
    private Double nota3;

    @Schema(description = "Nota 4 do Aluno")
    private Double nota4;
}
