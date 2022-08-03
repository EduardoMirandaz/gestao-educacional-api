package br.com.dbccompany.time7.gestaodeensino.repository;

import br.com.dbccompany.time7.gestaodeensino.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    Optional<UsuarioEntity> findByLoginAndSenha(String login, String senha);
    Optional<UsuarioEntity> findByLogin(String login);
    List<UsuarioEntity> findAllByAlunoEntityIsNullAndProfessorEntityIsNull();
}
