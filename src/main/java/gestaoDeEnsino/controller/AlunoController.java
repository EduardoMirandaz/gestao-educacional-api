package gestaoDeEnsino.controller;

import gestaoDeEnsino.dto.aluno.AlunoCompletoDTO;
import gestaoDeEnsino.dto.aluno.AlunoCreateDTO;
import gestaoDeEnsino.dto.aluno.AlunoDTO;
import gestaoDeEnsino.dto.aluno.AlunoUpdateDTO;
import gestaoDeEnsino.dto.paginacao.PageDTO;
import gestaoDeEnsino.dto.relatorios.RelatorioAlunosMaioresNotasDTO;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.response.Response;
import gestaoDeEnsino.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RequestMapping("/aluno")
@RestController
@Validated
@AllArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;

    @Operation(summary = "Adicionar aluno", description = "Insere aluno no banco de dados")
    @Response
    @PostMapping
    public ResponseEntity<AlunoDTO> save(@Valid @RequestBody AlunoCreateDTO alunoCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(alunoService.save(alunoCreateDTO));}

    @Operation(summary = "Atualizar aluno", description = "Atualiza aluno existente no banco de dados")
    @Response
    @PutMapping()
    public ResponseEntity<AlunoDTO> update(@Valid @RequestBody AlunoUpdateDTO alunoAtualizar) throws RegraDeNegocioException {
        return ResponseEntity.ok(alunoService.update(alunoAtualizar));
    }

    @Operation(summary = "Deletar aluno", description = "Deleta aluno existente no banco de dados")
    @Response
    @DeleteMapping("/{idAluno}")
    public void delete(@PathVariable("idAluno") Integer id) throws RegraDeNegocioException {
        alunoService.delete(id);
    }

    @Operation(summary = "Listar alunos", description = "Lista todos os alunos do banco")
    @Response
    @GetMapping
    public ResponseEntity<List<AlunoDTO>> list() throws RegraDeNegocioException {
        return ResponseEntity.ok(alunoService.list());
    }

    @Operation(summary = "Listar alunos paginados", description = "Lista todos os alunos paginados do banco")
    @Response
    @GetMapping("paginado")
    public ResponseEntity<PageDTO<AlunoDTO>> paginatedList(Integer pagina, Integer quantidadeDeRegistros) {
        return ResponseEntity.ok(alunoService.paginatedList(pagina, quantidadeDeRegistros));
    }

    @Operation(summary = "Listar aluno por ID", description = "Lista o aluno do banco com o ID informado")
    @Response
    @GetMapping("/{idAluno}")
    public ResponseEntity<AlunoDTO> listById(@PathVariable("idAluno") Integer id) throws RegraDeNegocioException {
        return ResponseEntity.ok(alunoService.listById(id));
    }

    @Operation(summary = "Listar aluno logado", description = "Lista o aluno logado")
    @Response
    @GetMapping("/logged")
    public ResponseEntity<AlunoDTO> listByIdUsuario() throws RegraDeNegocioException {
        return ResponseEntity.ok(alunoService.listByIdUsuario());
    }

    @Response
    @Operation(summary = "Relatório de alunos por ordem de notas",
            description = "Cria um relatório com os alunos ordenados por nota, com seus respectivos nome, curso, matricula e média.")
    @GetMapping("/relatorio-maiores-notas")
    public ResponseEntity<List<RelatorioAlunosMaioresNotasDTO>> relatorioMaioresNotas() {
        return ResponseEntity.ok(alunoService.relatorioAlunoNota());
    }

    @Response
    @Operation(summary = "Relatório paginado com as informações completas sobre os alunos",
            description = "Cria um relatório com os alunos ordenados por matricula, com seus respectivos nome, telefone, email, matricula, curso, logradouro, numero de residencia, cep, cidade, estado e media no curso.")
    @GetMapping("/completo")
    public ResponseEntity<PageDTO<AlunoCompletoDTO>> exibirAlunoCompleto(Integer pagina, Integer quantidadeDeRegistros) {
        return ResponseEntity.ok(alunoService.exibirAlunoCompleto(pagina,quantidadeDeRegistros));
    }
}