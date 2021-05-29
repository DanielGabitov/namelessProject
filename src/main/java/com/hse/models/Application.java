package com.hse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    private Long eventId;
    private Long creatorId;
    private String message;
    private Boolean accepted;
}