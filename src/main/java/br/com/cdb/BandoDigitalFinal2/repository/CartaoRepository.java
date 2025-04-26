package br.com.cdb.BandoDigitalFinal2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.cdb.BandoDigitalFinal2.entity.Cartao;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long>{

}
