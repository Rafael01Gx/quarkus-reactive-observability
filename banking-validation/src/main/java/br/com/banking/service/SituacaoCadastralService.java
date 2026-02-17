package br.com.banking.service;

import br.com.banking.domain.Agencia;
import br.com.banking.messaging.InativarAgenciaProducer;
import br.com.banking.repository.SituacaoCadastralRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SituacaoCadastralService {

    private final SituacaoCadastralRepository situacaoCadastralRepository;
    private final InativarAgenciaProducer producer;

    public SituacaoCadastralService(SituacaoCadastralRepository situacaoCadastralRepository, InativarAgenciaProducer producer) {
        this.situacaoCadastralRepository = situacaoCadastralRepository;
        this.producer = producer;
    }

    @WithTransaction
    public Uni<Void> alterar(Agencia agencia) {
        return situacaoCadastralRepository
                .update("situacaoCadastral = ?1 where cnpj = ?2",
                        agencia.getSituacaoCadastral(), agencia.getCnpj())
                .call(() -> producer.enviarMensagemSmallRyeMutinyEmitter(agencia))
                .call(() -> producer.enviarMensagemKafkaConfiguration(agencia))
                .replaceWithVoid();
    }
}
