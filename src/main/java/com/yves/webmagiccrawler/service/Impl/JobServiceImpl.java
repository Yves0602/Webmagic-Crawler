package com.yves.webmagiccrawler.service.Impl;

import com.yves.webmagiccrawler.dao.JobDao;
import com.yves.webmagiccrawler.entity.JobEntity;
import com.yves.webmagiccrawler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobDao jobDao;

    @Override
    @Transactional
    public void save(JobEntity jobEntity) {
        //根据url和发布时间查询数据
        JobEntity param  = new JobEntity();
       param.setUrl(jobEntity.getUrl());
       param.setTime(jobEntity.getTime());

       List<JobEntity> list = this.findAll(param);

       if(list.size()==0){
           jobDao.saveAndFlush(jobEntity);
       }

    }

    @Override
    public List<JobEntity> findAll(JobEntity jobEntity) {
        Example example  = Example.of(jobEntity);
        List list=jobDao.findAll(example);
        return list;
    }
}
