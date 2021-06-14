package com.hse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invitation {
    private Long creatorId;
    private Long eventId;
    private Long organizerId;
    private String message;
    private Boolean accepted;
}