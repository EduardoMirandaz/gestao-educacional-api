package gestaoDeEnsino.dto.professor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorUpdateDTO {

    @Schema(description = "Nome do professor")
    private String nome;

    @Schema(description = "Telefone do professor")
    private String telefone;

    @Schema(description = "E-mail do professor")
    private String email;

    @Schema(description = "ID do endereço do professor no banco de dados")
    private Integer idEndereco;

    @Schema(description = "Salário do professor")
    private Double salario;

    @Schema(description = "Cargo do colaborador")
    private String cargo;
}
