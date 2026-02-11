package com.tokubase.config;

import com.tokubase.model.Series;
import com.tokubase.model.SeriesType;
import com.tokubase.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final SeriesRepository seriesRepository;

    @Override
    public void run(String... args) {

        if (seriesRepository.count() == 0) {

            Series kuuga = Series.builder()
                    .name("Kamen Rider Kuuga")
                    .type(SeriesType.RIDER)
                    .yearStart(2000)
                    .yearEnd(2001)
                    .description("The first Heisei-era Kamen Rider series.")
                    .build();

            Series donbrothers = Series.builder()
                    .name("Avataro Sentai Donbrothers")
                    .type(SeriesType.SENTAI)
                    .yearStart(2022)
                    .yearEnd(2023)
                    .description("A chaotic and unique Sentai series.")
                    .build();

            seriesRepository.save(kuuga);
            seriesRepository.save(donbrothers);
        }
    }
}
