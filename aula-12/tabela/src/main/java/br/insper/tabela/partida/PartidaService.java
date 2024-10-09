package br.insper.tabela.partida;

import br.insper.loja.partida.dto.RetornarPartidaDTO;
import br.insper.tabela.tabela.Tabela;
import br.insper.tabela.tabela.TabelaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PartidaService {

    @Autowired
    private TabelaRepository tabelaRepository;

    @KafkaListener(topics = "partidas")
    public void getPartidas(RetornarPartidaDTO dto) {

        Tabela mandante = tabelaRepository.findByTime(dto.getNomeMandante());
        Tabela visitante = tabelaRepository.findByTime(dto.getNomeVisitante());

        if (mandante == null) {
            mandante = createTime(dto.getNomeMandante());
        }
        if (visitante == null) {
            visitante = createTime(dto.getNomeVisitante());
        }

        if (dto.getPlacarMandante() > dto.getPlacarVisitante()) {
            mandante.setPontos(mandante.getPontos() + 3);
        }  else if (dto.getPlacarMandante() < dto.getPlacarVisitante()) {
            visitante.setPontos(visitante.getPontos() + 3);
        } else {
            mandante.setPontos(mandante.getPontos() + 1);
            visitante.setPontos(visitante.getPontos() + 1);
        }
        mandante.setGolsPro(mandante.getGolsPro() + dto.getPlacarMandante());
        mandante.setGolsContra(mandante.getGolsContra() + dto.getPlacarVisitante());

        visitante.setGolsPro(visitante.getGolsPro() + dto.getPlacarVisitante());
        visitante.setGolsContra(visitante.getGolsContra() + dto.getPlacarMandante());

        tabelaRepository.save(mandante);
        tabelaRepository.save(visitante);

    }

    public Tabela createTime(String nomeTime) {
        Tabela tabela = new Tabela();
        tabela.setTime(nomeTime);
        tabela.setPontos(0);
        tabela.setGolsContra(0);
        tabela.setGolsPro(0);
        return tabela;
    }


}
