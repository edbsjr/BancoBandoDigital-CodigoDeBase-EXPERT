package br.com.cdb.BandoDigitalFinal2.infra;


import br.com.cdb.BandoDigitalFinal2.entity.Cliente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.fasterxml.jackson.databind.ObjectMapper; // Adicione este import
import com.fasterxml.jackson.databind.SerializationFeature; // Adicione este import
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Adicione este import para lidar com datas (LocalDateTime, LocalDate, etc.)

@Configuration
public class RedisConfig {

    @Bean // Bean para ser gerenciado pelo o String
    public RedisTemplate<String, Cliente> redisTemplate(RedisConnectionFactory connectionFactory) {
        // Cria uma nova instância de RedisTemplate
        RedisTemplate<String, Cliente> template = new RedisTemplate<>();
        // Define a fábrica de conexão que o Spring já auto-configurou (host, porta)
        template.setConnectionFactory(connectionFactory);

        // --- Configuração dos Serializadores ---
        // 1. Serializador para as CHAVES do Redis (geralmente Strings, como "cliente:id:123")
        // StringRedisSerializer é recomendado para chaves legíveis
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer()); // Para chaves de HASH, se você usar
        // 2. Serializador para os VALORES do Redis (seus objetos Cliente)
        // Jackson2JsonRedisSerializer serializa objetos Java para JSON e vice-versa


        // Configura o ObjectMapper dentro do serializador JSON
        // Isso é crucial para lidar corretamente com datas e evitar erros de serialização/desserialização
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Desabilita timestamps para datas
        objectMapper.registerModule(new JavaTimeModule()); // Suporte para tipos de data e hora do Java 8 (LocalDate, LocalDateTime)

        Jackson2JsonRedisSerializer<Cliente> jsonSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, Cliente.class);

        template.setValueSerializer(jsonSerializer); // Define o serializador para os valores principais
        template.setHashValueSerializer(jsonSerializer); // Para valores de HASH, se você usar

        template.afterPropertiesSet(); // Garante que todas as propriedades foram configuradas
        return template;
    }

}
