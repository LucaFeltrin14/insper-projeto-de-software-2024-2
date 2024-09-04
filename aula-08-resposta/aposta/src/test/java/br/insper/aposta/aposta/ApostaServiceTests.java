package br.insper.aposta.aposta;

import br.insper.aposta.partida.PartidaNaoEncontradaException;
import br.insper.aposta.partida.PartidaNaoRealizadaException;
import br.insper.aposta.partida.PartidaService;
import br.insper.aposta.partida.RetornarPartidaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ApostaServiceTests {

    @InjectMocks
    ApostaService apostaService;

    @Mock
    ApostaRepository apostaRepository;

    @Mock
    PartidaService partidaService;

    @Test
    public void testListarApostas() {
        Aposta aposta1 = new Aposta();
        Aposta aposta2 = new Aposta();

        ArrayList<Aposta> apostas = new ArrayList<>();
        apostas.add(aposta1);
        apostas.add(aposta2);

        Mockito.when(apostaRepository.findAll())
                .thenReturn(apostas);
        List<Aposta> resultado = apostaService.listar();

        Assertions.assertEquals(2, resultado.size());


    }

    @Test
    public void testSalvarApostaWhenStatusCodeIsSuccessful() {
        Aposta aposta = new Aposta();
        aposta.setIdPartida(1);
        //como eu testo o aposta setID?

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);

        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.salvar(aposta);

        Assertions.assertEquals(apostaRetorno.getStatus(), "REALIZADA");
        Assertions.assertNotNull(apostaRetorno);
        Assertions.assertNotNull(apostaRetorno.getDataAposta());
        Assertions.assertNotNull(apostaRetorno.getId());

    }

    @Test
    public void testSalvarApostaWhenStatusCodeIsNotSuccessful() {
        Aposta aposta = new Aposta();
        aposta.setIdPartida(1);

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.NOT_FOUND);

        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);

        Assertions.assertThrows(PartidaNaoEncontradaException.class, () -> {
            apostaService.salvar(aposta);
        });
    }

    @Test
    public void testGetApostaWhenApostaIsNull() {

        Mockito.when(apostaRepository.findById("1"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ApostaNaoEncontradaException.class,
                () -> apostaService.getAposta("1"));
    }

    @Test
    public void testGetApostaWhenApostaIsNotNullStatusNotRealizada() {

        Aposta aposta = new Aposta();
        aposta.setStatus("GANHOU");

        Mockito.when(apostaRepository.findById("1"))
                .thenReturn(Optional.of(aposta));

        Aposta apostaRetorno = apostaService.getAposta("1");
        Assertions.assertNotNull(apostaRetorno);

    }

    @Test public void testGetApostaWhenApostaIsNotNullStatusPartidaStatusIsNull() {
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.NOT_FOUND);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);

        Assertions.assertThrows(PartidaNaoEncontradaException.class, () -> {
            apostaService.getAposta("1");
        });
    }

    @Test
    public void testGetApostaPartidaIsNotNullPartidaStatusNotRealizada() {
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("AGENDADA");
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);

        Assertions.assertThrows(PartidaNaoRealizadaException.class, () -> {
            apostaService.getAposta("1");
        });

    }

    @Test
    public void testGetApostaPartidaRealizadaApostaEmpate(){
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setResultado("EMPATE");
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("REALIZADA");
        retornarPartidaDTO.setPlacarMandante(1);
        retornarPartidaDTO.setPlacarVisitante(1);
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.getAposta("1");

        Assertions.assertEquals("GANHOU", apostaRetorno.getStatus());
        Assertions.assertNotNull(apostaRetorno);
    }

    @Test
    public void testGetApostaPartidaRealizadaApostaEmpatePeridida(){
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setResultado("EMPATE");
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("REALIZADA");
        retornarPartidaDTO.setPlacarMandante(1);
        retornarPartidaDTO.setPlacarVisitante(2);
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.getAposta("1");

        Assertions.assertEquals("GANHOU", apostaRetorno.getStatus());
        Assertions.assertNotNull(apostaRetorno);
    }

    @Test
    public void testGetApostaPartidaRealizadaApostaVMandante(){
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setResultado("VITORIA_MANDANTE");
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("REALIZADA");
        retornarPartidaDTO.setPlacarMandante(2);
        retornarPartidaDTO.setPlacarVisitante(1);
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.getAposta("1");

        Assertions.assertEquals("GANHOU", apostaRetorno.getStatus());
        Assertions.assertNotNull(apostaRetorno);
    }

    @Test
    public void testGetApostaPartidaRealizadaApostaVMandantePeridida(){
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setResultado("VITORIA_MANDANTE");
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("REALIZADA");
        retornarPartidaDTO.setPlacarMandante(1);
        retornarPartidaDTO.setPlacarVisitante(1);
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.getAposta("1");

        Assertions.assertEquals("PERDEU", apostaRetorno.getStatus());
        Assertions.assertNotNull(apostaRetorno);
    }

    @Test
    public void testGetApostaPartidaRealizadaApostaVVisitante(){
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setResultado("VITORIA_VISITANTE");
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("REALIZADA");
        retornarPartidaDTO.setPlacarMandante(1);
        retornarPartidaDTO.setPlacarVisitante(2);
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.getAposta("1");

        Assertions.assertEquals("PERDEU", apostaRetorno.getStatus());
        Assertions.assertNotNull(apostaRetorno);
    }

    @Test
    public void testGetApostaPartidaRealizadaApostaVVisitantePerdida(){
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setResultado("VITORIA_VISITANTE");
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("REALIZADA");
        retornarPartidaDTO.setPlacarMandante(1);
        retornarPartidaDTO.setPlacarVisitante(1);
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.getAposta("1");

        Assertions.assertEquals("PERDEU", apostaRetorno.getStatus());
        Assertions.assertNotNull(apostaRetorno);
    }

    @Test
    public void testGetApostaPartidaRealizadaApostaPerdida(){
        Aposta aposta = new Aposta();
        aposta.setStatus("REALIZADA");
        aposta.setIdPartida(1);
        aposta.setResultado("");
        aposta.setId("1");

        RetornarPartidaDTO retornarPartidaDTO = new RetornarPartidaDTO();
        retornarPartidaDTO.setStatus("REALIZADA");
        retornarPartidaDTO.setPlacarMandante(1);
        retornarPartidaDTO.setPlacarVisitante(2);
        ResponseEntity<RetornarPartidaDTO> responseEntity = new ResponseEntity<>(retornarPartidaDTO, HttpStatus.OK);


        Mockito.when(apostaRepository.findById("1")).thenReturn(Optional.of(aposta));
        Mockito.when(partidaService.getPartida(1)).thenReturn(responseEntity);
        Mockito.when(apostaRepository.save(aposta)).thenReturn(aposta);

        Aposta apostaRetorno = apostaService.getAposta("1");

        Assertions.assertEquals("PERDEU", apostaRetorno.getStatus());
        Assertions.assertNotNull(apostaRetorno);
    }





}