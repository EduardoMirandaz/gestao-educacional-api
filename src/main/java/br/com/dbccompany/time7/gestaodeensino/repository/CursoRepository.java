package br.com.dbccompany.time7.gestaodeensino.repository;

import br.com.dbccompany.time7.gestaodeensino.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<CursoEntity, Integer> {

    Optional<CursoEntity> findByNomeIgnoreCase(String nome);
}
