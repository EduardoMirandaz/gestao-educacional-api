package br.com.dbccompany.time7.gestaodeensino.dto.professor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorUpdateDTO {

    @Schema(description = "Nome do professor")
    private String nome;

    @Schema(description = "Telefone do professor")
    private String telefone;

    @Schema(description = "ID do endereço do professor no banco de dados")
    private Integer idEndereco;

    @Schema(description = "Salário do professor")
    private Double salario;

    @Schema(description = "Cargo do colaborador")
    private String cargo;
}
