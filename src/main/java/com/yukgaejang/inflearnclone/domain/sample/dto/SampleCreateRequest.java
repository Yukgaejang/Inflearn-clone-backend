package com.yukgaejang.inflearnclone.domain.sample.dto;

import com.yukgaejang.inflearnclone.domain.sample.domain.Sample;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SampleCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;
    
    public Sample toEntity(){
        return Sample.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}