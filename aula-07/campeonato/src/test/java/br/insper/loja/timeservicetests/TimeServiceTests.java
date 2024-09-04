package br.insper.loja.timeservicetests;

import br.insper.loja.time.exception.TimeNaoEncontradoException;
import br.insper.loja.time.model.Time;
import br.insper.loja.time.repository.TimeRepository;
import br.insper.loja.time.service.TimeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TimeServiceTests {


    @InjectMocks
    private TimeService timeService;

    @Mock
    private TimeRepository timeRepository;


    // ** LISTAR-TIMES **

    @Test
    public void testListarTimesWhenEstadoIsNull() {

        // preparacao
        Mockito.when(timeRepository.findAll()).thenReturn(new ArrayList<>());

        // chamada do codigo testado
        List<Time> times = timeService.listarTimes(null);

        // verificacao dos resultados
        Assertions.assertTrue(times.isEmpty());
    }

    @Test
    public void testListarTimesWhenEstadoIsNotNull() {

        // preparacao
        List<Time> lista = new ArrayList<>();

        Time time = new Time();
        time.setEstado("SP");
        time.setIdentificador("time-1");
        lista.add(time);

        Mockito.when(timeRepository.findByEstado(Mockito.anyString())).thenReturn(lista);

        // chamada do codigo testado
        List<Time> times = timeService.listarTimes("SP");

        // verificacao dos resultados
        Assertions.assertTrue(times.size() == 1);
        Assertions.assertEquals("SP", times.getFirst().getEstado());
        Assertions.assertEquals("time-1", times.getFirst().getIdentificador());
    }

    // ****


    // ** GET-TIME **

    @Test
    public void testGetTimeWhenTimeIsNotNull() {

        Time time = new Time();
        time.setEstado("SP");
        time.setIdentificador("time-1");

        Mockito.when(timeRepository.findById(1)).thenReturn(Optional.of(time));

        Time timeRetorno = timeService.getTime(1);

        Assertions.assertNotNull(timeRetorno);
        Assertions.assertEquals("SP", timeRetorno.getEstado());
        Assertions.assertEquals("time-1", timeRetorno.getIdentificador());

    }

    @Test
    public void testGetTimeWhenTimeIsNull() {

        Mockito.when(timeRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(TimeNaoEncontradoException.class,
                () -> timeService.getTime(1));

    }

    // ****


    // ** CADASTRAR-TIME **

    @Test
    public void testCadastraTimeWhenTimeNomeIsEmpty() {

        Time time = new Time();

        time.setNome("");
        time.setIdentificador("time-1");

        Assertions.assertThrows(RuntimeException.class,
                () -> timeService.cadastrarTime(time));

    }

    @Test
    public void testCadastraTimeWhenTimeIdentificadorIsEmpty() {

        Time time = new Time();

        time.setNome("SP");
        time.setIdentificador("");

        Assertions.assertThrows(RuntimeException.class,
                () -> timeService.cadastrarTime(time));

    }

    @Test
    public void testCadastraTimeWhenTimeIsValid() {

        Time time = new Time();

        time.setNome("SP");
        time.setIdentificador("time-1");

        Mockito.when(timeRepository.save(time)).thenReturn(time);

        Time timeRetorno = timeService.cadastrarTime(time);

        Assertions.assertNotNull(timeRetorno);
        Assertions.assertEquals("SP", timeRetorno.getNome());
        Assertions.assertEquals("time-1", timeRetorno.getIdentificador());

    }

    // ****
}