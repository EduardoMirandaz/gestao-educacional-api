package br.com.dbccompany.time7.gestaodeensino.dto.aluno;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlunoCompletoDTO {
    @Schema(description = "Nome do aluno")
    private String nomeAluno;

    @Schema(description = "Telefone do aluno")
    private String telefone;

    @Schema(description = "E-mail do aluno")
    private String email;

    @Schema(description = "Número de matrícula do aluna")
    private Integer matricula;

    @Schema(description = "Curso do aluno")
    private String curso;

    @Schema(description = "Logradouro do endereço do aluno")
    private String logradouroResidencia;

    @Schema(description = "Número do endereço do aluno")
    private Integer numeroResidencia;

    @Schema(description = "Cep do endereço do aluno")
    private String cepResidencia;

    @Schema(description = "Cidade do endereço do aluno")
    private String cidadeResidencia;

    @Schema(description = "Estado do endereço do aluno")
    private String estadoResidencia;

}
