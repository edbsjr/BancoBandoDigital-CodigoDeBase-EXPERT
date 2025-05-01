package br.com.cdb.BandoDigitalFinal2.dao;

import br.com.cdb.BandoDigitalFinal2.entity.CartaoEntity;
import br.com.cdb.BandoDigitalFinal2.entity.mapper.CartaoEntityRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public void save(CartaoEntity cartaoEntity) {
        String sql = "INSERT INTO cartoes (senha, situacao, fk_id_conta, valor_fatura, limite, limite_usado, tipo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                sql,
                cartaoEntity.getSenha(),
                cartaoEntity.getSituacao().name(),
                cartaoEntity.getIdConta(),
                cartaoEntity.getValorFatura(),
                cartaoEntity.getLimite(),
                cartaoEntity.getLimiteUsado(),
                cartaoEntity.getTipo().name()
        );
    }

    //READ
    public CartaoEntity findById(Long idCartao)
    {
        String sql = "SELECT id_cartao, senha, situacao, tipo, fk_id_conta, valor_fatura, limite, limiteusado" +
                " FROM cartoes WHERE id_cartao = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idCartao}, cartaoRowMapper);

    }


}
