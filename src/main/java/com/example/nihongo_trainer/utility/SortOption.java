package com.example.nihongo_trainer.utility;

import org.springframework.data.domain.Sort;

public enum SortOption {
    CREATED_ASC("createdAt", Sort.Direction.ASC),
    CREATED_DESC("createdAt", Sort.Direction.DESC),
    ALPHA_ASC("japanese", Sort.Direction.ASC),
    ALPHA_DESC("japanese", Sort.Direction.DESC),
    UPDATE_ASC("updatedAt", Sort.Direction.ASC),
    UPDATE_DESC("updatedAt", Sort.Direction.DESC);

    private final String field;
    private final Sort.Direction direction;

    SortOption(String field, Sort.Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public static SortOption from(String value) {
        for (SortOption option : values()) {
            if (option.name().equalsIgnoreCase(value)) {
                return option;
            }
        }
        return CREATED_DESC;
    }
}
