package gestaoDeEnsino.repository;

import gestaoDeEnsino.dto.relatorios.RelatorioProfessoresMenoresSalariosDTO;
import gestaoDeEnsino.dto.relatorios.RelatorioUsuariosDoSistemaDTO;
import gestaoDeEnsino.entity.ProfessorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<ProfessorEntity, Integer> {

    List<ProfessorEntity> findAllByNomeContainingIgnoreCase(String nome);

    @Query("select count (p)" +
            " from professor p" +
            " where p.enderecoEntity.idEndereco = :idEndereco")
    Integer countProfessorEntityByIdEndereco(@Param("idEndereco") Integer idEndereco);

    @Query(value = "select new gestaoDeEnsino.dto.relatorios.RelatorioProfessoresMenoresSalariosDTO (" +
            "p.registroTrabalho, " +
            "p.nome, " +
            "p.salario " +
            ") " +
            "from professor p " +
            "ORDER BY p.salario DESC, p.nome")
    List<RelatorioProfessoresMenoresSalariosDTO> relatorioProfessorSalario();

    @Query(value = "SELECT nextval('seq_registro_trabalho')", nativeQuery = true)
    Integer sequenceRegistroTrabalho();

    Optional<ProfessorEntity> findByIdUsuario(Integer idUsuario);

    @Query("""
            select new gestaoDeEnsino.dto.relatorios.RelatorioUsuariosDoSistemaDTO(
            u.idUsuario,
            p.nome,
            u.login,
            u.status
            )
            from professor p
            left join p.usuarioEntity u
            """
    )
    List<RelatorioUsuariosDoSistemaDTO> relatorioProfessoresDoSistema();
}
