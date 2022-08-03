package br.com.dbccompany.time7.gestaodeensino.controller;

import br.com.dbccompany.time7.gestaodeensino.documentation.UsuarioDocumentation;
import br.com.dbccompany.time7.gestaodeensino.dto.relatorios.RelatorioUsuariosDoSistemaDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.usuario.*;
import br.com.dbccompany.time7.gestaodeensino.enums.AtivarDesativarUsuario;
import br.com.dbccompany.time7.gestaodeensino.enums.TipoPessoa;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        return new ResponseEntity<>(usuarioService.login(usuarioLoginDTO), HttpStatus.OK);
    }

    @PostMapping("/cadastro-usuario")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO, @RequestParam @Valid TipoPessoa tipoPessoa) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.saveUsuario(usuarioCreateDTO, tipoPessoa), HttpStatus.OK);
    }

    @GetMapping("/logged")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.getLoggedUser(), HttpStatus.OK);
    }

    @GetMapping("/recuperar-senha/{login}")
    public ResponseEntity<String> recuperarSenha(@PathVariable("login") String login) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.recuperarSenha(login), HttpStatus.OK);
    }

    @GetMapping("/recuperar-senha/valid")
    public ResponseEntity<String> validarTokenRecuperarSenha(@RequestParam("token") String token) throws RegraDeNegocioException {
        return new ResponseEntity<>(usuarioService.validarTokenRecuperarSenha(token), HttpStatus.OK);
    }

    @GetMapping("/listar-usuarios-pessoas")
    public ResponseEntity<List<RelatorioUsuariosDoSistemaDTO>> listarUsuariosDoSistema(@RequestParam TipoPessoa tipoPessoa) {
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
