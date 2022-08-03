package br.com.dbccompany.time7.gestaodeensino.security;

import br.com.dbccompany.time7.gestaodeensino.entity.UsuarioEntity;
import br.com.dbccompany.time7.gestaodeensino.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UsuarioService usuarioService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UsuarioEntity> usuarioOptional = usuarioService.findByLogin(username);
        return usuarioOptional
                .orElseThrow(() -> new UsernameNotFoundException("Usuario inválido"));
    }
}
