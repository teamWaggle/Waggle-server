package com.example.waggle.dto.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({ValidationGroups.Default.class, ValidationGroups.NotEmpty.class, ValidationGroups.LimitCount.class })
public interface ValidationSequence {
    //default -> not empty -> limit count
}
