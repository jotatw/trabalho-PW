package com.br.ifg.luziania.trabalhopw.service;
import com.br.ifg.luziania.trabalhopw.dto.*;import com.br.ifg.luziania.trabalhopw.model.Veiculo;import com.br.ifg.luziania.trabalhopw.repository.*;import jakarta.enterprise.context.ApplicationScoped;import jakarta.inject.Inject;import jakarta.transaction.Transactional;import jakarta.ws.rs.NotFoundException;import java.util.List;
@ApplicationScoped public class VeiculoService {
 @Inject VeiculoRepository repo;@Inject CategoriaVeiculoRepository categorias;
 public List<VeiculoDTO> listar(){return repo.listAll().stream().map(this::dto).toList();}
 @Transactional public VeiculoDTO criar(VeiculoEntradaDTO d){if(repo.count("placa",d.getPlaca().toUpperCase())>0)throw new IllegalArgumentException("Placa já cadastrada.");Veiculo v=new Veiculo();aplicar(v,d);v.setStatus("DISPONIVEL");v.setAtivo(true);repo.persist(v);return dto(v);}
 @Transactional public VeiculoDTO atualizar(Long id,VeiculoEntradaDTO d){Veiculo v=repo.findById(id);if(v==null)throw new NotFoundException();if(repo.count("placa = ?1 and id <> ?2",d.getPlaca().toUpperCase(),id)>0)throw new IllegalArgumentException("Placa já cadastrada.");aplicar(v,d);return dto(v);}
 @Transactional public void desativar(Long id){Veiculo v=repo.findById(id);if(v==null)throw new NotFoundException();if("ALUGADO".equals(v.getStatus()))throw new IllegalArgumentException("Veículo alugado não pode ser desativado.");v.setAtivo(false);v.setStatus("INATIVO");}
 private void aplicar(Veiculo v,VeiculoEntradaDTO d){var cat=categorias.findById(d.getCategoriaId());if(cat==null||!Boolean.TRUE.equals(cat.getAtivo()))throw new IllegalArgumentException("Categoria inválida.");v.setCategoria(cat);v.setMarca(d.getMarca().trim());v.setModelo(d.getModelo().trim());v.setAno(d.getAno());v.setPlaca(d.getPlaca().toUpperCase());v.setCor(d.getCor().trim());v.setQuilometragem(d.getQuilometragem());}
 private VeiculoDTO dto(Veiculo v){return new VeiculoDTO(v.getId(),v.getCategoria().getId(),v.getCategoria().getNome(),v.getMarca(),v.getModelo(),v.getAno(),v.getPlaca(),v.getCor(),v.getQuilometragem(),v.getStatus(),v.getAtivo());}
}
