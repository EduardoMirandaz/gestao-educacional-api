package br.com.dbccompany.time7.gestaodeensino.service;

import br.com.dbccompany.time7.gestaodeensino.dto.relatorios.RelatorioUsuariosDoSistemaDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.usuario.*;
import br.com.dbccompany.time7.gestaodeensino.entity.AlunoEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.PessoaEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.ProfessorEntity;
import br.com.dbccompany.time7.gestaodeensino.entity.UsuarioEntity;
import br.com.dbccompany.time7.gestaodeensino.enums.AtivarDesativarUsuario;
import br.com.dbccompany.time7.gestaodeensino.enums.TipoPessoa;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.repository.AlunoRepository;
import br.com.dbccompany.time7.gestaodeensino.repository.ProfessorRepository;
import br.com.dbccompany.time7.gestaodeensino.repository.UsuarioRepository;
import br.com.dbccompany.time7.gestaodeensino.security.TokenAuthenticationFilter;
import br.com.dbccompany.time7.gestaodeensino.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private String expiration;
    @Value("${jwt.email.expiration}")
    private String emailExpiration;
    @Value("${jwt.password.recovery.url}")
    private String passwordRecovery;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final RolesService rolesService;
    private final AuthenticationManager authenticationManager;

    public String login(UsuarioLoginDTO usuarioLoginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        usuarioLoginDTO.getLogin(),
                        usuarioLoginDTO.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return tokenService.getToken((UsuarioEntity) authentication.getPrincipal(), expiration);
    }
    public UsuarioDTO saveUsuario(UsuarioCreateDTO usuarioCreateDTO, TipoPessoa tipoPessoa) throws RegraDeNegocioException {
        verificarSeEmailExiste(usuarioCreateDTO.getLogin());
        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);
        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(tipoPessoa.getDescricao())));
        return entityToDto(usuarioRepository.save(usuarioEntity));
    }

    public UsuarioDTO update(UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
        Integer idUsuario = getIdLoggedUser();
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);

        if (!usuarioEntityRecuperado.getLogin().equals(usuarioUpdateDTO.getLogin())) {
            verificarSeEmailExiste(usuarioUpdateDTO.getLogin());
            usuarioEntityRecuperado.setLogin(usuarioUpdateDTO.getLogin());

            Optional<AlunoEntity> alunoEntityOptional = alunoRepository.findByIdUsuario(idUsuario);
            Optional<ProfessorEntity> professorEntityOptional = professorRepository.findByIdUsuario(idUsuario);
            if (alunoEntityOptional.isPresent()){
                alunoEntityOptional.get().setEmail(usuarioEntityRecuperado.getLogin());
                usuarioEntityRecuperado.setAlunoEntity(alunoEntityOptional.get());
            } else if (professorEntityOptional.isPresent()) {
                professorEntityOptional.get().setEmail(usuarioEntityRecuperado.getLogin());
                usuarioEntityRecuperado.setProfessorEntity(professorEntityOptional.get());
            }
        }
        if (usuarioUpdateDTO.getSenha() != null) {
            usuarioEntityRecuperado.setSenha(usuarioUpdateDTO.getSenha());
            encodePassword(usuarioEntityRecuperado);
        }

        return entityToDto(usuarioRepository.save(usuarioEntityRecuperado));
    }

    public String recuperarSenha(String login) throws RegraDeNegocioException {
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByLogin(login);
        if (usuarioEntity.isPresent()) {
            PessoaEntity pessoaEntity = findPessoaByIdUsuario(usuarioEntity.get().getIdUsuario());

            String token = tokenService.getToken(usuarioEntity.get(), emailExpiration);
            String tokenReplace = token.replace(TokenAuthenticationFilter.BEARER, "");
            String url = passwordRecovery + tokenReplace;

            emailService.sendEmailAlterarSenha(pessoaEntity, url);

            return "Enviado email com instruções para recuperar senha";
        } else {
            return "Usuário não encontrado";
        }
    }

    public UsuarioDTO updateRecuperarSenha(UsuarioRecuperarSenhaDTO usuarioRecuperarSenhaDTO) throws RegraDeNegocioException {
        Integer idUsuario = getIdLoggedUser();
        UsuarioEntity usuarioEntity = findById(idUsuario);
        usuarioEntity.setSenha(usuarioRecuperarSenhaDTO.getSenha());
        encodePassword(usuarioEntity);

        return entityToDto(usuarioRepository.save(usuarioEntity));
    }

    public String ativarDesativarUsuario(Integer idUsuario, AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntityRecuperado = findById(idUsuario);

        if (ativarDesativarUsuario.equals(AtivarDesativarUsuario.ATIVAR)) {
            usuarioEntityRecuperado.setStatus(true);
            usuarioRepository.save(usuarioEntityRecuperado);
            return "Ativado";
        } else {
            usuarioEntityRecuperado.setStatus(false);
            usuarioRepository.save(usuarioEntityRecuperado);
            return "Desativado";
        }
    }

    public List<RelatorioUsuariosDoSistemaDTO> listarUsuariosDoSistema(TipoPessoa tipoPessoa) {
        if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
            return alunoRepository.relatorioAlunosDoSistema();
        } else if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
            return professorRepository.relatorioProfessoresDoSistema();
        } else {
            return usuarioRepository.findAllByAlunoEntityIsNullAndProfessorEntityIsNull().stream()
                    .map(usuarioEntity -> {
                        RelatorioUsuariosDoSistemaDTO relatorioUsuariosDoSistemaDTO = objectMapper.convertValue(usuarioEntity, RelatorioUsuariosDoSistemaDTO.class);
                        relatorioUsuariosDoSistemaDTO.setNomeUsuario(usuarioEntity.getLogin());
                        return relatorioUsuariosDoSistemaDTO;
                    })
                    .toList();
        }
    }

    public UsuarioEntity findById(Integer idUsuario) throws RegraDeNegocioException {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }

    public Optional<UsuarioEntity> findByLogin(String login) {
        return usuarioRepository.findByLogin(login);
    }

    public PessoaEntity findPessoaByIdUsuario(Integer idUsuario) {
        Optional<AlunoEntity> alunoEntityOptional = alunoRepository.findByIdUsuario(idUsuario);
        if (alunoEntityOptional.isPresent()) {
            return alunoEntityOptional.get();
        }

        Optional<ProfessorEntity> professorEntityOptional = professorRepository.findByIdUsuario(idUsuario);

        return professorEntityOptional.orElse(null);
    }

    public String validarTokenRecuperarSenha(String token) throws RegraDeNegocioException {
        try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return TokenAuthenticationFilter.BEARER + token;
        } catch (Exception e) {
            throw new RegraDeNegocioException("Token inválido. Solicite novo link para alterar senha");
        }
    }

    public void encodePassword(UsuarioEntity usuarioEntity) {
        usuarioEntity.setSenha(passwordEncoder.encode(usuarioEntity.getPassword()));
    }

    private void verificarSeEmailExiste(String login) throws RegraDeNegocioException {
        if (findByLogin(login).isPresent()) {
            throw new RegraDeNegocioException("Email já possui cadastro");
        }
    }

    public UsuarioDTO getLoggedUser() throws RegraDeNegocioException {
        Integer idLoggedUser = getIdLoggedUser();
        UsuarioEntity byId = findById(idLoggedUser);
        return entityToDto(byId);
    }

    public Integer getIdLoggedUser() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        return (Integer) principal;
    }

    public UsuarioDTO entityToDto(UsuarioEntity usuarioEntity) {
        return objectMapper.convertValue(usuarioEntity, UsuarioDTO.class);
    }

    public UsuarioEntity createToEntity(UsuarioCreateDTO usuarioCreateDTO) {
        UsuarioEntity usuarioEntity = objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
        usuarioEntity.setStatus(true);
        encodePassword(usuarioEntity);
        return usuarioEntity;
    }

}
