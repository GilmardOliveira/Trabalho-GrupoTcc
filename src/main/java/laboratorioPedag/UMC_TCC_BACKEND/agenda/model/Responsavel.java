package laboratorioPedag.UMC_TCC_BACKEND.agenda.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
public class Responsavel implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotNull
    @Column(unique = true)
    private String rg;
    private String nome;
    @NotNull
    @Column(unique = true)
    private String email;
}
