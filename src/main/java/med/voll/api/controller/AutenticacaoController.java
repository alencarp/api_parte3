package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosAutenticacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;



    // preciso chamar o método AutenticacaoService.loadUserByUsername(String username) que faz a autenticação
    //Porém, no Spring não chamamos direto a classe, mas chamamos a AutenticationManager, que por baixo dos panos vai chamar ela

    //1-converte do nosso dto para o UsernamePasswordAuthenticationToken(que é tipo um dto do Spring)
    //2-Aqui inicia o processo de autenticação. Retorna um objeto que representa o usuario autenticado
    //3-Para usar JWT pegamos no site jwt.io, a Auth0 (biblioteca em Java para gerar tokens em JWT)
    //4-criei o dto DadosTokenJWT para responder com o token gerado
    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        System.out.println("Chegou no efetuar login");
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = manager.authenticate(authenticationToken);
        String tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
