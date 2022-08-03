package br.com.dbccompany.time7.gestaodeensino.dto.relatorios;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioAlunosMaioresNotasDTO {
    @Schema(description = "Nome do aluno")
    private String nomeAluno;

    @Schema(description = "Matrícula do aluno")
    private Integer matricula;

    @Schema(description = "Disciplina do aluno")
    private String disciplina;

    @Schema(description = "Curso do aluno")
    private String curso;

    @Schema(description = "Média das notas na disciplina do aluno")
    private Double media;
}
