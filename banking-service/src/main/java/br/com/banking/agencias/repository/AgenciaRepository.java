package br.com.banking.agencias.repository;

import br.com.banking.agencias.domain.Agencia;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AgenciaRepository implements PanacheRepository<Agencia> {

    @WithSession
    public Uni<Agencia> findByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResult();
    }
}
