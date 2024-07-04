package med.voll.api.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable //tem que colocar esta anotação para o JPA deixar os atributos desta tabela como campos na tabela de medicos.
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Endereco {

    private String logradouro;
    private String bairro;
    private String cep;
    private String cidade;
    private String uf;
    private String numero;
    private String complemento;

    public Endereco(EnderecoRequestDTO enderecoRequestDTO) {
        this.logradouro = enderecoRequestDTO.logradouro();
        this.bairro = enderecoRequestDTO.bairro();
        this.cep = enderecoRequestDTO.cep();
        this.cidade = enderecoRequestDTO.cidade();
        this.uf = enderecoRequestDTO.uf();
        this.numero = enderecoRequestDTO.numero();
        this.complemento = enderecoRequestDTO.complemento();
    }

    public void atualizarInformacoes(EnderecoRequestDTO dados) {
        if (this.logradouro != null) {
            this.logradouro = dados.logradouro();
        }
        if (this.bairro != null) {
            this.bairro = dados.bairro();
        }
        if (this.cep != null) {
            this.cep = dados.cep();
        }
        if (this.cidade != null) {
            this.cidade = dados.cidade();
        }
        if (this.uf != null) {
            this.uf = dados.uf();
        }
        if (this.numero != null) {
            this.numero = dados.numero();
        }
        if (this.complemento != null) {
            this.complemento = dados.complemento();
        }
    }


}
