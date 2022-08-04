package gestaoDeEnsino.service;

import gestaoDeEnsino.dto.relatorios.RelatorioUsuariosDoSistemaDTO;
import gestaoDeEnsino.dto.usuario.UsuarioCreateDTO;
import gestaoDeEnsino.dto.usuario.UsuarioDTO;
import gestaoDeEnsino.dto.usuario.UsuarioRecuperarSenhaDTO;
import gestaoDeEnsino.dto.usuario.UsuarioUpdateDTO;
import gestaoDeEnsino.entity.AlunoEntity;
import gestaoDeEnsino.entity.PessoaEntity;
import gestaoDeEnsino.entity.ProfessorEntity;
import gestaoDeEnsino.entity.UsuarioEntity;
import gestaoDeEnsino.enums.AtivarDesativarUsuario;
import gestaoDeEnsino.enums.TipoPessoa;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.repository.AlunoRepository;
import gestaoDeEnsino.repository.ProfessorRepository;
import gestaoDeEnsino.repository.UsuarioRepository;
import gestaoDeEnsino.security.TokenAuthenticationFilter;
import gestaoDeEnsino.security.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private static final String RECUPERAR_SENHA_URL = "https://gestao-de-ensino-api.herokuapp.com/usuario/recuperar-senha/valid?token=";
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final RolesService rolesService;


    public UsuarioDTO saveUsuario(UsuarioCreateDTO usuarioCreateDTO, TipoPessoa tipoPessoa) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = createToEntity(usuarioCreateDTO);
        usuarioEntity.setRolesEntities(Set.of(rolesService.findByRole(tipoPessoa.toString())));
        return entityToDto(usuarioRepository.save(usuarioEntity));
    }

    public UsuarioDTO update(UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
        Integer idUsuario = getIdLoggedUser();
        UsuarioEntity usuarioEntity = findById(idUsuario);
        if (usuarioUpdateDTO.getLogin() != null) {
            usuarioEntity.setLogin(usuarioUpdateDTO.getLogin());
        }
        if (usuarioUpdateDTO.getSenha() != null) {
            usuarioEntity.setSenha(usuarioUpdateDTO.getSenha());
            encodePassword(usuarioEntity);
        }

        return entityToDto(usuarioRepository.save(usuarioEntity));
    }

    public UsuarioDTO updateRecuperarSenha(UsuarioRecuperarSenhaDTO usuarioRecuperarSenhaDTO) throws RegraDeNegocioException {
        Integer idUsuario = getIdLoggedUser();
        UsuarioEntity usuarioEntity = findById(idUsuario);
        usuarioEntity.setSenha(usuarioRecuperarSenhaDTO.getSenha());
        encodePassword(usuarioEntity);

        return entityToDto(usuarioRepository.save(usuarioEntity));
    }

    public String recuperarSenha(String login) throws RegraDeNegocioException {
        Optional<UsuarioEntity> usuarioEntity = usuarioRepository.findByLogin(login);
        if (usuarioEntity.isPresent()) {
            PessoaEntity pessoaEntity = findPessoaByIdUsuario(usuarioEntity.get().getIdUsuario());

            String token = tokenService.getTokenRecuperarSenha(usuarioEntity.get());
            String tokenReplace = token.replace(TokenAuthenticationFilter.BEARER, "");
            String url = RECUPERAR_SENHA_URL + tokenReplace;

            emailService.sendEmailAlterarSenha(pessoaEntity, url);

            return "Enviado email com instruções para recuperar senha";
        } else {
            return "Usuário não encontrado";
        }
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
        if (tipoPessoa.equals(TipoPessoa.ROLE_ALUNO)) {
            return alunoRepository.relatorioAlunosDoSistema();
        } else if (tipoPessoa.equals(TipoPessoa.ROLE_PROFESSOR)){
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
        if (professorEntityOptional.isPresent()) {
            return professorEntityOptional.get();
        }
        return null;
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
