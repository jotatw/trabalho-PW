package com.br.ifg.luziania.trabalhopw.service;
import com.br.ifg.luziania.trabalhopw.dto.*; import com.br.ifg.luziania.trabalhopw.model.Cliente; import com.br.ifg.luziania.trabalhopw.repository.ClienteRepository;
import jakarta.enterprise.context.ApplicationScoped; import jakarta.inject.Inject; import jakarta.transaction.Transactional; import jakarta.ws.rs.NotFoundException; import java.util.List;
@ApplicationScoped public class ClienteService {
 @Inject ClienteRepository repo;
 public List<ClienteDTO> listar(){return repo.listAll().stream().map(this::dto).toList();}
 @Transactional public ClienteDTO criar(ClienteEntradaDTO d){if(repo.count("cpf",d.getCpf())>0)throw new IllegalArgumentException("CPF já cadastrado."); Cliente c=new Cliente(); aplicar(c,d);c.setAtivo(true);repo.persist(c);return dto(c);}
 @Transactional public ClienteDTO atualizar(Long id,ClienteEntradaDTO d){Cliente c=repo.findById(id);if(c==null)throw new NotFoundException();if(repo.count("cpf = ?1 and id <> ?2",d.getCpf(),id)>0)throw new IllegalArgumentException("CPF já cadastrado.");aplicar(c,d);return dto(c);}
 @Transactional public void desativar(Long id){Cliente c=repo.findById(id);if(c==null)throw new NotFoundException();c.setAtivo(false);}
 private void aplicar(Cliente c,ClienteEntradaDTO d){c.setNome(d.getNome().trim());c.setCpf(d.getCpf());c.setDataNascimento(d.getDataNascimento());c.setTelefone(d.getTelefone().trim());c.setEmail(d.getEmail());}
 private ClienteDTO dto(Cliente c){return new ClienteDTO(c.getId(),c.getNome(),c.getCpf(),c.getDataNascimento(),c.getTelefone(),c.getEmail(),c.getAtivo());}
}
