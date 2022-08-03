package gestaoDeEnsino.controller;

import gestaoDeEnsino.documentation.UsuarioDocumentation;
import gestaoDeEnsino.dto.relatorios.RelatorioUsuariosDoSistemaDTO;
import gestaoDeEnsino.dto.usuario.*;
import gestaoDeEnsino.entity.UsuarioEntity;
import gestaoDeEnsino.enums.AtivarDesativarUsuario;
import gestaoDeEnsino.enums.TipoPessoa;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.security.TokenService;
import gestaoDeEnsino.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Validated
public class UsuarioController implements UsuarioDocumentation {
    private final UsuarioService usuarioService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        usuarioLoginDTO.getLogin(),
                        usuarioLoginDTO.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return tokenService.getToken((UsuarioEntity) authentication.getPrincipal());
    }

    @PostMapping("/cadastro-admin")
    public ResponseEntity<UsuarioDTO> createUserAdmin(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveUsuario(usuarioCreateDTO, TipoPessoa.ADMIN), HttpStatus.OK);
    }

    @PostMapping("/cadastro-aluno")
    public ResponseEntity<UsuarioDTO> createUserAluno(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveUsuario(usuarioCreateDTO, TipoPessoa.ALUNO), HttpStatus.OK);
    }

    @PostMapping("/cadastro-professor")
    public ResponseEntity<UsuarioDTO> createUserProfessor(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveUsuario(usuarioCreateDTO, TipoPessoa.PROFESSOR), HttpStatus.OK);
    }

    @GetMapping("/logged")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
    }

    @GetMapping("/recuperar-senha/{login}")
    public ResponseEntity<String> recuperarSenha(@PathVariable ("login") String login) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.recuperarSenha(login), HttpStatus.OK);
    }

    @GetMapping("/recuperar-senha/valid")
    public ResponseEntity<String> validarTokenRecuperarSenha(@RequestParam ("token") String token) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.validarTokenRecuperarSenha(token), HttpStatus.OK);
    }

    @GetMapping("/listar-usuarios-pessoas")
    public ResponseEntity<List<RelatorioUsuariosDoSistemaDTO>> listarUsuariosDoSistema(@RequestParam TipoPessoa tipoPessoa) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.listarUsuariosDoSistema(tipoPessoa), HttpStatus.OK);
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<UsuarioDTO> updateSenha(@RequestBody @Valid UsuarioRecuperarSenhaDTO usuarioRecuperarSenhaDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.updateRecuperarSenha(usuarioRecuperarSenhaDTO), HttpStatus.OK);
    }

    @PutMapping("/ativar-desativar-usuario/{idUsuario}")
    public ResponseEntity<String> ativarDesativarUsuario(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.ativarDesativarUsuario(idUsuario, ativarDesativarUsuario), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.update(usuarioUpdateDTO), HttpStatus.OK);
    }

}
