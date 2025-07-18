package com.cooperfilme.utils;

import com.cooperfilme.domain.enums.StatusRoteiro;

import java.util.List;
import java.util.Map;

public class StatusTransicaoValidator {

    private static final Map<StatusRoteiro, List<StatusRoteiro>> transicoesValidas = Map.of(
            StatusRoteiro.AGUARDANDO_ANALISE, List.of(StatusRoteiro.EM_ANALISE),
            StatusRoteiro.EM_ANALISE, List.of(StatusRoteiro.AGUARDANDO_REVISAO, StatusRoteiro.RECUSADO),
            StatusRoteiro.AGUARDANDO_REVISAO, List.of(StatusRoteiro.EM_REVISAO),
            StatusRoteiro.EM_REVISAO, List.of(StatusRoteiro.AGUARDANDO_APROVACAO),
            StatusRoteiro.AGUARDANDO_APROVACAO, List.of(StatusRoteiro.EM_APROVACAO, StatusRoteiro.RECUSADO),
            StatusRoteiro.EM_APROVACAO, List.of(StatusRoteiro.APROVADO, StatusRoteiro.RECUSADO)
    );

    public static boolean isTransicaoValida(StatusRoteiro atual, StatusRoteiro novo) {
        return transicoesValidas
                .getOrDefault(atual, List.of())
                .contains(novo);
    }
}

