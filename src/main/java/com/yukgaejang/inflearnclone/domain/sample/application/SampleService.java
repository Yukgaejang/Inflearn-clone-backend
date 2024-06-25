package com.yukgaejang.inflearnclone.domain.sample.application;

import com.yukgaejang.inflearnclone.domain.sample.dao.SampleDao;
import com.yukgaejang.inflearnclone.domain.sample.domain.Sample;
import com.yukgaejang.inflearnclone.domain.sample.dto.SampleCreateRequest;
import com.yukgaejang.inflearnclone.domain.sample.dto.SampleResponse;
import com.yukgaejang.inflearnclone.domain.sample.dto.SampleUpdateRequest;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final SampleDao sampleDao;

    public Page<SampleResponse> getAll(Pageable pageable) {
        return sampleDao.findAll(pageable).map(entity -> entity.toResponse());
    }

    public SampleResponse getById(Long id) {
        Sample sample = findById(id);
        return sample.toResponse();
    }

    public Long save(SampleCreateRequest request) {
        Sample sample = sampleDao.save(request.toEntity());
        return sample.getId();
    }

    @Transactional
    public void update(Long id, SampleUpdateRequest request) {
        Sample sample = findById(id);
        sample.update(request);
        sampleDao.save(sample);
    }

    @Transactional
    public void delete(Long id) {
        Sample sample = findById(id);
        sampleDao.delete(sample);
    }

    private Sample findById(Long id) {
        Optional<Sample> sample = sampleDao.findById(id);
        if (sample.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 리소스");
        }
        return sample.get();
    }
}
