package laboratorioPedag.UMC_TCC_BACKEND.agenda.model;

import ch.qos.logback.core.status.Status;
import laboratorioPedag.UMC_TCC_BACKEND.material.model.Material;
import laboratorioPedag.UMC_TCC_BACKEND.usuario.model.Usuario;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Ensino{
        FUNDAMENTAL,
        PRIMARIO;
    }

    public enum Status{
        ACTIVE,
        INACTIVE
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne
    private Usuario coordenator;
    @OneToOne
    private Usuario monitor;
    @OneToOne
    private Usuario professor;

    private String escola;
    private Long criancas;
    @Enumerated(EnumType.STRING)
    private Ensino tipoEnsino;
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany
    private List<Responsavel> resposaveis;

    @ManyToMany
    private List<Material> materiais;

    private Long data;
    private String descricao;

}
