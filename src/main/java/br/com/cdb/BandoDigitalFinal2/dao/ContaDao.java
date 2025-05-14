package br.com.cdb.BandoDigitalFinal2.dao;

import br.com.cdb.BandoDigitalFinal2.entity.ContaEntity;
import br.com.cdb.BandoDigitalFinal2.entity.mapper.ContaEntityRowMapper;
import br.com.cdb.BandoDigitalFinal2.exceptions.ContaNaoEncontradaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ContaDao {

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
    public boolean save(ContaEntity contaEntity)
    {
        String sql = "INSERT INTO contas (tipo, fk_id_cliente, saldo, rendimento, manutencao) " +
                "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                contaEntity.getTipoConta().name(),
                contaEntity.getIdCliente(),
                contaEntity.getSaldo(),
                contaEntity.getRendimento(),
                contaEntity.getManutencao()

        ) > 0;
    }

    //READ
    public ContaEntity findById(Long idConta)
    {
        String sql = "SELECT id_conta, tipo, fk_id_cliente, saldo, rendimento, manutencao FROM contas " +
                "WHERE id_conta = ? ";
        List<ContaEntity> contas = jdbcTemplate.query(sql, new Object[]{idConta}, new ContaEntityRowMapper());
        if(contas.isEmpty())
            throw new ContaNaoEncontradaException ("Conta ID "+idConta+" não encontrada. ");
        return contas.get(0);

    }

    //READ ALL
    public List<ContaEntity> findAll()
    {
        String sql = "SELECT id_conta, tipo, fk_id_cliente, saldo, rendimento, manutencao FROM contas";
        return jdbcTemplate.query(sql, contaRowMapper);
    }

    //UPDATE
    //SO FAZ ALTERACOES EM SALDO, RENDIMENTO E MANUTENCAO, RESTANTE DAS INFORMACOES NAO FAZ SENTIDO
    // PARA A REGRA DE NEGOCIO
    public boolean update(ContaEntity contaEntity)
    {
        String sql = "UPDATE contas SET saldo = ?, rendimento = ?, manutencao = ? WHERE id_conta = ?";
        return jdbcTemplate.update(
                sql,
                contaEntity.getSaldo(),
                contaEntity.getRendimento(),
                contaEntity.getManutencao(),
                contaEntity.getIdConta()
        ) >0;
    }

    //DELETE
    private boolean delete(Long idConta)
    {
        String sql = "DELETE FROM contas WHERE id_conta = ?";
        return jdbcTemplate.update(sql, idConta) > 0;
    }
}
