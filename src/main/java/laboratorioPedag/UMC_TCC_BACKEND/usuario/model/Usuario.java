package laboratorioPedag.UMC_TCC_BACKEND.usuario.model;


import lombok.Data;
import lombok.NonNull;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
public class Usuario implements Serializable {

    public enum Status{ ACTIVE, INACTIVE }
    public enum Acesso{ COORDENADOR, PROFESSOR, MONITOR }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String nome;
    @Enumerated(EnumType.STRING)
    private Acesso acesso;
    @Enumerated(EnumType.STRING)
    private Status status;
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    private String senha;
    @NotNull
    @Column(unique = true)
    private Long rgm;

}
