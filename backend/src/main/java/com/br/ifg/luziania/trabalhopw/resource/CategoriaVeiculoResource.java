package com.br.ifg.luziania.trabalhopw.resource;
import com.br.ifg.luziania.trabalhopw.dto.PerfilDTO;import com.br.ifg.luziania.trabalhopw.repository.CategoriaVeiculoRepository;import jakarta.annotation.security.RolesAllowed;import jakarta.inject.Inject;import jakarta.ws.rs.*;import jakarta.ws.rs.core.MediaType;import java.util.List;
@Path("/categorias-veiculos") @Produces(MediaType.APPLICATION_JSON) @RolesAllowed({"ADMIN","ATENDENTE"})
public class CategoriaVeiculoResource {@Inject CategoriaVeiculoRepository repo;@GET public List<CategoriaDTO> listar(){return repo.list("ativo",true).stream().map(c->new CategoriaDTO(c.getId(),c.getNome(),c.getValorDiaria())).toList();} public record CategoriaDTO(Long id,String nome,java.math.BigDecimal valorDiaria) {}}
