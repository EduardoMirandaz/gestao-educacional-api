package gestaoDeEnsino.dto.paginacao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PageDTO<T> {
    @Schema(description = "Quantidade de elementos")
    private Long totalElements;

    @Schema(description = "Quantidade de página")
    private Integer totalPages;

    @Schema(description = "Página atual")
    private Integer page;

    @Schema(description = "Quantidade de elementos por página")
    private Integer size;

    @Schema(description = "Objetos paginados")
    private List<T> content;
}