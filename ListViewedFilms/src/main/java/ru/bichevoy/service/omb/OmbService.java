package ru.bichevoy.service.omb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.bichevoy.dto.omb.OmbFilmFullDTO;
import ru.bichevoy.dto.omb.OmbFilmsDTO;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class OmbService {

    private final String OMB_URI;
    private final RestTemplate restTemplate;
    private final int cacheLife;
    private ScheduledExecutorService scheduled;

    public OmbService(@Value("${OMB_URI}") String OMB_URI,
                      @Value("${CACHE_LIFE}") String cacheLife) {
        this.OMB_URI = OMB_URI;
        this.cacheLife = Integer.parseInt(cacheLife);
        this.restTemplate = new RestTemplate();
    }

    @Cacheable(value = "findFilmsByTitle",
            key = "#title.toLowerCase() + '_page' + #page")
    public OmbFilmsDTO findFilmsByTitle(String title, int page) {
        String titleParam = "&s=" + title;
        String pageParam = "&page=" + page;
        String param = String.format("%s%s%s", OMB_URI, titleParam, pageParam);
        return restTemplate.getForObject(param, OmbFilmsDTO.class);
    }

    @Cacheable(value = "findFilmByImdbID", key = "#id")
    public Optional<OmbFilmFullDTO> findFilmByImdbID(String id) {
        String idParam = "&i=" + id;
        String param = OMB_URI + idParam;
        return Optional.ofNullable(restTemplate.getForObject(param, OmbFilmFullDTO.class));
    }

    @PostConstruct
    public void init() {
        scheduled = Executors.newSingleThreadScheduledExecutor();
        scheduled.schedule(() -> clearCache("findFilmsByTitle"), cacheLife, TimeUnit.MINUTES);
    }

    @PreDestroy
    public void destroy() {
        scheduled.shutdown();
    }

    @CacheEvict(key = "#cacheName", allEntries = true)
    public void clearCache(String cacheName) {
    }
}
