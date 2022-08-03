package br.com.dbccompany.time7.gestaodeensino.repository;

import br.com.dbccompany.time7.gestaodeensino.entity.DisciplinaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DisciplinaRepository extends JpaRepository<DisciplinaEntity, Integer> {

    Optional<DisciplinaEntity> findByNomeIgnoreCase(String nome);
}
