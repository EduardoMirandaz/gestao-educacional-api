package br.com.dbccompany.time7.gestaodeensino.service;

import br.com.dbccompany.time7.gestaodeensino.dto.aluno.AlunoDTO;
import br.com.dbccompany.time7.gestaodeensino.dto.professor.ProfessorDTO;
import br.com.dbccompany.time7.gestaodeensino.entity.PessoaEntity;
import br.com.dbccompany.time7.gestaodeensino.exceptions.RegraDeNegocioException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender emailSender;

    public void sendEmailCriarAluno(AlunoDTO alunoDTO) throws RegraDeNegocioException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(alunoDTO.getEmail());
            mimeMessageHelper.setSubject("Gestão de Ensino - Matrícula realizada");
            mimeMessageHelper.setText(geContentFromTemplateAdicionarContato(alunoDTO), true);

            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
            throw new RegraDeNegocioException("Falha ao enviar email");
        }
    }

    public String geContentFromTemplateAdicionarContato(AlunoDTO alunoDTO) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", alunoDTO.getNome());
        dados.put("email", from);
        dados.put("curso", alunoDTO.getCurso().getNome());
        dados.put("matricula", alunoDTO.getMatricula());

        Template template = fmConfiguration.getTemplate("emailAdicionarAluno-template.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }

    public void sendEmailCriarProfessor(ProfessorDTO professorDTO){
        log.info("Enviando e-mail para " + professorDTO.getNome());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(professorDTO.getEmail());
        message.setSubject("Bem vindo(a)!");
        message.setText("Olá " +professorDTO.getNome()+ ", \n" +
                "Estamos felizes em ter você em nosso sistema :) \n"+
                "Seu cadastro foi realizado com sucesso, seu identificador é " +professorDTO.getIdProfessor()+"\n" +
                "Qualquer dúvida é só contatar o suporte pelo e-mail " + from
                + "\nAtt, Sistema de Gestão de Ensino");
        emailSender.send(message);
        log.info("E-mail enviado");
    }

    public void sendEmailAlterarSenha(PessoaEntity pessoaEntity, String urlAlterarSenha) throws RegraDeNegocioException {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(pessoaEntity.getEmail());
            mimeMessageHelper.setSubject("Gestão de Ensino - Recuperar Senha");
            mimeMessageHelper.setText(geContentFromTemplateAlterarSenha(pessoaEntity, urlAlterarSenha), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
            throw new RegraDeNegocioException("Falha ao enviar email");
        }
    }

    public String geContentFromTemplateAlterarSenha(PessoaEntity pessoaEntity, String urlAlterarSenha) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", pessoaEntity.getNome());
        dados.put("email", from);
        dados.put("url", urlAlterarSenha);
        Template template = fmConfiguration.getTemplate("emailRecuperarSenha.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
    }
}
