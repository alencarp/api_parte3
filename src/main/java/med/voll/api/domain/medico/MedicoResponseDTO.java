package med.voll.api.domain.medico;

public record MedicoResponseDTO(Long id, String nome, String email, String crm, Especialidade especialidade) {

    public MedicoResponseDTO(Medico medico){
        this(medico.getId(), medico.getNome(), medico.getEmail(), medico.getCrm(), medico.getEspecialidade());
    }


}
