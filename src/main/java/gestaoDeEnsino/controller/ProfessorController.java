package gestaoDeEnsino.controller;

import gestaoDeEnsino.dto.paginacao.PageDTO;
import gestaoDeEnsino.dto.professor.ProfessorCreateDTO;
import gestaoDeEnsino.dto.professor.ProfessorDTO;
import gestaoDeEnsino.dto.professor.ProfessorUpdateDTO;
import gestaoDeEnsino.dto.relatorios.RelatorioProfessoresMenoresSalariosDTO;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.response.Response;
import gestaoDeEnsino.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RequestMapping("/professor")
@RestController
@Validated
@AllArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @PostMapping
    @Response
    @Operation(summary = "Adicionar professor", description = "Insere professor no banco de dados")
    public ResponseEntity<ProfessorDTO> save(@RequestBody @Valid ProfessorCreateDTO professorCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(professorService.save(professorCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping()
    @Response
    @Operation(summary = "Atualizar professor", description = "Atualiza professor existente no banco de dados")
    public ResponseEntity<ProfessorDTO> update(@RequestBody @Valid ProfessorUpdateDTO professorDTOAtualizar) throws RegraDeNegocioException {
        return new ResponseEntity<>(professorService.update(professorDTOAtualizar), HttpStatus.OK);
    }

    @DeleteMapping("/{idProfessor}")
    @Response
    @Operation(summary = "Deletar professor", description = "Deleta professor existente no banco de dados")
    public void delete(@PathVariable("idProfessor") Integer id) throws SQLException, RegraDeNegocioException {
        professorService.delete(id);
    }

    @GetMapping
    @Response
    @Operation(summary = "Listar professores", description = "Lista todos os professores do banco")
    public ResponseEntity<List<ProfessorDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(professorService.list(), HttpStatus.OK);
    }

    @GetMapping("/paginado")
    @Response
    @Operation(summary = "Listar professores paginados", description = "Lista todos os professores paginados do banco")
    public PageDTO<ProfessorDTO> paginatedList(Integer pagina, Integer quantidadeDeRegistros) {
        return professorService.paginatedList(pagina, quantidadeDeRegistros);
    }

    @GetMapping("/{idProfessor}")
    @Response
    @Operation(summary = "Listar professor por ID", description = "Lista o professor do banco com o ID informado")
    public ResponseEntity<ProfessorDTO> listById(@PathVariable("idProfessor") Integer idProfessor) throws RegraDeNegocioException {
        return new ResponseEntity<>(professorService.listById(idProfessor), HttpStatus.OK);
    }

    @GetMapping("/logged")
    @Response
    @Operation(summary = "Listar professor logado", description = "Lista o professor logado")
    public ResponseEntity<ProfessorDTO> listByIdUsuario() throws RegraDeNegocioException {
        return new ResponseEntity<>(professorService.listByIdUsuario(), HttpStatus.OK);
    }

    @GetMapping("/byNome/{nome}")
    @Response
    @Operation(summary = "Listar professores por nome", description = "Lista todos professores do banco que contem o nome informado")
    public ResponseEntity<List<ProfessorDTO>> listByNome(@PathVariable("nome") String nomeProfessor) {
        return new ResponseEntity<>(professorService.listByName(nomeProfessor), HttpStatus.OK);
    }

    @GetMapping("/relatorio-maior-salario")
    @Response
    @Operation(summary = "Relat??rio com os professores por ordem decrescente de sal??rios",
            description = "Cria um relat??rio com os professores ordenados de forma decrescente por sal??rio mensal, com seus respectivos nomes, registro de trabalho, sal??rio e disciplinas ministradas.")
    public ResponseEntity<List<RelatorioProfessoresMenoresSalariosDTO>> relatorioProfessorSalario() {
        return ResponseEntity.ok(professorService.relatorioProfessorSalario());
    }
}