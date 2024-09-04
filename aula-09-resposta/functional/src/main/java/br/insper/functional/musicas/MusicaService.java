package br.insper.functional.musicas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MusicaService {

    @Autowired
    private MusicaRepository musicaRepository;
    public List<MusicaDTO> getAllMusicas(String nome, String artista) {
        List<Musica> musicas = musicaRepository.findAll();
        Stream<Musica> stream = musicas
                .stream();

            if (nome != null) {
                stream = stream.filter(m -> m.getNome().contains(nome));
            }

            if (artista != null) {
                stream = stream.filter(m -> m.getArtista().contains(artista));
            }

        return stream
                .map(m -> MusicaDTO.convert(m))
                .toList();
       // List<MusicaDTO> dtos = new ArrayList<>();
       // for (Musica musica : musicas) {
       //     dtos.add(MusicaDTO.convert(musica));
       // }
       // return dtos;
    }


}
