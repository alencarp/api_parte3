package med.voll.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //é o método que vai fazer a consulta da tabela no BD.
    UserDetails findByLogin(String login);
}
