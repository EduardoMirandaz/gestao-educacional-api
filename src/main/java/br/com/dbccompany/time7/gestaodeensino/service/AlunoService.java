package br.com.dbccompany.time7.gestaodeensino.service;

import br.com.dbccompany.time7.gestaodeensino.dto.aluno.AlunoCompletoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.aluno.AlunoCreateDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.aluno.AlunoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.aluno.AlunoUpdateDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.paginacao.PageDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.curso.CursoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.endereco.EnderecoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.relatorios.RelatorioAlunosMaioresNotasDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.usuario.UsuarioDTO;
import br.com.dbccompany.time7.gestaodeensino.entity.*;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import br.com.dbccompany.time7.gestaodeensino.repository.AlunoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;
    private final ObjectMapper objectMapper;
    private final NotaService notaService;
    private final CursoService cursoService;
    private final EnderecoService enderecoService;
    private final EmailService emailService;
    private final UsuarioService usuarioService;

    public AlunoDTO save(AlunoCreateDTO alunoCreateDTO) throws RegraDeNegocioException {
        log.info("Criando aluno...");

        EnderecoEntity enderecoEntity = enderecoService.findById(alunoCreateDTO.getIdEndereco());
        CursoEntity cursoEntity = cursoService.findById(alunoCreateDTO.getIdCurso());
        AlunoEntity alunoEntity = createToEntity(alunoCreateDTO);
        UsuarioDTO usuarioDTO = usuarioService.getLoggedUser();

        alunoEntity.setEnderecoEntity(enderecoEntity);
        alunoEntity.setCursoEntity(cursoEntity);
        alunoEntity.setMatricula(alunoRepository.sequenceMatriculaAluno());
        alunoEntity.setUsuarioEntity(usuarioService.findById(usuarioService.getIdLoggedUser()));
        alunoEntity.setEmail(usuarioDTO.getLogin());

        AlunoDTO alunoDTO = entityToDTO(alunoRepository.save(alunoEntity));
        notaService.adicionarNotasAluno(alunoEntity.getCursoEntity().getIdCurso(), alunoDTO.getIdAluno());

        emailService.sendEmailCriarAluno(alunoDTO);

        log.info("Aluno " + alunoDTO.getNome() + " criado");

        return alunoDTO;
    }

    public AlunoDTO update(AlunoUpdateDTO alunoAtualizar) throws RegraDeNegocioException {
        log.info("Atualizando aluno");

        AlunoEntity alunoEntityRecuperado = findByIdUsuario();
        AlunoEntity alunoEntityAtualizar = updateToEntity(alunoAtualizar);

        if (alunoAtualizar.getNome() == null) {
            alunoEntityAtualizar.setNome(alunoEntityRecuperado.getNome());
        }
        if (alunoAtualizar.getTelefone() == null) {
            alunoEntityAtualizar.setTelefone(alunoEntityRecuperado.getTelefone());
        }
        if (alunoAtualizar.getIdCurso() == null) {
            alunoEntityAtualizar.setCursoEntity(alunoEntityRecuperado.getCursoEntity());
        } else {
            alunoEntityAtualizar.setCursoEntity(cursoService.findById(alunoAtualizar.getIdCurso()));
        }
        if (alunoAtualizar.getIdEndereco() == null) {
            alunoEntityAtualizar.setEnderecoEntity(alunoEntityRecuperado.getEnderecoEntity());
        } else {
            alunoEntityAtualizar.setEnderecoEntity(enderecoService.findById(alunoAtualizar.getIdEndereco()));
        }
        alunoEntityAtualizar.setIdAluno(alunoEntityRecuperado.getIdAluno());
        alunoEntityAtualizar.setMatricula(alunoEntityRecuperado.getMatricula());
        alunoEntityAtualizar.setNotaEntities(alunoEntityRecuperado.getNotaEntities());
        alunoEntityAtualizar.setUsuarioEntity(alunoEntityRecuperado.getUsuarioEntity());

        if (!alunoEntityRecuperado.getCursoEntity().getIdCurso().equals(alunoEntityAtualizar.getCursoEntity().getIdCurso())) {
            notaService.deleteAllNotasByIdAluno(alunoEntityRecuperado.getIdAluno());
            notaService.adicionarNotasAluno(alunoEntityAtualizar.getCursoEntity().getIdCurso(), alunoEntityRecuperado.getIdAluno());
        }

        alunoRepository.save(alunoEntityAtualizar);

        AlunoDTO alunoDTO = entityToDTO(alunoEntityAtualizar);

        log.info(alunoDTO.getNome() + " teve seus dados atualizados");

        return alunoDTO;
    }

    public void delete(Integer idAluno) throws RegraDeNegocioException {
        log.info("Removendo aluno");

            AlunoEntity alunoEntityRecuperado = findById(idAluno);

            alunoEntityRecuperado.setNotaEntities(alunoEntityRecuperado.getNotaEntities());
            alunoEntityRecuperado.setCursoEntity(alunoEntityRecuperado.getCursoEntity());
            alunoEntityRecuperado.setEnderecoEntity(alunoEntityRecuperado.getEnderecoEntity());

            alunoRepository.delete(alunoEntityRecuperado);

            log.info("Aluno removido");
    }

    public List<AlunoDTO> list() throws RegraDeNegocioException {
        log.info("Listando alunos");
        return alunoRepository.findAll().stream()
                .map(this::entityToDTO)
                .toList();
    }

    public AlunoDTO listById(Integer idAluno) throws RegraDeNegocioException {
        log.info("Listando aluno por id");
        return entityToDTO(findById(idAluno));
    }

    public AlunoDTO listByIdUsuario() throws RegraDeNegocioException {
        return entityToDTO(findByIdUsuario());
    }

    private AlunoEntity findByIdUsuario() throws RegraDeNegocioException {
        Integer idLoggedUser = usuarioService.getIdLoggedUser();
        return alunoRepository.findByIdUsuario(idLoggedUser)
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }

    public PageDTO<AlunoDTO> paginatedList(Integer pagina, Integer quantidadeDeRegistros) {
        log.info("Listando alunos com paginação");

        PageRequest pageRequest = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<AlunoEntity> page = alunoRepository.findAll(pageRequest);
        List<AlunoDTO> alunoDTOS = page.getContent().stream()
                .map(this::entityToDTO)
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, alunoDTOS);
    }
    public AlunoEntity createToEntity(AlunoCreateDTO alunoCreateDTO) {
        return objectMapper.convertValue(alunoCreateDTO, AlunoEntity.class);
    }

    public AlunoEntity updateToEntity(AlunoUpdateDTO alunoUpdateDTO) {
        return objectMapper.convertValue(alunoUpdateDTO, AlunoEntity.class);
    }

    public AlunoDTO entityToDTO(AlunoEntity alunoEntity) {
        AlunoDTO alunoDTO = objectMapper.convertValue(alunoEntity, AlunoDTO.class);
        alunoDTO.setEndereco(objectMapper.convertValue(alunoEntity.getEnderecoEntity(), EnderecoDTO.class));
        alunoDTO.setCurso(objectMapper.convertValue(alunoEntity.getCursoEntity(), CursoDTO.class));
        return alunoDTO;
    }

    public AlunoEntity findById(Integer id) throws RegraDeNegocioException {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Aluno não encontrado"));
    }

    public List<RelatorioAlunosMaioresNotasDTO> relatorioAlunoNota() {
        return alunoRepository.relatorioAlunoNota();
    }

    public PageDTO<AlunoCompletoDTO> exibirAlunoCompleto(Integer pagina, Integer quantidadeDeRegistros) {
        log.info("Listando alunos completos");

        Pageable pageable = PageRequest.of(pagina, quantidadeDeRegistros);
        Page<AlunoCompletoDTO> page = alunoRepository.exibirAlunoCompleto(pageable);
        List<AlunoCompletoDTO> alunoCompletoDTOS = page.getContent();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeDeRegistros, alunoCompletoDTOS);
    }
}