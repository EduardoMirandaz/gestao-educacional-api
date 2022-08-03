package br.com.dbccompany.time7.gestaodeensino.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TipoPessoa {

    ALUNO("ROLE_ALUNO"),
    PROFESSOR("ROLE_PROFESSOR"),
    ADMIN("ROLE_ADMIN");

    String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }



}
