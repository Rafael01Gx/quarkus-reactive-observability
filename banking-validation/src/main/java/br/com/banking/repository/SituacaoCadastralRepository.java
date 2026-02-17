package br.com.banking.repository;

import br.com.banking.domain.Agencia;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SituacaoCadastralRepository implements PanacheRepository<Agencia> {

    public Uni<Agencia> findByCnpj(String cnpj) {
        return find("cnpj", cnpj).firstResult();
    }
}
