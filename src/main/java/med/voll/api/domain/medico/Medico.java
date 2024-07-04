package med.voll.api.domain.medico;

import jakarta.persistence.*;
import lombok.*;
import med.voll.api.domain.endereco.Endereco;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@ToString
public class Medico {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @Embedded //EMBEDDABLE ATTRIBUTE da JPA => fica em uma classe separada, mas no BD ele considera que os campos da classe Endereco fazem parte da mesma tabela de medicos
    private Endereco endereco;

    private Boolean ativo;

    public Medico(MedicoRequestDTO medicoRequestDTO) {
        this.ativo = true;
        this.nome = medicoRequestDTO.nome();
        this.email = medicoRequestDTO.email();
        this.telefone = medicoRequestDTO.telefone();
        this.crm = medicoRequestDTO.crm();
        this.especialidade = medicoRequestDTO.especialidade();
        this.endereco = new Endereco(medicoRequestDTO.endereco());
    }


    //Ex.: pego o nome do médico atual e substituo pelo que está chegando por parâmetro, no dto
    public void atualizarInformacoes(DadosAtualizaMedico dadosAtualizaMedico) {
        if (dadosAtualizaMedico.nome() != null) {
            this.nome = dadosAtualizaMedico.nome();
        }
        if (dadosAtualizaMedico.telefone() != null) {
            this.telefone = dadosAtualizaMedico.telefone();
        }
        if (dadosAtualizaMedico.endereco() != null) {
            this.endereco.atualizarInformacoes(dadosAtualizaMedico.endereco());
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
