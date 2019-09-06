package com.yves.webmagiccrawler.service;

import com.yves.webmagiccrawler.entity.JobEntity;

import java.util.List;

public interface JobService {
    public void save(JobEntity jobEntity);

    public List<JobEntity> findAll(JobEntity jobEntity);
}
