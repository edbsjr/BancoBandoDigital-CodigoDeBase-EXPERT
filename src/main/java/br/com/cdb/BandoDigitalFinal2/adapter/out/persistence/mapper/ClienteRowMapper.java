package br.com.cdb.BandoDigitalFinal2.adapter.out.persistence.mapper;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;
import br.com.cdb.BandoDigitalFinal2.enums.Categoria;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ClienteRowMapper implements RowMapper<Cliente> {

    @Override
    public Cliente mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cliente cliente = new Cliente();

        cliente.setIdCliente(rs.getLong("id_cliente"));
        cliente.setNome(rs.getString("nome"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setDataNasc(rs.getDate("data_nasc").toLocalDate());
        cliente.setCategoria(Categoria.valueOf(rs.getString("categoria")));

        return cliente;
    }


}
