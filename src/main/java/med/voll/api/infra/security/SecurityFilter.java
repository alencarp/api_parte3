package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


//No método "doFilterInternal" fazemos a lógica de recuperar o token.
//Mas como o token vai chegar nas requisições? Como o front envia esse token json-jwt?
//Essa questão de enviar um token, é um detalhe de configuração. Por isso não vai no corpo da requisição junto com o json.
//O envio de um token é realizado em um cabeçalho do Protocolo HTTP.
//E tem um cabeçaho específico para enviar o token, que é o cabeçalho Authorization
//O front vai enviar o token no cabeçalho Authorization
//Agora justamente o código deste filter vai ser recuperar esse cabeçalho (e o cabeçalho está associado com o request)



//@Component é usado para que o Spring carregue uma classe/componente genérico automaticamente
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired(required=true)
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    //Este é o método que o Spring vai chamar, qdo este Filtro for executado. Sempre que disparar uma requisição na API, o Spring sabe que tem este Filter, ele vai chamar o Filter (o método doFilterInternal)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);             //Recuperei o token que está vindo dentro do cabeçalho Authorization, na requisição

        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);    //Validando se o token recuperado está correto
            var usuario = usuarioRepository.findByLogin(subject);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);            //necessário para chamar os próxs filtros na aplicação (como não tinha mais nenhum filtro, ele foi direto chamar o controller)
    }




    //Passo para este método a request e ele vai me devolver a String do Token
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
