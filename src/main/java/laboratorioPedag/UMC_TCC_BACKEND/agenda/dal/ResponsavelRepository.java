package laboratorioPedag.UMC_TCC_BACKEND.agenda.dal;

import laboratorioPedag.UMC_TCC_BACKEND.agenda.model.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {

    Responsavel findByEmail(String email);
    Responsavel findByNome(String nome);
}
