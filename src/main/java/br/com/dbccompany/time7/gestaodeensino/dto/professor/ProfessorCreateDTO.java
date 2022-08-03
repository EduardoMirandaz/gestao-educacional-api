package br.com.dbccompany.time7.gestaodeensino.dto.professor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorCreateDTO {

    @Schema(description = "Nome do professor")
    @NotBlank
    private String nome;

    @Schema(description = "Telefone do professor")
    @NotBlank
    private String telefone;

    @Schema(description = "ID do endereço do professor no banco de dados")
    private Integer idEndereco;

    @Schema(description = "ID da disciplina que o professor ira atuar")
    private Integer idDisciplina;

    @Schema(description = "Salário do professor")
    @NotNull
    private Double salario;

    @Schema(description = "Cargo do colaborador")
    private String cargo;
}
