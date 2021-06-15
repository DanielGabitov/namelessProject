package com.hse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeAssociationInvitation {
    private Long associationId;
    private Long creatorId;
    private Boolean isAnswered;
    private Boolean accepted;
}
