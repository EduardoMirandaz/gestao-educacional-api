package br.com.dbccompany.time7.gestaodeensino.service;

import br.com.dbccompany.time7.gestaodeensino.dto.endereco.EnderecoCreateDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.endereco.EnderecoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.endereco.EnderecoUpdateDTO;
import br.com.dbccompany.time7.gestaodeensino.entity.EnderecoEntity;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.repository.AlunoRepository;
import br.com.dbccompany.time7.gestaodeensino.repository.EnderecoRepository;
import br.com.dbccompany.time7.gestaodeensino.repository.ProfessorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final ObjectMapper objectMapper;

    public EnderecoDTO save(EnderecoCreateDTO enderecoCreateDTO) {
        log.info("Adicionando endereços");

        EnderecoDTO enderecoDTO = entityToDTO(enderecoRepository.save(createToEntity(enderecoCreateDTO)));

        log.info("Endereço adicionado");

        return enderecoDTO;
    }

    public EnderecoDTO update(Integer idEndereco, EnderecoUpdateDTO enderecoUpdateDTO) throws RegraDeNegocioException {
        EnderecoEntity enderecoEntityAtualizar = updateToEntity(enderecoUpdateDTO);
        EnderecoEntity enderecoEntityRecuperado = findById(idEndereco);

        log.info("Atualizando endereço");

        enderecoEntityAtualizar.setIdEndereco(idEndereco);
        enderecoEntityAtualizar.setAlunoEntities(enderecoEntityRecuperado.getAlunoEntities());
        enderecoEntityAtualizar.setProfessorEntities(enderecoEntityRecuperado.getProfessorEntities());

        EnderecoDTO enderecoDTO = entityToDTO(enderecoRepository.save(enderecoEntityAtualizar));

        log.info("Endereço atualizado");

        return enderecoDTO;
    }

    public void delete(Integer idEndereco) throws RegraDeNegocioException {
        Integer count = professorRepository.countProfessorEntityByIdEndereco(idEndereco);
        count += alunoRepository.countProfessorEntityByIdEndereco(idEndereco);

        if (count == 0) {
            log.info("Removendo endereço");

            EnderecoEntity enderecoDelete = findById(idEndereco);

            enderecoDelete.setProfessorEntities(enderecoDelete.getProfessorEntities());
            enderecoDelete.setAlunoEntities(enderecoDelete.getAlunoEntities());

            enderecoRepository.delete(enderecoDelete);

            log.info("Endereço removido");
        } else {
            throw new RegraDeNegocioException("Endereço está sendo utilizado");
        }
    }

    public EnderecoDTO listById(Integer idEndereco) throws RegraDeNegocioException {
        log.info("Listando endereço");
        return entityToDTO(findById(idEndereco));
    }

    public EnderecoEntity findById(Integer idEndereco) throws RegraDeNegocioException {
        return enderecoRepository.findById(idEndereco)
                .orElseThrow(() -> new RegraDeNegocioException("Endereço não encontrado"));
    }

    public EnderecoEntity createToEntity(EnderecoCreateDTO enderecoCreateDTO) {
        return objectMapper.convertValue(enderecoCreateDTO, EnderecoEntity.class);
    }

    public EnderecoDTO entityToDTO(EnderecoEntity endereco) {
        return objectMapper.convertValue(endereco, EnderecoDTO.class);
    }

    public EnderecoEntity updateToEntity(EnderecoUpdateDTO enderecoUpdateDTO) {
        return objectMapper.convertValue(enderecoUpdateDTO, EnderecoEntity.class);
    }
}
