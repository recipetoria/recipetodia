package com.jit.rec.recipetoria.dto;

import com.jit.rec.recipetoria.entity.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TagDTO {
    @Nullable
    private Long id;
    @NotBlank
    private String name;
    @Nullable
    private String icon;

    public static TagDTO convertToTagDto(Tag tag){
        return new TagDTO(tag.getId(), tag.getName(), tag.getIcon());
    }



}
