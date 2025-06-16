package br.com.cdb.BandoDigitalFinal2.adapter.out.persistence;

import br.com.cdb.BandoDigitalFinal2.domain.model.CartaoEntity;
import br.com.cdb.BandoDigitalFinal2.adapter.out.persistence.mapper.CartaoEntityRowMapper;
import br.com.cdb.BandoDigitalFinal2.exceptions.RegistroNaoAtualizadoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.RegistroNaoDeletadoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.RegistroNaoEncontradoException;
import br.com.cdb.BandoDigitalFinal2.exceptions.RegistroNaoSalvoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartaoDao {

    private static final Logger log = LoggerFactory.getLogger(CartaoDao.class);
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
        String sql = "SELECT * FROM public.inserir_cartao_v1(?, ?, ?, ?, ?, ?, ?)";

        log.info("Passando os parametros para a insercao na base de dados");
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    cartaoEntity.getSenha(),
                    cartaoEntity.getSituacao().name(),
                    cartaoEntity.getIdConta(),
                    cartaoEntity.getValorFatura(),
                    cartaoEntity.getLimite(),
                    cartaoEntity.getLimiteUsado(),
                    cartaoEntity.getTipo().name()
            ));
        } catch (DataAccessException ex){
            log.error("Erro ao inserir cartao na base de dados.", ex);
            throw new RegistroNaoSalvoException("Cartao nao salvo. ");
        }
    }

    //READ
    public CartaoEntity findById(Long idCartao)
    {
        String sql = "SELECT id_cartao, senha, situacao, fk_id_conta, valor_fatura, limite, limite_usado, tipo" +
                " FROM public.busca_cartao_por_id_v1(?)";
        try {
            log.info("Iniciando busca na base de dados");
            return jdbcTemplate.queryForObject(
                    sql, new CartaoEntityRowMapper(), idCartao);
        } catch (EmptyResultDataAccessException ex) {
            log.error("Cartao ID {} nao encontrado", idCartao);
            return null;
        } catch (DataAccessException ex) {
            log.error("Erro ao tentar acessar a base de dados");
            throw new RegistroNaoEncontradoException("Erro ao tentar acessar a base de dados ");
        }
    }

    //READ ALL
    public List<CartaoEntity> findAll() {
        String sql = "SELECT id_cartao, senha, situacao, fk_id_conta, valor_fatura, limite, limite_usado,tipo " +
                "FROM public.lista_cartoes_completa_v1()";
        try {
            log.info("Iniciando busca das cartoes na base de dados");
            return jdbcTemplate.query(sql, cartaoRowMapper);
        } catch (DataAccessException ex) {
            log.error("Erro ao tentar acessar a base de dados");
            throw new RegistroNaoEncontradoException("Erro ao tentar acessar a base de dados ");
        }
    }


    //UPDATE
    //NAO POSSUI EDICAO DO ID_CONTA, TIPO POR NAO FAZER SENTIDO NA REGRA DE NEGOCIO
    public boolean update(CartaoEntity cartaoEntity)
    {
        String sql = "SELECT * FROM public.inserir_cartao_v1(?, ?, ?, ?, ?, ?, ?)";

        try {
            log.info("Passando parametros para atualizar cartao ID {}", cartaoEntity.getIdCartao());
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(
                    sql,
                    Boolean.class,
                    cartaoEntity.getIdCartao(),
                    cartaoEntity.getSenha(),
                    cartaoEntity.getSituacao().name(),
                    cartaoEntity.getValorFatura(),
                    cartaoEntity.getLimite(),
                    cartaoEntity.getLimiteUsado(),
                    cartaoEntity.getTipo()
            ));
        } catch (DataAccessException ex){
            log.error("Erro ao tentar atualizar a base de dados");
            throw new RegistroNaoAtualizadoException("Cliente nao atualizado");
        }
    }

    //DELETE
    public boolean deleteById(Long idCartao)
    {
        String sql = "SELECT * FROM public.deletar_cartoes_v1 (?)";
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, idCartao));
        } catch (DataAccessException ex){
            log.error("Erro ao tentar deletar na base de dados o cartao ID {}", idCartao);
            throw new RegistroNaoDeletadoException("Erro ao tentar deletar o cartao ID "+idCartao);
        }
    }

}
