package br.com.cdb.BandoDigitalFinal2.entity.mapper;


import br.com.cdb.BandoDigitalFinal2.entity.Conta;
import br.com.cdb.BandoDigitalFinal2.entity.ContaEntity;
import br.com.cdb.BandoDigitalFinal2.enums.Situacao;
import br.com.cdb.BandoDigitalFinal2.enums.TipoCartao;
import br.com.cdb.BandoDigitalFinal2.enums.TipoConta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContaEntityRowMapper implements RowMapper<ContaEntity> {
    @Override
    public ContaEntity mapRow(ResultSet rs, int rowNum) throws SQLException {

        ContaEntity contaEntity = new ContaEntity();
        contaEntity.setIdConta(rs.getLong("id_conta"));
        contaEntity.setTipoConta(TipoConta.valueOf(rs.getString("tipo")));
        contaEntity.setIdCliente(rs.getLong("fk_id_cliente"));
        contaEntity.setSaldo(rs.getBigDecimal("saldo"));
        contaEntity.setRendimento(rs.getBigDecimal("rendimento"));
        contaEntity.setManutencao(rs.getBigDecimal("manutencao"));
        return contaEntity;
    }
}