package br.com.dbccompany.time7.gestaodeensino.service;

import br.com.dbccompany.time7.gestaodeensino.dto.curso.CursoCreateDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.curso.CursoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.paginacao.PageDTO;
import br.com.dbccompany.time7.gestaodeensino.entity.CursoEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.DisciplinaEntity;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.repository.AlunoRepository;
import br.com.dbccompany.time7.gestaodeensino.repository.CursoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class CursoService {
    private final CursoRepository cursoRepository;
    private final DisciplinaService disciplinaService;
    private final NotaService notaService;
    private final AlunoRepository alunoRepository;
    private final ObjectMapper objectMapper;


    public CursoDTO save(CursoCreateDTO cursoCreateDTO) throws RegraDeNegocioException {
        log.info("Criando curso...");

        if (containsCurso(cursoCreateDTO)) {
            log.info("O curso " + cursoCreateDTO.getNome() + " já existe no banco");
            throw new RegraDeNegocioException("O curso já existe no banco de dados");
        } else {
            CursoEntity cursoEntity = createToEntity(cursoCreateDTO);

            cursoEntity = cursoRepository.save(cursoEntity);

            CursoDTO cursoDTO = entityToDTO(cursoEntity);

            log.info("Curso " + cursoDTO.getNome() + " adicionado ao banco de dados");

            return cursoDTO;
        }
    }

    public CursoDTO update(Integer idCurso, CursoCreateDTO cursoCreateDTOAtualizar) throws RegraDeNegocioException {
        log.info("Atualizando curso...");

        if (containsCurso(cursoCreateDTOAtualizar)) {
            log.info("O nome " + cursoCreateDTOAtualizar.getNome() + " já existe no banco");
            throw new RegraDeNegocioException("Falha ao atualizar nome do curso, esse nome já existe no banco");
        } else {
            CursoEntity cursoEntityRecuperado = findById(idCurso);
            CursoEntity cursoEntityAtualizar = createToEntity(cursoCreateDTOAtualizar);

            cursoEntityAtualizar.setIdCurso(idCurso);
            cursoEntityAtualizar.setDisciplinasEntities(cursoEntityRecuperado.getDisciplinasEntities());
            cursoEntityAtualizar.setAlunosEntities(cursoEntityRecuperado.getAlunosEntities());

            cursoRepository.save(cursoEntityAtualizar);

            log.info(cursoEntityAtualizar.getNome() + " atualizado");

            return entityToDTO(cursoEntityAtualizar);
        }
    }

    public void delete(Integer idCurso) throws RegraDeNegocioException {
        log.info("Deletando o curso...");

        CursoEntity cursoEntityRecuperado = findById(idCurso);

        cursoEntityRecuperado.setAlunosEntities(cursoEntityRecuperado.getAlunosEntities());
        cursoEntityRecuperado.getAlunosEntities().forEach(alunoEntity -> {
            alunoEntity.setCursoEntity(null);
            alunoEntity.setEnderecoEntity(alunoEntity.getEnderecoEntity());
            alunoEntity.setNotaEntities(alunoEntity.getNotaEntities());
            notaService.deleteAllNotasByIdAluno(alunoEntity.getIdAluno());
            alunoRepository.save(alunoEntity);
        });
        cursoEntityRecuperado.setDisciplinasEntities(cursoEntityRecuperado.getDisciplinasEntities());

        cursoRepository.delete(cursoEntityRecuperado);

        log.info("Curso removido");
    }

    public List<CursoDTO> list() throws RegraDeNegocioException {
        log.info("Listando todos os cursos");

        return cursoRepository.findAll().stream()
                .map(this::entityToDTO)
                .toList();
    }

    public CursoDTO saveDisciplinaNoCurso(Integer idCurso, Integer idDisciplina) throws RegraDeNegocioException {
        log.info("Adicionado disciplina no curso");

        CursoEntity cursoEntityRecuperado = findById(idCurso);
        DisciplinaEntity disciplinaEntityRecuperada = disciplinaService.findById(idDisciplina);

        cursoEntityRecuperado.setAlunosEntities(cursoEntityRecuperado.getAlunosEntities());
        cursoEntityRecuperado.setDisciplinasEntities(cursoEntityRecuperado.getDisciplinasEntities());
        cursoEntityRecuperado.getDisciplinasEntities().add(disciplinaEntityRecuperada);

        CursoDTO cursoDTOAtualizado = entityToDTO(cursoRepository.save(cursoEntityRecuperado));

        notaService.adicionarNotasAlunosDoCursoByDisciplina(disciplinaEntityRecuperada, cursoEntityRecuperado);

        return cursoDTOAtualizado;
    }

    public CursoDTO deleteDisciplinaDoCurso(Integer idCurso, Integer idDisciplina) throws RegraDeNegocioException {
        log.info("Removendo disciplina do curso");

        CursoEntity cursoEntityRecuperado = findById(idCurso);
        DisciplinaEntity disciplinaEntityRecuperada = disciplinaService.findById(idDisciplina);

        cursoEntityRecuperado.setAlunosEntities(cursoEntityRecuperado.getAlunosEntities());
        cursoEntityRecuperado.setDisciplinasEntities(cursoEntityRecuperado.getDisciplinasEntities());
        cursoEntityRecuperado.getDisciplinasEntities().remove(disciplinaEntityRecuperada);

        CursoDTO cursoDTO = entityToDTO(cursoRepository.save(cursoEntityRecuperado));

        cursoEntityRecuperado.getAlunosEntities().forEach(alunoEntity -> notaService.deletarNotasAlunosDoCursoByDisciplinaAndAluno(disciplinaEntityRecuperada, alunoEntity));

        return cursoDTO;
    }

    public CursoEntity findById(Integer idCurso) throws RegraDeNegocioException {
        return cursoRepository.findById(idCurso)
                .orElseThrow(() -> new RegraDeNegocioException("Curso não encontrado"));
    }

    public boolean containsCurso(CursoCreateDTO cursoCreateDTO) {
        return cursoRepository.findByNomeIgnoreCase(cursoCreateDTO.getNome()).isPresent();
    }

    public CursoDTO entityToDTO(CursoEntity cursoEntity) {
        return objectMapper.convertValue(cursoEntity, CursoDTO.class);
    }

    public CursoEntity createToEntity(CursoCreateDTO cursoCreateDTO) {
        return objectMapper.convertValue(cursoCreateDTO, CursoEntity.class);
    }

    public PageDTO<CursoDTO> paginatedList(Integer pagina, Integer quantidadeDeRegistros) {
        log.info("Listando cursos com paginação");

        PageRequest pageRequest = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<CursoEntity> page = cursoRepository.findAll(pageRequest);
        List<CursoDTO> cursoDTOS = page.getContent().stream()
                .map(this::entityToDTO)
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, cursoDTOS);
    }


}
