        package com.hse.DAOs;

import com.hse.models.Event;
import com.hse.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

        @Component
public class ImageDAO {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Image> imageMapper = new BeanPropertyRowMapper<>(Image.class);

    @Autowired
    public ImageDAO(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void loadImage(byte[] image, String eventName) throws IOException {
        jdbcTemplate.update("INSERT INTO images (eventName, data) VALUES (?, ?)", eventName, image);
    }

    public List<byte[]> getImagesWithGivenEventName(String eventName) {
        return jdbcTemplate.query("SELECT * FROM images WHERE eventName=?", new Object[]{eventName}, imageMapper)
                .stream().map(Image::getData).collect(Collectors.toList());
    }
}
