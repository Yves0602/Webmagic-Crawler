package com.yves.webmagiccrawler.dao;

import com.yves.webmagiccrawler.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobDao extends JpaRepository<JobEntity,Long> {
}
