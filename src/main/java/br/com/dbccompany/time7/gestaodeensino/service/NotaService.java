package br.com.dbccompany.time7.gestaodeensino.service;


import br.com.dbccompany.time7.gestaodeensino.dto.disciplina.DisciplinaDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.notas.NotaUpdateDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.notas.NotaDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.professor.ProfessorComposeDTO;
import br.com.dbccompany.time7.gestaodeensino.entity.AlunoEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.CursoEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.DisciplinaEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.NotaEntity;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.repository.AlunoRepository;
import br.com.dbccompany.time7.gestaodeensino.repository.CursoRepository;
import br.com.dbccompany.time7.gestaodeensino.repository.NotaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NotaService {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    private final ObjectMapper objectMapper;
    private final UsuarioService usuarioService;

    public void adicionarNotasAluno(Integer idCurso, Integer idAluno) throws RegraDeNegocioException {
        log.info("Criando notas...");

        CursoEntity cursoEntityRecuperado = cursoRepository.findById(idCurso).orElseThrow(() -> new RegraDeNegocioException("Curso não encontrado"));
        AlunoEntity alunoEntityRecuperado = alunoRepository.findById(idAluno).orElseThrow(() -> new RegraDeNegocioException("Aluno não encontrado"));

        cursoEntityRecuperado.getDisciplinasEntities().forEach(disciplinaEntity -> {
            NotaEntity notaEntityNova = new NotaEntity();
            notaEntityNova.setAlunoEntity(alunoEntityRecuperado);
            notaEntityNova.setDisciplinaEntity(disciplinaEntity);
            notaRepository.save(notaEntityNova);
        });

        log.info("Notas adicionadas");
    }

    public void adicionarNotasAlunosDoCursoByDisciplina(DisciplinaEntity disciplinaEntity, CursoEntity cursoEntity){
        log.info("Adicionando notas da nova disciplina para os alunos do curso...");

        cursoEntity.getAlunosEntities().forEach(alunoEntity -> {
            NotaEntity notaEntityNova = new NotaEntity();
            notaEntityNova.setAlunoEntity(alunoEntity);
            notaEntityNova.setDisciplinaEntity(disciplinaEntity);
            notaRepository.save(notaEntityNova);
        });
    }

    public NotaDTO atualizarNotasAluno(Integer idNota, NotaUpdateDTO notaUpdateDTO) throws RegraDeNegocioException {
        int divisor = 0;
        Double media = 0.0;

        log.info("Atualizando notas de aluno");

        NotaEntity notaEntityRecuperada = notaRepository.findById(idNota)
                .orElseThrow(() -> new RegraDeNegocioException("Nota não encontrada"));

        NotaEntity notaEntityAtualizar = updateToEntity(notaUpdateDTO);

        if (notaEntityAtualizar.getNota1() == null){
            notaEntityAtualizar.setNota1(notaEntityRecuperada.getNota1());
        }
        if (notaEntityAtualizar.getNota2() == null){
            notaEntityAtualizar.setNota2(notaEntityRecuperada.getNota2());
        }
        if (notaEntityAtualizar.getNota3() == null){
            notaEntityAtualizar.setNota3(notaEntityRecuperada.getNota3());
        }
        if (notaEntityAtualizar.getNota4() == null){
            notaEntityAtualizar.setNota4(notaEntityRecuperada.getNota4());
        }

        if (notaEntityAtualizar.getNota1() != null) {
            divisor += 1;
            media += notaEntityAtualizar.getNota1();
        }
        if (notaEntityAtualizar.getNota2() != null) {
            divisor += 1;
            media += notaEntityAtualizar.getNota2();
        }
        if (notaEntityAtualizar.getNota3() != null) {
            divisor += 1;
            media += notaEntityAtualizar.getNota3();
        }
        if (notaEntityAtualizar.getNota4() != null) {
            divisor += 1;
            media += notaEntityAtualizar.getNota4();
        }

        if (divisor > 0){
            media /= divisor;
        }

        notaEntityAtualizar.setIdNota(idNota);
        notaEntityAtualizar.setMedia(media);
        notaEntityAtualizar.setDisciplinaEntity(notaEntityRecuperada.getDisciplinaEntity());
        notaEntityAtualizar.setAlunoEntity(notaEntityRecuperada.getAlunoEntity());

        NotaDTO notaDTO = entityToDTO(notaRepository.save(notaEntityAtualizar));

        log.info("Nota adicionada");
        return notaDTO;
    }

    @Transactional
    public void deletarNotasAlunosDoCursoByDisciplinaAndAluno(DisciplinaEntity disciplinaEntity, AlunoEntity alunoEntity){
        log.info("Deletando notas de disciplina removida do curso");

        notaRepository.deleteAllByDisciplinaEntityAndAlunoEntity(disciplinaEntity, alunoEntity);
    }

    @Transactional
    public void deleteAllNotasByIdAluno(Integer idAluno) {
        log.info("Deletando notas do aluno " + idAluno);

        notaRepository.deleteAllByAlunoEntity_IdAluno(idAluno);
    }

    public List<NotaDTO> findByIdAluno(Integer idAluno) {
        log.info("Listando notas por ID do aluno");

        return notaRepository.findAllByAlunoEntity_IdAluno(idAluno).stream()
                .map(this::entityToDTO).toList();
    }

    public List<NotaDTO> findByAlunoLogged() throws RegraDeNegocioException {
        log.info("Listando notas por ID do aluno");
        AlunoEntity alunoEntity = alunoRepository.findByIdUsuario(usuarioService.getIdLoggedUser())
                .orElseThrow(() -> new RegraDeNegocioException("Aluno não encontrado"));
        return notaRepository.findAllByAlunoEntity_IdAluno(alunoEntity.getIdAluno()).stream()
                .map(this::entityToDTO).toList();
    }
     public NotaEntity updateToEntity(NotaUpdateDTO notaUpdateDTO) {
        return objectMapper.convertValue(notaUpdateDTO, NotaEntity.class);
    }

    public NotaDTO entityToDTO(NotaEntity notaEntity) {
        NotaDTO notaDTO = objectMapper.convertValue(notaEntity, NotaDTO.class);
        notaDTO.setDisciplinaDTO(objectMapper.convertValue(notaEntity.getDisciplinaEntity(), DisciplinaDTO.class));
        notaDTO.getDisciplinaDTO().setProfessor(objectMapper.convertValue(notaEntity.getDisciplinaEntity().getProfessorEntity(), ProfessorComposeDTO.class));
        return notaDTO;
    }
}
