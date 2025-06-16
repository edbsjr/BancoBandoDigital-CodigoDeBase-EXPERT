package br.com.cdb.BandoDigitalFinal2.adapter.out.redis;

import br.com.cdb.BandoDigitalFinal2.domain.model.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ClienteCacheService {

    private static final Logger log = LoggerFactory.getLogger(ClienteCacheService.class);

    private final RedisTemplate<String, Cliente> redisTemplate; // RedisTemplate tipado para Cliente
    private final String idCachePrefix = "cliente:id:"; // Prefixo para chaves de ID no Redis

    private final long cacheTtlMinutes = 30; // APOS 30 MINUTOS CACHE EXPIRA


    public ClienteCacheService(RedisTemplate<String, Cliente> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String generateRedisKey(Long id) {
        return idCachePrefix + id;
    }

    //Adiciona ou atualiza um Cliente no cache por ID.
    public void putById(Long id, Cliente cliente) {
        String key = generateRedisKey(id);
        // Define o valor no Redis com um tempo de expiração (TTL)
        redisTemplate.opsForValue().set(key, cliente, cacheTtlMinutes, TimeUnit.MINUTES);
        log.info("Cliente com ID {} colocado no cache. Expira em {} minutos.", id, cacheTtlMinutes);
    }

    //Busca um Cliente no cache por ID.
    public Optional<Cliente> getById(Long id) {
        String key = generateRedisKey(id);
        Cliente cliente = redisTemplate.opsForValue().get(key);
        if (cliente != null) {
            log.info("Cliente com ID {} encontrado no cache.", id);
        } else {
            log.info("Cliente com ID {} NÃO encontrado no cache.", id);
        }
        return Optional.ofNullable(cliente);
    }

    //Remove um Cliente do cache por ID.
    public void evictById(Long id) {
        String key = generateRedisKey(id);
        Boolean deleted = redisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.info("Cliente com ID {} removido do cache.", id);
        } else {
            log.warn("Tentativa de remover cliente com ID {} do cache, mas não foi encontrado.", id);
        }
    }
    
    public void evictAllClientesCache() {
        Set<String> keysToDelete = redisTemplate.keys(idCachePrefix + "*");
        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            Long deletedCount = redisTemplate.delete(keysToDelete);
            log.info("Total de {} clientes removidos do cache.", deletedCount);
        } else {
            log.info("Nenhum cliente encontrado para remover do cache.");
        }
    }

    //  Um métod o para verificar se a chave existe no cache
    public boolean containsKey(Long id) {
        if (id == null) {
            return false;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(generateRedisKey(id)));
    }
}
