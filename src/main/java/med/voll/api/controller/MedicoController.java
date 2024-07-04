package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    //Quando cadastramos temos que devolver:
    // o código 201,
    // no corpo da resposta, os dados do objeto recém-criado
    // um cabeçalho do protocolo HTTP (Location), que é o endereço para que o front acesse o recurso cadastrado.
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid MedicoRequestDTO medicoRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        Medico medico = new Medico(medicoRequestDTO);
        medicoRepository.save(medico);

        var uri = uriComponentsBuilder.path("medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<MedicoResponseDTO>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        Page page = medicoRepository.findAllByAtivoTrue(paginacao).map(MedicoResponseDTO::new);
        return ResponseEntity.ok(page);
    }

    //1 - Carregar esse médico do BD
    //2 - Sobrescrever os campos de acordo com as novas informações do dto, fazendo um update na sequência
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizaMedico dadosAtualizaMedico){
        Medico medico = medicoRepository.getReferenceById(dadosAtualizaMedico.id()); //este medico está vindo do BD, portanto, está com as infs desatualizadas
        medico.atualizarInformacoes(dadosAtualizaMedico);  //Ex.: pego o nome do médico atual e substituo pelo que está chegando por parâmetro, no dto
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }


    /**
     * @param id
     *
     * Exclusão lógica
     * 1- Carregar a entidade do BD
     * 2- Inativá-la: (Setar o atributo ativo = false)
     * 3- Disparar o update no BD (por causa dos itens 1 e 2 a JPA vai disparar o update automaticamente.
     *                             Portanto, não tem nada para fazer no passo 3)
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        medico.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity detalhar(@PathVariable Long id) {
        Medico medico = medicoRepository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
