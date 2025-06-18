package br.com.cdb.BandoDigitalFinal2.adapter.out.persistence.mapper;


import br.com.cdb.BandoDigitalFinal2.domain.model.Conta;
import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ContaRowMapper implements RowMapper<Conta> {
    @Override
    public Conta mapRow(ResultSet rs, int rowNum) throws SQLException {

        Conta conta = new Conta();
        conta.setIdConta(rs.getLong("id_conta"));
        conta.setTipoConta(TipoConta.valueOf(rs.getString("tipo")));
        conta.setIdCliente(rs.getLong("fk_id_cliente"));
        conta.setSaldo(rs.getBigDecimal("saldo"));
        conta.setRendimento(rs.getBigDecimal("rendimento"));
        conta.setManutencao(rs.getBigDecimal("manutencao"));
        return conta;
    }
}