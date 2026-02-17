package com.alura.agencias.services;


import br.com.banking.agencias.domain.Agencia;
import br.com.banking.agencias.domain.Endereco;
import br.com.banking.agencias.domain.http.AgenciaHttp;
import br.com.banking.agencias.exception.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.banking.agencias.repository.AgenciaRepository;
import br.com.banking.agencias.service.AgenciaService;
import br.com.banking.agencias.service.http.SituacaoCadastralHttpService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class AgenciaServiceTest {

    @InjectMock
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    @InjectMock
    private AgenciaRepository agenciaRepository;

    @Inject
    private AgenciaService agenciaService;

    @Test
    public void deveNaoCadastrarQuandoClientRetornarNull() {
        Agencia agencia = criarAgencia();

        Vertx.vertx().runOnContext(r -> {
            Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(Uni.createFrom().nullItem());

            Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));

            Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
        });
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtivo() {
        Agencia agencia = criarAgencia();

        Vertx.vertx().runOnContext(r -> {
            Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(criarAgenciaHttp());

            agenciaService.cadastrar(agencia);

            Mockito.verify(agenciaRepository).persist(agencia);
        });
    }

    private Agencia criarAgencia() {
        Endereco endereco = new Endereco(1L, "Rua de teste", "Logradouro de teste", "Complemento de teste", 1);
        return new Agencia(1L, "Agencia Teste", "Razao social da Agencia Teste", "123", endereco);
    }

    private Uni<AgenciaHttp> criarAgenciaHttp() {
        return Uni.createFrom().item(new AgenciaHttp("Agencia Teste", "Razao social da Agencia Teste", "123", "ATIVO"));
    }
}
