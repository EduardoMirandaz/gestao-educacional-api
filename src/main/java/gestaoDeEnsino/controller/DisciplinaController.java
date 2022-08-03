package gestaoDeEnsino.controller;

import gestaoDeEnsino.dto.disciplina.DisciplinaCreateDTO;
import gestaoDeEnsino.dto.disciplina.DisciplinaDTO;
import gestaoDeEnsino.dto.disciplina.DisciplinaUpdateDTO;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.response.Response;
import gestaoDeEnsino.service.DisciplinaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RequestMapping("/disciplina")
@RestController
@AllArgsConstructor
@Validated
public class DisciplinaController {

    private final DisciplinaService disciplinaService;


    @PostMapping()
    @Response
    @Operation(summary = "Adicionar disciplina", description = "Adiciona uma disciplina, podendo vincular a mesma à um professor")
    public ResponseEntity<DisciplinaDTO> save(@Valid @RequestBody DisciplinaCreateDTO disciplinaCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(disciplinaService.save(disciplinaCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/{idDisciplina}")
    @Response
    @Operation(summary = "Atualizar disciplina", description = "Atualiza uma disciplina, localizando-a por seu ID")
    public ResponseEntity<DisciplinaDTO> update(@PathVariable Integer idDisciplina, @RequestBody DisciplinaUpdateDTO disciplinaUpdateDTO) throws SQLException, RegraDeNegocioException {
        return new ResponseEntity<>(disciplinaService.update(idDisciplina, disciplinaUpdateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idDisciplina}")
    @Response
    @Operation(summary = "Remover disciplina", description = "Remove uma disciplina, localizando-a por seu ID")
    public void delete(@PathVariable Integer idDisciplina) throws RegraDeNegocioException {
        disciplinaService.delete(idDisciplina);
    }


    @PutMapping("/{idDisciplina}/professor/")
    @Response
    @Operation(summary = "Remove o professor da disciplina", description = "Remove um professor a disciplina")
    public ResponseEntity<DisciplinaDTO> deleteProfessorDaDisciplina(@PathVariable("idDisciplina") Integer idDisciplina) throws RegraDeNegocioException {
        return new ResponseEntity<>(disciplinaService.deleteProfessorDaDisciplina(idDisciplina), HttpStatus.OK);
    }

    @GetMapping
    @Response
    @Operation(summary = "Listar disciplinas", description = "Lista todas as disciplinas")
    public ResponseEntity<List<DisciplinaDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(disciplinaService.list(), HttpStatus.OK);
    }

    @GetMapping("/{idDisciplina}")
    @Response
    @Operation(summary = "Listar disciplina por ID", description = "Lista uma disciplina através de seu ID único")
    public ResponseEntity<DisciplinaDTO> getByIdDisciplina(@PathVariable Integer idDisciplina) throws RegraDeNegocioException {
        return new ResponseEntity<>(disciplinaService.listByIdDisciplina(idDisciplina), HttpStatus.OK);
    }
}
