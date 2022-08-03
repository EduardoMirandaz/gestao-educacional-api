package gestaoDeEnsino.repository;

import gestaoDeEnsino.entity.AlunoEntity;
import gestaoDeEnsino.entity.DisciplinaEntity;
import gestaoDeEnsino.entity.NotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<NotaEntity, Integer> {

    List<NotaEntity> findAllByAlunoEntity_IdAluno(Integer idAluno);

    void deleteAllByAlunoEntity_IdAluno(Integer idAluno);

    void deleteAllByDisciplinaEntityAndAlunoEntity(DisciplinaEntity disciplinaEntity, AlunoEntity alunoEntity);
}
