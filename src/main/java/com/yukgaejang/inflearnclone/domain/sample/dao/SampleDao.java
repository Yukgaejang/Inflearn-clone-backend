package com.yukgaejang.inflearnclone.domain.sample.dao;

import com.yukgaejang.inflearnclone.domain.sample.domain.Sample;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface SampleDao extends JpaRepository<Sample, Long>, SampleDaoCustom {
    @Override
    @NonNull
    Page<Sample> findAll(@NonNull Pageable pageable);

    @Override
    @NonNull
    Optional<Sample> findById(@NonNull Long id);
}
