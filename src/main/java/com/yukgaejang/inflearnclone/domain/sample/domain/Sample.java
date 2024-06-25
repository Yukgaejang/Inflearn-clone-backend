package com.yukgaejang.inflearnclone.domain.sample.domain;

import com.yukgaejang.inflearnclone.domain.model.BaseEntity;
import com.yukgaejang.inflearnclone.domain.sample.dto.SampleResponse;
import com.yukgaejang.inflearnclone.domain.sample.dto.SampleUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sample extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    public SampleResponse toResponse(){
        return SampleResponse.builder()
                .id(this.getId())
                .title(this.getTitle())
                .content(this.getContent())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .build();
    }

    public void update(SampleUpdateRequest request) {
        if(!request.getTitle().equals("") || request.getTitle() != null){
            this.title = request.getTitle();
        }
        if(!request.getContent().equals("") || request.getContent() != null){
            this.content = request.getContent();
        }
    }
}
