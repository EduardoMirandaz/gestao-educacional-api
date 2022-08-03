package br.com.dbccompany.time7.gestaodeensino.dto.relatorios;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioProfessoresMenoresSalariosDTO {

    @Schema(description = "Registro de trabalho do professor")
    private Integer registroTrabalho;

    @Schema(description = "Nome do pressor")
    private String nomeProfessor;

    @Schema(description = "Salário do professor")
    private Double salario;
}
