package br.com.dbccompany.time7.gestaodeensino.service;

import br.com.dbccompany.time7.gestaodeensino.entity.RolesEntity;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RolesService {
    private final RolesRepository rolesRepository;

    public RolesEntity findByRole(String role) throws RegraDeNegocioException {
        return rolesRepository.findByRoles(role).orElseThrow(() -> new RegraDeNegocioException("Role não encontrada"));
    }
}
