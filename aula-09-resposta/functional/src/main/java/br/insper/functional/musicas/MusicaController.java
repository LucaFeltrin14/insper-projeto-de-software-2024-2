package br.insper.functional.musicas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/musica")
public class MusicaController {

    @Autowired
    private MusicaService musicaService;

    @GetMapping
    public List<MusicaDTO> getMusicas(@RequestParam(required = false) String nome,
                                      @RequestParam(required = false) String artista) {
        return musicaService.getAllMusicas(nome, artista);
    }

    // 1 - Refator o método getAllMusicas e adicionar filtros de artista e nome
    // 2 - Descobrir o total de reproducoes de todas as musicas
    // 3 - Agrupar artista e somar a quantidade de reproducoes
    // 4 - Agrupar artista e ver a duracao media das musicas
    // 5 - Contar quantas músicas existem no total
    // 6 - No exercício anterior, adicionar um filtro pelo nome do artista
    // 7 - Encontrar a música com maior duração
    // 8 - Encontrar a música com mais execuções
    // 9 - Adicionar a rota para buscar uma musica pelo id, se ela nao existir retornar uma RntimeException



}
