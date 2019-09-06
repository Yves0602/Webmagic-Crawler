package com.yves.webmagiccrawler.task;

import com.yves.webmagiccrawler.entity.JobEntity;
import com.yves.webmagiccrawler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class SpringDatePipeline implements Pipeline {
    @Autowired
    private JobService jobService;
    @Override
    public void process(ResultItems resultItems, Task task) {
        JobEntity jobEntity = resultItems.get("jobEntity");
        if(jobEntity!=null){
            jobService.save(jobEntity);
        }
    }
}
