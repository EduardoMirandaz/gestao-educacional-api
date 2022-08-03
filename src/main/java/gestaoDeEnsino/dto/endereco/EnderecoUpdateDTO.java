package gestaoDeEnsino.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoUpdateDTO {

    @Schema(description = "Logradouro do endereço")
    @Size(max = 250, message = "Informe um logradouro com até 250 carácteres")
    private String logradouro;

    @Schema(description = "Cidade do endereço")
    @Size(max = 250, message = "Informe uma cidade com até 250 carácteres")
    private String cidade;

    @Schema(description = "Estado do endereço")
    private String estado;

    @Schema(description = "CEP do endereço", maxLength = 8, minLength = 8)
    @Size(max = 9, min = 9, message = "O CEP deve conter somente 9 digitos")
    private String cep;

    @Schema(description = "Complemento (opcional) do endereço")
    private String complemento;

    @Schema(description = "Número do endereço")
    private Integer numero;
}
