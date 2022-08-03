package gestaoDeEnsino.service;

import gestaoDeEnsino.dto.endereco.EnderecoDTO;
import gestaoDeEnsino.dto.paginacao.PageDTO;
import gestaoDeEnsino.dto.professor.ProfessorCreateDTO;
import gestaoDeEnsino.dto.professor.ProfessorDTO;
import gestaoDeEnsino.dto.professor.ProfessorUpdateDTO;
import gestaoDeEnsino.dto.relatorios.RelatorioProfessoresMenoresSalariosDTO;
import gestaoDeEnsino.entity.DisciplinaEntity;
import gestaoDeEnsino.entity.EnderecoEntity;
import gestaoDeEnsino.entity.ProfessorEntity;
import gestaoDeEnsino.exceptions.RegraDeNegocioException;
import gestaoDeEnsino.repository.DisciplinaRepository;
import gestaoDeEnsino.repository.ProfessorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final ObjectMapper objectMapper;
    private final EnderecoService enderecoService;
    private final DisciplinaRepository disciplinaRepository;
    private final EmailService emailService;
    private final UsuarioService usuarioService;


    public ProfessorDTO save(ProfessorCreateDTO professorCreateDTO) throws RegraDeNegocioException {
        log.info("Criando o professor...");

        ProfessorEntity professorEntity = createToEntity(professorCreateDTO);

        if (professorCreateDTO.getIdDisciplina() != null) {
            DisciplinaEntity disciplinaEntityRecuperada = disciplinaRepository.findById(professorCreateDTO.getIdDisciplina())
                    .orElseThrow(() -> new RegraDeNegocioException("Disciplina não encontrada"));
            disciplinaEntityRecuperada.setProfessorEntity(professorEntity);
            professorEntity.setDisciplinaEntities(Set.of(disciplinaEntityRecuperada));
        }
        EnderecoEntity enderecoEntityRecuperado = enderecoService.findById(professorCreateDTO.getIdEndereco());
        professorEntity.setEnderecoEntity(enderecoEntityRecuperado);
        professorEntity.setRegistroTrabalho(professorRepository.sequenceRegistroTrabalho());
        professorEntity.setUsuarioEntity(usuarioService.findById(usuarioService.getIdLoggedUser()));

        ProfessorDTO professorDTO = entityToDTO(professorRepository.save(professorEntity));

        log.info("Professor " + professorDTO.getNome() + " criado!");

        emailService.sendEmailCriarProfessor(professorDTO);

        return professorDTO;
    }

    public ProfessorDTO update(ProfessorUpdateDTO professorAtualizar) throws RegraDeNegocioException {
        log.info("Atualizando o professor...");

        ProfessorEntity professorEntityRecuperado = findByIdUsuario();
        ProfessorEntity professorEntityAtualizar = updateToEntity(professorAtualizar);

        if (professorAtualizar.getNome() == null){
            professorEntityAtualizar.setNome(professorEntityRecuperado.getNome());
        }
        if (professorAtualizar.getTelefone() == null){
            professorEntityAtualizar.setTelefone(professorEntityRecuperado.getTelefone());
        }
        if (professorAtualizar.getEmail() == null){
            professorEntityAtualizar.setEmail(professorEntityRecuperado.getEmail());
        }
        if (professorAtualizar.getCargo() == null){
            professorEntityAtualizar.setCargo(professorEntityRecuperado.getCargo());
        }
        if (professorAtualizar.getSalario() == null){
            professorEntityAtualizar.setSalario(professorEntityRecuperado.getSalario());
        }
        if (professorAtualizar.getIdEndereco() == null) {
            professorEntityAtualizar.setEnderecoEntity(professorEntityRecuperado.getEnderecoEntity());
        } else {
            professorEntityAtualizar.setEnderecoEntity(enderecoService.findById(professorAtualizar.getIdEndereco()));
        }
        professorEntityAtualizar.setIdProfessor(professorEntityRecuperado.getIdProfessor());
        professorEntityAtualizar.setRegistroTrabalho(professorEntityRecuperado.getRegistroTrabalho());
        professorEntityAtualizar.setDisciplinaEntities(professorEntityRecuperado.getDisciplinaEntities());
        professorEntityAtualizar.setUsuarioEntity(professorEntityRecuperado.getUsuarioEntity());

        ProfessorDTO professorDTO = entityToDTO(professorRepository.save(professorEntityAtualizar));

        log.info("Professor atualizado");

        return professorDTO;
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        log.info("Deletando o professor...");

        ProfessorEntity professorRecuperado = findById(id);

        professorRecuperado.getDisciplinaEntities().forEach(disciplinaEntity -> {
            disciplinaEntity.setProfessorEntity(null);
            disciplinaEntity.setNotaEntities(disciplinaEntity.getNotaEntities());
            disciplinaEntity.setCursosEntities(disciplinaEntity.getCursosEntities());
            disciplinaRepository.save(disciplinaEntity);
        });
        professorRepository.delete(professorRecuperado);
        enderecoService.delete(professorRecuperado.getEnderecoEntity().getIdEndereco());

        log.info(professorRecuperado.getNome() + " removido do banco de dados");
    }

    public List<ProfessorDTO> list() throws RegraDeNegocioException {
        log.info("Listando todos professores");

        return professorRepository.findAll().stream()
                .map(this::entityToDTO)
                .toList();
    }

    public ProfessorDTO listByIdUsuario() throws RegraDeNegocioException {
        return entityToDTO(findByIdUsuario());
    }
    public ProfessorDTO listById(Integer idProfessor) throws RegraDeNegocioException {
        log.info("Listando professor por id");

        return entityToDTO(findById(idProfessor));
    }

    public List<ProfessorDTO> listByName(String nomeProfessor) {
        log.info("Listando professor " + nomeProfessor);

        return findByName(nomeProfessor).stream()
                .map(this::entityToDTO)
                .toList();
    }

    public PageDTO<ProfessorDTO> paginatedList(Integer pagina, Integer quantidadeDeRegistros) {
        log.info("Listando professores com paginação");

        PageRequest pageRequest = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<ProfessorEntity> page = professorRepository.findAll(pageRequest);
        List<ProfessorDTO> alunoDTOS = page.getContent().stream()
                .map(this::entityToDTO)
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, alunoDTOS);
    }

    public ProfessorEntity findById(Integer idProfessor) throws RegraDeNegocioException {
        return professorRepository.findById(idProfessor)
                .orElseThrow(() -> new RegraDeNegocioException("Professor não encontrado"));
    }

    private ProfessorEntity findByIdUsuario() throws RegraDeNegocioException {
        return professorRepository.findByIdUsuario(usuarioService.getIdLoggedUser())
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }

    public List<ProfessorEntity> findByName(String nome) {
        return professorRepository.findAllByNomeContainingIgnoreCase(nome);
    }

    public ProfessorEntity updateToEntity(ProfessorUpdateDTO professorUpdateDTO) {
        return objectMapper.convertValue(professorUpdateDTO, ProfessorEntity.class);
    }

    public ProfessorEntity createToEntity(ProfessorCreateDTO professorCreateDTO) {
        return objectMapper.convertValue(professorCreateDTO, ProfessorEntity.class);
    }

    public EnderecoDTO enderecoToEnderecoDTO(EnderecoEntity endereco) {
        return objectMapper.convertValue(endereco, EnderecoDTO.class);
    }

    public ProfessorDTO entityToDTO(ProfessorEntity professorEntity) {
        ProfessorDTO professorDTO = objectMapper.convertValue(professorEntity, ProfessorDTO.class);
        professorDTO.setEndereco(enderecoToEnderecoDTO(professorEntity.getEnderecoEntity()));

        return professorDTO;
    }

    public List<RelatorioProfessoresMenoresSalariosDTO> relatorioProfessorSalario() {
        return professorRepository.relatorioProfessorSalario();
    }

}
