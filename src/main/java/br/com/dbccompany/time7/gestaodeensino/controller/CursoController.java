package br.com.dbccompany.time7.gestaodeensino.controller;

import br.com.dbccompany.time7.gestaodeensino.dto.curso.CursoCreateDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.curso.CursoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.paginacao.PageDTO;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.response.Response;
import br.com.dbccompany.time7.gestaodeensino.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/curso")
@RestController
@Validated
@AllArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    @Response
    @Operation(summary = "Adicionar curso", description = "Insere curso no banco de dados")
    public ResponseEntity<CursoDTO> save(@RequestBody @Valid CursoCreateDTO cursoCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(cursoService.save(cursoCreateDTO), HttpStatus.CREATED);
    }

    @Operation(summary = "Listar curso por ID", description = "Lista um curso, filtrando pelo ID exclusivo")
    @Response
    @PutMapping("/{idCurso}")
    public ResponseEntity<CursoDTO> update(@PathVariable("idCurso") Integer id, @RequestBody @Valid CursoCreateDTO cursoDTOAtualizar) throws RegraDeNegocioException {
        return new ResponseEntity<>(cursoService.update(id, cursoDTOAtualizar), HttpStatus.OK);
    }

    @Operation(summary = "Deletar curso", description = "Deleta curso existente no banco de dados")
    @Response
    @DeleteMapping("/{idCurso}")
    public void delete(@PathVariable("idCurso") Integer id) throws RegraDeNegocioException {
        cursoService.delete(id);
    }

    @Operation(summary = "Listar cursos", description = "Lista todos os cursos do banco")
    @Response
    @GetMapping
    public ResponseEntity<List<CursoDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(cursoService.list(), HttpStatus.OK);
    }

    @Operation(summary = "Listar cursos paginados", description = "Lista todos os cursos do banco de forma paginada")
    @Response
    @GetMapping("paginado")
    public ResponseEntity<PageDTO<CursoDTO>> paginatedList(Integer pagina, Integer quantidadeDeRegistros) {
        return ResponseEntity.ok(cursoService.paginatedList(pagina, quantidadeDeRegistros));
    }

    @Operation(summary = "Adicionar disciplina ao curso", description = "Insere uma disciplina ao curso no banco de dados")
    @Response
    @PostMapping("/{idCurso}/disciplina/{idDisciplina}")
    public ResponseEntity<CursoDTO> saveDisciplinaNoCurso(@PathVariable("idCurso") Integer idCurso, @PathVariable("idDisciplina") Integer idDisciplina) throws RegraDeNegocioException {
        return new ResponseEntity<>(cursoService.saveDisciplinaNoCurso(idCurso, idDisciplina), HttpStatus.OK);
    }

    @Operation(summary = "Remover disciplina do curso", description = "Deleta disciplina do curso no banco de dados")
    @Response
    @DeleteMapping("/{idCurso}/disciplina/{idDisciplina}")
    public ResponseEntity<CursoDTO> deleteDisciplinaDoCurso(@PathVariable("idCurso") Integer idCurso, @PathVariable("idDisciplina") Integer idDisciplina) throws RegraDeNegocioException {
        return new ResponseEntity<>(cursoService.deleteDisciplinaDoCurso(idCurso, idDisciplina), HttpStatus.OK);
    }
}