package br.insper.tabela.tabela;

import br.insper.tabela.partida.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TabelaService {

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private TabelaRepository tabelaRepository;

    public List<Tabela> getTabela() {

        List<Tabela> tabelas = tabelaRepository.findAll();

        return tabelas.stream()
                .sorted(Comparator.comparing(Tabela::getPontos).reversed())
                .collect(Collectors.toList());


    }

}
