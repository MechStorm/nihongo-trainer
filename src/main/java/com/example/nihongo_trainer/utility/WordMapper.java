package com.example.nihongo_trainer.utility;

import com.example.nihongo_trainer.dto.WordDto;
import com.example.nihongo_trainer.entity.Word;

import java.time.ZoneId;

public class WordMapper {
    public static WordDto toDto(Word word) {
        WordDto dto = new WordDto();
        dto.setId(word.getId());
        dto.setJapanese(word.getJapanese());
        dto.setTranslation(word.getTranslation());
        dto.setExample(word.getExample());

        dto.setCreatedAt(word.getCreatedAt()
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime());
        dto.setUpdatedAt(word.getUpdatedAt() != null
                ? word.getUpdatedAt().atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Europe/Moscow"))
                .toLocalDateTime()
                : null);

        dto.setCategoryId(word.getCategory() != null ? word.getCategory().getId() : null);
        dto.setCategoryName(word.getCategory() != null ? word.getCategory().getName() : null);

        dto.setImagePath(word.getImagePath());
        dto.setLearned(word.isLearned());
        dto.setNeedsReview(word.isNeedsReview());

        return dto;
    }

    public static Word toEntity(WordDto dto) {
        Word word = new Word();
        word.setId(dto.getId());
        word.setJapanese(dto.getJapanese());
        word.setTranslation(dto.getTranslation());
        word.setExample(dto.getExample());
        word.setImagePath(dto.getImagePath());
        word.setLearned(dto.isLearned());
        word.setNeedsReview(dto.isNeedsReview());

        return word;
    }
}
