package br.com.cdb.BandoDigitalFinal2.dao;

import br.com.cdb.BandoDigitalFinal2.entity.CartaoEntity;
import br.com.cdb.BandoDigitalFinal2.entity.mapper.CartaoEntityRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartaoDao {

    private final CartaoEntityRowMapper cartaoRowMapper;
    //Usado para converter o ResultSet e criar um objeto CartaoEntity para ser retornado

    private final JdbcTemplate jdbcTemplate;
    //Significa que a variável jdbcTemplate dentro da nossa classe CartaoDao sempre apontará para a mesma instância
    // do JdbcTemplate que foi fornecida a ela quando o CartaoDao foi criado pelo Spring.

    @Autowired
    public CartaoDao(CartaoEntityRowMapper cartaoRowMapper, JdbcTemplate jdbcTemplate)
    {
        this.cartaoRowMapper = cartaoRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    //CRIACAO DOS METODOS CRUD (CREAT,READ;UPPDATE,DELETE)
    //CREAT
    public boolean save(CartaoEntity cartaoEntity) {
        String sql = "INSERT INTO cartoes (senha, situacao, fk_id_conta, valor_fatura, limite, limite_usado, tipo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql,
                cartaoEntity.getSenha(),
                cartaoEntity.getSituacao().name(),
                cartaoEntity.getIdConta(),
                cartaoEntity.getValorFatura(),
                cartaoEntity.getLimite(),
                cartaoEntity.getLimiteUsado(),
                cartaoEntity.getTipo().name()
        ) > 0;
    }

    //READ
    public CartaoEntity findById(Long idCartao)
    {
        String sql = "SELECT id_cartao, senha, situacao, tipo, fk_id_conta, valor_fatura, limite, limite_usado" +
                " FROM cartoes WHERE id_cartao = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idCartao}, cartaoRowMapper);

    }

    //READ ALL
    public List<CartaoEntity> findAll()
    {
        String sql = "SELECT id_cartao, senha, situacao, limite, " +
                "FROM cartoes";
        return jdbcTemplate.query(sql,  cartaoRowMapper );
    }


    //UPDATE
    //NAO POSSUI EDICAO DO ID_CONTA, TIPO, FATURA POR NAO FAZER SENTIDO NA REGRA DE NEGOCIO
    public boolean update(CartaoEntity cartaoEntity)
    {
        String sql = "UPDATE cartoes SET senha = ?, situacao = ?, limite= ? WHERE id_cartao = ?";

        return jdbcTemplate.update(
                sql,
                cartaoEntity.getSenha(),
                cartaoEntity.getSituacao().name(),
                cartaoEntity.getLimite(),
                cartaoEntity.getIdCartao()
        ) > 0;
    }

    //DELETE
    public boolean deleteById(Long idCartao)
    {
        String sql = "DELETE FROM cartoes WHERE id_cartao = ?";

        return jdbcTemplate.update(sql, idCartao) > 0;

    }

}
