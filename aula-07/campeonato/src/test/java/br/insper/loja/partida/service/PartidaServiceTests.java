package br.insper.loja.partida.service;

import br.insper.loja.partida.dto.EditarPartidaDTO;
import br.insper.loja.partida.dto.SalvarPartidaDTO;
import br.insper.loja.partida.exception.PartidaNaoEncontradaException;
import br.insper.loja.partida.model.Partida;
import br.insper.loja.partida.repository.PartidaRepository;
import br.insper.loja.time.model.Time;
import br.insper.loja.time.service.TimeService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import br.insper.loja.partida.dto.RetornarPartidaDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class PartidaServiceTests {


    @InjectMocks
    private PartidaService partidaService;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private TimeService timeService;


    // ** CADASTRAR PARTIDA *** //

    @Test
    public void testCadastrarPartidaIsValid() {

        // preparo

        SalvarPartidaDTO salvarPartidaDTO = new SalvarPartidaDTO();
        salvarPartidaDTO.setMandante(1);
        salvarPartidaDTO.setVisitante(2);

        Time timeMandante = new Time();
        timeMandante.setId(1);
        timeMandante.setNome("Time Mandante");

        Time timeVisitante = new Time();
        timeVisitante.setId(2);
        timeVisitante.setNome("Time Visitante");

        Partida partida = new Partida();
        partida.setMandante(timeMandante);
        partida.setVisitante(timeVisitante);
        partida.setStatus("AGENDADA");

        Mockito.when(timeService.getTime(1)).thenReturn(timeMandante);
        Mockito.when(timeService.getTime(2)).thenReturn(timeVisitante);
        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partida);


        // chamada do codigo testado

        RetornarPartidaDTO resultado = partidaService.cadastrarPartida(salvarPartidaDTO);

        // verificacao dos resultados

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("AGENDADA", resultado.getStatus());
        Assertions.assertEquals("Time Mandante", resultado.getNomeMandante());
        Assertions.assertEquals("Time Visitante", resultado.getNomeVisitante());

    }

    @Test
    public void testCadastrarPartidaWhenTimeMandanteIsNull() {

        // preparo

        SalvarPartidaDTO salvarPartidaDTO = new SalvarPartidaDTO();
        salvarPartidaDTO.setMandante(1);
        salvarPartidaDTO.setVisitante(2);

        Time timeVisitante = new Time();
        timeVisitante.setId(2);
        timeVisitante.setNome("Time Visitante");

        Mockito.when(timeService.getTime(1)).thenReturn(null);

        // chamada do codigo testado

        Assertions.assertThrows(RuntimeException.class,
                () -> partidaService.cadastrarPartida(salvarPartidaDTO));

    }

    @Test
    public void testCadastrarPartidaWhenTimeVisitanteIsNull() {

        // preparo

        SalvarPartidaDTO salvarPartidaDTO = new SalvarPartidaDTO();
        salvarPartidaDTO.setMandante(1);
        salvarPartidaDTO.setVisitante(2);

        Mockito.when(timeService.getTime(2)).thenReturn(null);

        // chamada do codigo testado

        Assertions.assertThrows(RuntimeException.class,
                () -> partidaService.cadastrarPartida(salvarPartidaDTO));

    }


    // ** LISTAR PARTIDAS *** //

    @Test
    public void testListarPartidasIsValid() {

        // preparo

        Time timeMandante = new Time();
        timeMandante.setId(1);
        timeMandante.setNome("Time Mandante");
        timeMandante.setIdentificador("time-1");
        timeMandante.setEstado("SP");

        Time timeVisitante = new Time();
        timeVisitante.setId(2);
        timeVisitante.setNome("Time Visitante");
        timeVisitante.setIdentificador("time-2");
        timeVisitante.setEstado("RJ");

        Partida partida = new Partida();
        partida.setMandante(timeMandante);
        partida.setVisitante(timeVisitante);
        partida.setStatus("AGENDADA");

        List<Partida> partidas = new ArrayList<>();
        partidas.add(partida);

        Mockito.when(partidaRepository.findAll()).thenReturn(partidas);

        // chamada do codigo testado

        List<RetornarPartidaDTO> resultado = partidaService.listarPartidas("time-1");

        // verificacao dos resultados

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Time Mandante", resultado.getFirst().getNomeMandante());
        Assertions.assertEquals("Time Visitante", resultado.getFirst().getNomeVisitante());
        Assertions.assertEquals("AGENDADA", resultado.getFirst().getStatus());
    }

    @Test
    public void testListarPartidasWhenMandanteIsNull() {

        // preparo

        Time timeMandante = new Time();
        timeMandante.setId(1);
        timeMandante.setNome("Time Mandante");
        timeMandante.setEstado("SP");

        Time timeVisitante = new Time();
        timeVisitante.setId(2);
        timeVisitante.setNome("Time Visitante");
        timeVisitante.setEstado("RJ");

        Partida partida = new Partida();
        partida.setMandante(timeMandante);
        partida.setVisitante(timeVisitante);
        partida.setStatus("AGENDADA");

        List<Partida> partidas = new ArrayList<>();
        partidas.add(partida);

        Mockito.when(partidaRepository.findAll()).thenReturn(partidas);

        // chamada do codigo testado

        List<RetornarPartidaDTO> resultado = partidaService.listarPartidas(null);

        // verificacao dos resultados

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.size());
        Assertions.assertEquals("Time Mandante", resultado.getFirst().getNomeMandante());
        Assertions.assertEquals("Time Visitante", resultado.getFirst().getNomeVisitante());
        Assertions.assertEquals("AGENDADA", resultado.getFirst().getStatus());

    }

    @Test
    public void testaListarPartidasWhenIdentificadorIsNotMandante() {

        // Preparação

        Time timeMandante = new Time();
        timeMandante.setId(1);
        timeMandante.setNome("Time Mandante");
        timeMandante.setEstado("SP");
        timeMandante.setIdentificador("time-1");

        Time timeVisitante = new Time();
        timeVisitante.setId(2);
        timeVisitante.setNome("Time Visitante");
        timeVisitante.setEstado("RJ");
        timeVisitante.setIdentificador("time-2");

        Partida partida = new Partida();
        partida.setMandante(timeMandante);
        partida.setVisitante(timeVisitante);
        partida.setStatus("AGENDADA");

        List<Partida> partidas = new ArrayList<>();
        partidas.add(partida);

        Mockito.when(partidaRepository.findAll()).thenReturn(partidas);

        // Chamada do código testado

        List<RetornarPartidaDTO> resultado = partidaService.listarPartidas("time-3");

        // Verificação dos resultados

        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.isEmpty(), "A lista de partidas deve estar vazia, pois o identificador não corresponde ao mandante.");
    }


    // ** EDITAR PARTIDA *** //

    @Test
    public void testEditarPartida() {

        // Preparo

        EditarPartidaDTO editarPartidaDTO = new EditarPartidaDTO();
        editarPartidaDTO.setPlacarMandante(2);
        editarPartidaDTO.setPlacarVisitante(1);

        Time timeMandante = new Time();
        timeMandante.setId(1);
        timeMandante.setNome("Time Mandante");
        timeMandante.setEstado("SP");

        Time timeVisitante = new Time();
        timeVisitante.setId(2);
        timeVisitante.setNome("Time Visitante");
        timeVisitante.setEstado("RJ");

        Partida partida = new Partida();
        partida.setId(1);
        partida.setMandante(timeMandante);
        partida.setVisitante(timeVisitante);
        partida.setPlacarMandante(0);
        partida.setPlacarVisitante(0);
        partida.setStatus("AGENDADA");


        Mockito.when(partidaRepository.findById(1)).thenReturn(Optional.of(partida));
        Mockito.when(partidaRepository.save(Mockito.any(Partida.class))).thenReturn(partida);

        // Execução do método a ser testado

        RetornarPartidaDTO resultado = partidaService.editarPartida(editarPartidaDTO, 1);

        // Verificação dos resultados

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.getPlacarMandante());
        Assertions.assertEquals(1, resultado.getPlacarVisitante());
        Assertions.assertEquals("REALIZADA", resultado.getStatus());

        Assertions.assertEquals(2, partida.getPlacarMandante());
        Assertions.assertEquals(1, partida.getPlacarVisitante());
        Assertions.assertEquals("REALIZADA", partida.getStatus());

        Mockito.verify(partidaRepository).findById(1);
        Mockito.verify(partidaRepository).save(partida);
    }


    // ** GET PARTIDA *** //

    @Test
    public void testGetPartidaWhenPartidaIsFound() {

        // Preparação

        Time timeMandante = new Time();
        timeMandante.setId(1);
        timeMandante.setNome("Time Mandante");
        timeMandante.setEstado("SP");

        Time timeVisitante = new Time();
        timeVisitante.setId(2);
        timeVisitante.setNome("Time Visitante");
        timeVisitante.setEstado("RJ");

        Partida partida = new Partida();
        partida.setId(1);
        partida.setMandante(timeMandante);
        partida.setVisitante(timeVisitante);
        partida.setStatus("AGENDADA");



        Mockito.when(partidaRepository.findById(1)).thenReturn(Optional.of(partida));

        // Chamada do código testado

        RetornarPartidaDTO resultado = partidaService.getPartida(1);

        // Verificação dos resultados

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("AGENDADA", resultado.getStatus());
        Assertions.assertEquals(1, resultado.getId());
    }


    @Test
    public void testGetPartidaWhenPartidaIsNull() {

        // Preparo

        Mockito.when(partidaRepository.findById(1)).thenReturn(Optional.empty());

        // Execução do método a ser testado

        Assertions.assertThrows(PartidaNaoEncontradaException.class,
                () -> partidaService.getPartida(1));

        Mockito.verify(partidaRepository).findById(1);
    }

}