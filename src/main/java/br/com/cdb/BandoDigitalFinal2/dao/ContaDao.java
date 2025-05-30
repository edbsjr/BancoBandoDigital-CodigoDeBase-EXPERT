package br.com.cdb.BandoDigitalFinal2.dao;

import br.com.cdb.BandoDigitalFinal2.entity.ContaEntity;
import br.com.cdb.BandoDigitalFinal2.entity.mapper.ContaEntityRowMapper;
import br.com.cdb.BandoDigitalFinal2.exceptions.*;
import br.com.cdb.BandoDigitalFinal2.service.ClienteService;
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
public class ContaDao {

    private static final Logger log = LoggerFactory.getLogger(ContaDao.class);
    private final ContaEntityRowMapper contaRowMapper;
    //Usado para converter o ResultSet e criar um objeto ContaEntity para ser retornado

    private final JdbcTemplate jdbcTemplate;
    //Significa que a variável jdbcTemplate dentro da nossa classe ContaDao sempre apontará para a mesma instância
    // do JdbcTemplate que foi fornecida a ela quando o ContaDao foi criado pelo Spring.

    @Autowired
    public ContaDao(ContaEntityRowMapper contaRowMapper, ContaEntityRowMapper contaRowMapper1, JdbcTemplate jdbcTemplate)
    {
        this.contaRowMapper = contaRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }


    //METODOS CRUD

    //CREAT
    public boolean save(ContaEntity contaEntity) {
        String sql = "SELECT * from public.inserir_conta_v1(?, ?, ?, ?, ?)";
        log.info("Passando os parametros da conta para a insercao");
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    contaEntity.getTipoConta().name(),
                    contaEntity.getIdCliente(),
                    contaEntity.getSaldo(),
                    contaEntity.getRendimento(),
                    contaEntity.getManutencao()

            ));
        } catch (DataAccessException ex){
            log.error("Erro ao inserir a conta na base de dados.", ex);
            throw new RegistroNaoSalvoException("Conta nao salva. ");
        }
    }

    //READ
    public ContaEntity findById(Long idConta)
    {
        String sql = "SELECT * FROM public.busca_conta_por_id_v1(?)";
        log.info("Iniciando busca da conta ID {}", idConta);
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new ContaEntityRowMapper(),
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
    public List<ContaEntity> findAll()
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
    public boolean update(ContaEntity contaEntity)
    {
        String sql = "SELECT * FROM public.atualizar_conta_v1(?,?,?,?,?,?)";
        try{
            log.info("Passando parametros para atualizar conta ID {}",contaEntity.getIdConta());
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    contaEntity.getIdConta(),
                    contaEntity.getTipoConta().name(),
                    contaEntity.getIdCliente(),
                    contaEntity.getSaldo(),
                    contaEntity.getRendimento(),
                    contaEntity.getManutencao()

            ));
        } catch (DataAccessException ex){
            log.error("Erro ao tentar atualizar Conta ID {} na base de dados", contaEntity.getIdConta());
            throw new RegistroNaoAtualizadoException("Conta nao atualizada");
        }
    }

    //DELETE
    private boolean deleteById(Long idConta) {
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
