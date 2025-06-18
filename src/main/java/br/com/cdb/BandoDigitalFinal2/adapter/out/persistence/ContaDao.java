package br.com.cdb.BandoDigitalFinal2.adapter.out.persistence;

import br.com.cdb.BandoDigitalFinal2.application.port.out.ContaRepositoryPort;
import br.com.cdb.BandoDigitalFinal2.domain.model.Conta;
import br.com.cdb.BandoDigitalFinal2.adapter.out.persistence.mapper.ContaRowMapper;
import br.com.cdb.BandoDigitalFinal2.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class ContaDao implements ContaRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(ContaDao.class);
    private final ContaRowMapper contaRowMapper;
    //Usado para converter o ResultSet e criar um objeto Conta para ser retornado

    private final JdbcTemplate jdbcTemplate;
    //Significa que a variável jdbcTemplate dentro da nossa classe ContaDao sempre apontará para a mesma instância
    // do JdbcTemplate que foi fornecida a ela quando o ContaDao foi criado pelo Spring.

    @Autowired
    public ContaDao(ContaRowMapper contaRowMapper, ContaRowMapper contaRowMapper1, JdbcTemplate jdbcTemplate)
    {
        this.contaRowMapper = contaRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }


    //METODOS CRUD

    //CREAT
    @Override
    public boolean save(Conta conta) {
        String sql = "SELECT * from public.inserir_conta_v1(?, ?, ?, ?, ?)";
        log.info("Passando os parametros da conta para a insercao");
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    conta.getTipoConta().name(),
                    conta.getIdCliente(),
                    conta.getSaldo(),
                    conta.getRendimento(),
                    conta.getManutencao()

            ));
        } catch (DataAccessException ex){
            log.error("Erro ao inserir a conta na base de dados.", ex);
            throw new RegistroNaoSalvoException("Conta nao salva. ");
        }
    }

    //READ
    @Override
    public Conta findById(Long idConta)
    {
        String sql = "SELECT * FROM public.busca_conta_por_id_v1(?)";
        log.info("Iniciando busca da conta ID {}", idConta);
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new ContaRowMapper(),
                    idConta);
        } catch(EmptyResultDataAccessException ex){
            log.error("Cliente ID {} nao encontrado", idConta);
            return null;
        } catch (DataAccessException ex) {
            log.error("Erro ao tentar acessar a base de dados");
            throw new FalhaAoAcessarBaseException("Erro ao tentar acessar a base de dados ");
        }
    }

    //READ ALL
    @Override
    public List<Conta> findAll()
    {
        String sql = "SELECT id_conta, tipo, fk_id_cliente, saldo, rendimento, manutencao " +
                "FROM public.lista_contas_completa_v1()";
        try{
            return jdbcTemplate.query(sql, contaRowMapper);
        } catch (DataAccessException ex) {
            log.error("Erro ao buscar lista na base de dados");
            throw new RegistroNaoEncontradoException("Erro ao buscar lista na base de dados");
        }
    }

    //UPDATE
    //SO FAZ ALTERACOES EM SALDO, RENDIMENTO E MANUTENCAO, RESTANTE DAS INFORMACOES NAO FAZ SENTIDO
    // PARA A REGRA DE NEGOCIO
    @Override
    public boolean update(Conta conta)
    {
        String sql = "SELECT * FROM public.atualizar_conta_v1(?,?,?,?,?,?)";
        try{
            log.info("Passando parametros para atualizar conta ID {}", conta.getIdConta());
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    conta.getIdConta(),
                    conta.getTipoConta().name(),
                    conta.getIdCliente(),
                    conta.getSaldo(),
                    conta.getRendimento(),
                    conta.getManutencao()

            ));
        } catch (DataAccessException ex){
            log.error("Erro ao tentar atualizar Conta ID {} na base de dados", conta.getIdConta());
            throw new RegistroNaoAtualizadoException("Conta nao atualizada");
        }
    }

    //DELETE
    @Override
    public boolean deleteById(Long idConta) {
        String sql = "SELECT * FROM public.deletar_conta_v1(?)";
        try {
            log.info("Passando parametro para deletar conta ID {}",idConta);
            return Boolean.TRUE.equals((jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    idConta)));
        } catch (DataAccessException ex){
            log.error("Erro ao tentar deletar na base de dados a conta ID {}", idConta);
            throw new RegistroNaoDeletadoException("Erro ao tentar deletar a conta ID "+idConta);
        }
    }
}
