package gestaoDeEnsino.controller;

import gestaoDeEnsino.dto.endereco.EnderecoCreateDTO;
import gestaoDeEnsino.dto.endereco.EnderecoDTO;
import gestaoDeEnsino.dto.endereco.EnderecoUpdateDTO;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.response.Response;
import gestaoDeEnsino.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RequestMapping("/endereco")
@RestController
@AllArgsConstructor
@Validated
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping("/{idEndereco}")
    @Response
    @Operation(summary = "Listar endereço por ID", description = "Lista um endereço através de seu ID único")
    public ResponseEntity<EnderecoDTO> findByIdPessoa(@PathVariable Integer idEndereco) throws RegraDeNegocioException {
        return new ResponseEntity<>(enderecoService.listById(idEndereco), HttpStatus.OK);
    }

    @PostMapping()
    @Response
    @Operation(summary = "Adicionar endereço", description = "Adiciona um endereço, vinculando-o à uma pessoa existente")
    public ResponseEntity<EnderecoDTO> post(@Valid @RequestBody EnderecoCreateDTO enderecoCreateDTO) {
        return new ResponseEntity<>(enderecoService.save(enderecoCreateDTO), HttpStatus.OK);
    }

    @PutMapping("/{idEndereco}")
    @Response
    @Operation(summary = "Atualizar endereço", description = "Atualiza um endereço, localizando-o por seu ID")
    public ResponseEntity<EnderecoDTO> update(@PathVariable Integer idEndereco, @Valid @RequestBody EnderecoUpdateDTO enderecoUpdateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(enderecoService.update(idEndereco, enderecoUpdateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idEndereco}")
    @Response
    @Operation(summary = "Remover endereço", description = "Remove um endereço, localizando-o por seu ID")
    public void delete(@PathVariable Integer idEndereco) throws RegraDeNegocioException {
        enderecoService.delete(idEndereco);
    }
}