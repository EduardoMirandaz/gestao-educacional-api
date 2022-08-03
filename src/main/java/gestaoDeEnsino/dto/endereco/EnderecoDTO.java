package gestaoDeEnsino.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTO extends EnderecoCreateDTO{

    @Schema(description = "ID exclusivo do endereço")
    private Integer idEndereco;
}
