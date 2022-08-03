package br.com.dbccompany.time7.gestaodeensino.repository;

import br.com.dbccompany.time7.gestaodeensino.entity.AlunoEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.DisciplinaEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.NotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaRepository extends JpaRepository<NotaEntity, Integer> {

    List<NotaEntity> findAllByAlunoEntity_IdAluno(Integer idAluno);

    void deleteAllByAlunoEntity_IdAluno(Integer idAluno);

    void deleteAllByDisciplinaEntityAndAlunoEntity(DisciplinaEntity disciplinaEntity, AlunoEntity alunoEntity);
}
