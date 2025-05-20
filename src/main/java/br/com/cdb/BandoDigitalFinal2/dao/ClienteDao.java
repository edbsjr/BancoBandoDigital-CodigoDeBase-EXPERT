package br.com.cdb.BandoDigitalFinal2.dao;

import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import br.com.cdb.BandoDigitalFinal2.entity.mapper.ClienteRowMapper;
import br.com.cdb.BandoDigitalFinal2.exceptions.ClienteNaoAtualizadoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.ClienteNaoDeletadoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.ClienteNaoEncontradoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.ClienteNaoSalvoException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ClienteDao {

    private static final Logger log = LoggerFactory.getLogger(ClienteDao.class);
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
        String sql = "SELECT * FROM public.inserir_cliente_v1(?,?,?,?)";
                //"INSERT INTO clientes (nome, cpf, data_nasc, categoria) VALUES ( ?, ?, ?, ?)";

        log.info("Passando os parametros para a insercao");

        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    cliente.getNome(),
                    cliente.getDataNasc(),
                    cliente.getCpf(),
                    cliente.getCategoria().name()
            ));
        } catch (DataAccessException ex){
            log.error("Erro ao inserir cliente na base de dados.", ex);
            throw new ClienteNaoSalvoException("Cliente nao salvo. ");
        }
    }

    //READ
        public Cliente findById(Long idCliente)
        {
            String sql = "SELECT id_cliente, nome, cpf, data_nasc, categoria " +
                    "FROM public.busca_cliente_por_id_v1(?)";
            try{
                log.info("Iniciando busca na base de dados");
                return jdbcTemplate.queryForObject(
                        sql,
                        new ClienteRowMapper(),
                        idCliente);
            } catch (EmptyResultDataAccessException ex){
                log.error("Cliente ID "+idCliente+" nao encontrado");
                throw new ClienteNaoEncontradoException("Cliente ID "+idCliente+" nao encontrado ");
            }catch (DataAccessException ex) {
                log.error("Erro ao tentar acessar a base de dados");
                throw new ClienteNaoEncontradoException("Erro ao tentar acessar a base de dados ");
                }
        }

    public List<Cliente> findAll()
    {
        String sql = "SELECT id_cliente, nome, cpf, data_nasc, categoria FROM public.lista_clientes_completa_v1()";
        try {
            return jdbcTemplate.query(sql, clienteRowMapper);
        } catch (DataAccessException ex){
            log.error("Erro ao buscar lista na base de dados");
            throw new ClienteNaoEncontradoException("Erro ao buscar lista na base de dados");
        }
    }

    public boolean update(Cliente cliente)
    {
        String sql = "SELECT * from public.atualizar_cliente_v1(?,?,?,?,?)";
        try {
            log.info("Passando parametros para atualizar cliente ID {}", cliente.getIdCliente());
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    cliente.getIdCliente(),
                    cliente.getNome(),
                    cliente.getDataNasc(),
                    cliente.getCpf(),
                    cliente.getCategoria().name()
            ));
        } catch (DataAccessException ex){
            log.error("Erro ao tentar atualizar a base de dados");
            throw new ClienteNaoAtualizadoException("Cliente nao atualizado");
        }
    }

    //DELETE
    public boolean deleteById(Long idCliente)
    {
        String sql = "SELECT * FROM public.deletar_cliente_v1(?)";
        log.info("Passando parametro para deletar cliente");
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, idCliente));
        } catch (DataAccessException ex){
            log.error("Erro ao tentar deletar na base de dados o cliente ID {}", idCliente);
            throw new ClienteNaoDeletadoException("Erro ao tentar deletar o cliente "+idCliente);
        }
        }
}
