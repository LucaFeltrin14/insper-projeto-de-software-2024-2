package br.insper.functional.musicas;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicaRepository extends JpaRepository<Musica, Integer> {
}
