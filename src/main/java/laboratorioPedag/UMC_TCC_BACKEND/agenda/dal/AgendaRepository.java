package laboratorioPedag.UMC_TCC_BACKEND.agenda.dal;

import laboratorioPedag.UMC_TCC_BACKEND.agenda.model.Agenda;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    @Query(value = "select a.* from agenda a where a.data between :startDate and :endDate", nativeQuery = true)
    List<Agenda> findAllByDateRange(@Param("startDate") Long startDate, @Param("endDate") Long endDate);
    Agenda findByData(Long data);
    List<Agenda> findByProfessor(Usuario professor);
    List<Agenda> findAllByStatus(Agenda.Status status);
}
