package com.example.nihongo_trainer.utility;

import com.example.nihongo_trainer.dto.WordDto;
import com.example.nihongo_trainer.entity.Word;

import java.time.ZoneId;

public class WordMapper {
    public static WordDto toDto(Word word) {
        return new WordDto(
                word.getId(),
                word.getJapanese(),
                word.getTranslation(),
                word.getExample(),
                word.getCreatedAt()
                        .atZone(ZoneId.of("UTC"))
                        .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                        .toLocalDateTime(),
                word.getUpdatedAt() != null
                        ? word.getUpdatedAt().atZone(ZoneId.of("UTC"))
                        .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                        .toLocalDateTime()
                        : null,
                word.getCategory() != null ? word.getCategory().getId() : null,
                word.getCategory() != null ? word.getCategory().getName() : null
        );
    }
}
