package br.com.cdb.BandoDigitalFinal2.entity.mapper;

import br.com.cdb.BandoDigitalFinal2.entity.CartaoEntity;
import br.com.cdb.BandoDigitalFinal2.enums.Situacao;
import br.com.cdb.BandoDigitalFinal2.enums.TipoCartao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartaoEntityRowMapper implements RowMapper<CartaoEntity> {
    @Override
    public CartaoEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        CartaoEntity cartaoEntity = new CartaoEntity();
        cartaoEntity.setIdCartao(rs.getLong("id_cartao"));
        cartaoEntity.setSenha(rs.getString("senha"));
        cartaoEntity.setSituacao(Situacao.valueOf(rs.getString("situacao")));
        cartaoEntity.setTipo(TipoCartao.valueOf(rs.getString("tipo")));
        cartaoEntity.setIdConta(rs.getLong("fk_id_conta"));
        cartaoEntity.setValorFatura(rs.getBigDecimal("valor_fatura"));
        cartaoEntity.setLimite(rs.getBigDecimal("limite"));
        cartaoEntity.setLimiteUsado(rs.getBigDecimal("limite_usado"));
        return new CartaoEntity();
    }



}
