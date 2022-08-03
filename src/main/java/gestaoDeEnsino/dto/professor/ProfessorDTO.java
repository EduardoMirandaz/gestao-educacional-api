package gestaoDeEnsino.dto.professor;

import gestaoDeEnsino.dto.endereco.EnderecoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorDTO {

    @Schema(description = "ID do professor")
    private Integer idProfessor;

    @Schema(description = "Registro de trabalho do professor")
    private Integer registroTrabalho;

    @Schema(description = "Nome do professor")
    private String nome;

    @Schema(description = "Telefone do professor")
    @NotBlank
    private String telefone;

    @Schema(description = "E-mail do professor")
    @NotBlank
    private String email;

    @Schema(description = "Salário do professor")
    @NotNull
    private Double salario;

    @Schema(description = "Cargo do colaborador")
    private String cargo;

    @Schema(description = "ID do endereço do professor no banco de dados")
    private EnderecoDTO endereco;
}
