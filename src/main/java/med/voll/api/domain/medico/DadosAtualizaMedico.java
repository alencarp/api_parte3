package med.voll.api.domain.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.endereco.EnderecoRequestDTO;

public record DadosAtualizaMedico(
        @NotNull
        Long id,
        String nome,
        String telefone,
        EnderecoRequestDTO endereco) {
}
