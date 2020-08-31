package br.com.inteligente.security

import br.com.inteligente.funcionario.Funcionario
import br.com.inteligente.funcionario.FuncionarioService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class FuncionarioDetailsService(val funcionarioService: FuncionarioService): UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        if(Objects.nonNull(username)){
            val funcionario: Funcionario? = funcionarioService.buscarPorEmail(username!!)
            if(Objects.nonNull(funcionario)){
                return FuncionarioPrincipal(funcionario!!)
            }
        }

        throw UsernameNotFoundException(username)
    }
}