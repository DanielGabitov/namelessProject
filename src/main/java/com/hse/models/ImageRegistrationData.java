package com.hse.models;

import com.hse.enums.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ImageRegistrationData {

    private Entity destination;
    private List<String> images;
    private long destinationId;
}
