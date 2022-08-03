package gestaoDeEnsino.service;

import gestaoDeEnsino.entity.RolesEntity;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.repository.RolesRepository;
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
