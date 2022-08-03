package br.com.dbccompany.time7.gestaodeensino.documentation;

import br.com.dbccompany.time7.gestaodeensino.dto.usuario.*;
import br.com.dbccompany.time7.gestaodeensino.enums.AtivarDesativarUsuario;
import br.com.dbccompany.time7.gestaodeensino.enums.TipoPessoa;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface UsuarioDocumentation {

    @PostMapping("/login")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Login criado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Login inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro ao criar login.")
            }
    )
    @Operation(summary = "Autenticar usuário.", description = "Valida através da geração de um token a existência do par login/senha cadastrado no banco de dados.")
    public ResponseEntity<String> login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO);

    @PostMapping("/cadastro-usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Login administrador criado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Login inválido."),
                    @ApiResponse(responseCode = "500", description = "Erro ao criar login.")
            }
    )
    @Operation(summary = "Cadastrar usuário.", description = "Cadastra no banco de dados uma pessoa com a role de ADMIN, ALUNO OU PROFESSOR.")
    public ResponseEntity<UsuarioDTO> createUser(@RequestBody @Valid UsuarioCreateDTO usuarioCreateDTO, @RequestParam @Valid TipoPessoa tipoPessoa) throws RegraDeNegocioException;

    @GetMapping("/logged")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Verificação de log realizada com sucesso."),
                    @ApiResponse(responseCode = "404", description = "O usuário não foi encontrado."),
                    @ApiResponse(responseCode = "500", description = "Erro ao buscar entidade logada.")
            }
    )
    @Operation(summary = "Recuperar usuario logado.", description = "Recupera do banco o usuario logado.")
    public ResponseEntity<UsuarioDTO> getUsuarioLogado() throws RegraDeNegocioException;

    @GetMapping("/recuperar-senha/{login}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Email enviado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Alguma entidade não foi encontrada."),
                    @ApiResponse(responseCode = "500", description = "Erro ao recuperar senha.")
            }
    )
    @Operation(summary = "Recuperar senha do usuario.", description = "Recupera senha do usuário através de uma confirmação por email.")
    public ResponseEntity<String> recuperarSenha(@PathVariable("login") String login) throws RegraDeNegocioException;

    @GetMapping("/recuperar-senha/valid")
    @Operation(summary = "Verifica validade do token", description = "Verifica validade do token de recuperação de senha, enviado por email")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do usuário alterado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado,"),
                    @ApiResponse(responseCode = "500", description = "Falha ao desativar/ativar usuário.")
            }
    )
    public ResponseEntity<String> validarTokenRecuperarSenha(@RequestParam("token") String token) throws RegraDeNegocioException;

    @PutMapping("/alterar-senha")
    @Operation(summary = "Alterar senha do usuário", description = "Altera senha do usuário logado")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do usuário alterado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado,"),
                    @ApiResponse(responseCode = "500", description = "Falha ao desativar/ativar usuário.")
            }
    )
    public ResponseEntity<UsuarioDTO> updateSenha(@RequestBody @Valid UsuarioRecuperarSenhaDTO usuarioRecuperarSenhaDTO) throws RegraDeNegocioException;

    @PutMapping("/ativar-desativar-usuario/{idUsuario}")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Status do usuário alterado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado,"),
                    @ApiResponse(responseCode = "500", description = "Falha ao desativar/ativar usuário.")
            }
    )
    @Operation(summary = "Ativar/Desativar usuario.", description = "Muda o status de certo usuário no banco de dados para Ativado||Desativado.")
    public ResponseEntity<String> ativarDesativarUsuario(@PathVariable("idUsuario") @Valid Integer idUsuario, @RequestParam AtivarDesativarUsuario ativarDesativarUsuario) throws RegraDeNegocioException;

    @PutMapping("/update")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Não foi possível encontrar este usuário."),
                    @ApiResponse(responseCode = "500", description = "Falha ao atualizar esses dados.")
            }
    )
    @Operation(summary = "Atualizar usuário.", description = "Atualiza o usuário no banco de dados, podendo mudar o login, a senha, ou ambos.")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody @Valid UsuarioUpdateDTO usuarioUpdateDTO) throws RegraDeNegocioException;

}
