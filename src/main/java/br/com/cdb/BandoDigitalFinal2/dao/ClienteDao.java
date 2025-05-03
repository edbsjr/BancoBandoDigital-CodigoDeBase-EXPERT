package br.com.cdb.BandoDigitalFinal2.dao;

import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.entity.mapper.ClienteRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClienteDao {

    private ClienteRowMapper clienteRowMapper;

    private final JdbcTemplate jdbcTemplate;

    public ClienteDao(ClienteRowMapper clienteRowMapper, JdbcTemplate jdbcTemplate) {
        this.clienteRowMapper = clienteRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    //METODOS CRUD

    //CREAT
    public boolean save(Cliente cliente)
    {
        String sql = "INSERT INTO clientes (nome, cpf, data_nasc, categoria) VALUES ( ?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDataNasc(),
                cliente.getCategoria().name()
        ) > 0;
    }

    //READ
    public Cliente findById(Long idCliente)
    {
        String sql = "SELECT id_cliente, nome, cpf, data_nasc, categoria FROM clientes WHERE id_cliente = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{idCliente}, clienteRowMapper);
    }

    public List<Cliente> findAll()
    {
        String sql = "SELECT id_cliente, nome, cpf, data_nasc, categoria FROM clientes";
        return jdbcTemplate.query(sql, clienteRowMapper);
    }

    public boolean update(Cliente cliente)
    {
        String sql = "UPDATE clientes SET nome = ?, cpf = ?, data_nasc = ?, categoria = ? WHERE id_cliente = ?";
        return jdbcTemplate.update(
                sql,
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDataNasc(),
                cliente.getCategoria().name(),
                cliente.getIdCliente()
        ) > 0;
    }

    //DELETE
    public boolean deleteById(Long idCliente)
    {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        return jdbcTemplate.update(sql, idCliente) > 0;
    }
}
