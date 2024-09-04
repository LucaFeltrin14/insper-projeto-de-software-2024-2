package br.insper.functional.musicas;

public class MusicaDTO {
    private Integer id;
    private String nome;
    private String artista;
    private Double tamanhoMinutos;
    private Integer reproducoes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public Double getTamanhoMinutos() {
        return tamanhoMinutos;
    }

    public void setTamanhoMinutos(Double tamanhoMinutos) {
        this.tamanhoMinutos = tamanhoMinutos;
    }

    public Integer getReproducoes() {
        return reproducoes;
    }

    public void setReproducoes(Integer reproducoes) {
        this.reproducoes = reproducoes;
    }

    public static MusicaDTO convert(Musica musica) {
        MusicaDTO musicaDTO = new MusicaDTO();
        musicaDTO.setId(musica.getId());
        musicaDTO.setArtista(musica.getArtista());
        musicaDTO.setTamanhoMinutos(musica.getTamanhoMinutos());
        musicaDTO.setNome(musica.getNome());
        musicaDTO.setReproducoes(musica.getReproducoes());
        return musicaDTO;
    }
}
