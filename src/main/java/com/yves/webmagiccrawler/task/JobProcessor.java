package com.yves.webmagiccrawler.task;

import com.yves.webmagiccrawler.entity.JobEntity;
import org.hibernate.service.spi.InjectService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import javax.inject.Inject;
import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    private String url = "https://search.51job.com/list/000000,000000,0000,01%252C32,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";
    @Override
    public void process(Page page) {
        //解析页面，获取招聘信息详情的url地址
        List<Selectable> list=page.getHtml().xpath("//div[@id=resultList]/div[@class=el]/p").nodes();

        //判断页面的集合是否为空
        if(list.size()==0) {
            this.saveJobInfo(page);
        }//如果为空，表示这是招聘详情页
        else{//如果不为空，表示这是列表页
            for(Selectable selectable:list){
                String jobInfoUrl = selectable.links().toString();
                page.addTargetRequest(jobInfoUrl);
            }
            String nextUrl=page.
                    getHtml().
                    xpath("//div[@class=p_in]/ul/li[@class=bk]").
                    nodes().
                    get(1).
                    links().
                    toString();
            page.addTargetRequest(nextUrl);
        }
    }

    //解析页面并保存数据
    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobEntity jobEntity = new JobEntity();
        //解析页面
        Html html = page.getHtml();
        //获取数据，封装到对象中
        jobEntity.setCompanyName(html.xpath("//div[@class=cn]/p[@class=cname]/a/text()").toString());
        jobEntity.setCompanyAddr(Jsoup.parse(html.css("div.bmsg").nodes().get(1).css("p").toString()).text());
        jobEntity.setJobName(html.xpath("//div[@class=cn]/h1/text()").toString());
        jobEntity.setJobAddr(html.xpath("//div[@class=cn]/p[2]/text(1)").toString());
        jobEntity.setJobInfo(Jsoup.parse(html.xpath("/html/body/div[3]/div[2]/div[3]/div[1]/div").toString()).text());
        jobEntity.setUrl(page.getUrl().toString());
        jobEntity.setSalary(html.xpath("//div[@class=cn]/strong/text()").toString());
        jobEntity.setTime(html.xpath("/html/body/div[3]/div[2]/div[2]/div/div[1]/p[2]/text(5)").toString());

        page.putField("jobEntity",jobEntity);
    }

    private Site site = Site.me()
            .setCharset("gbk")//设置编码
            .setTimeOut(1000)//设置超时时间
            .setRetrySleepTime(10000)//设置重试时间
            .setRetryTimes(3);//设置重试次数
    @Override
    public Site getSite() {
        return site;
    }



    @Autowired
    private SpringDatePipeline springDatePipeline;
    //initialDelay当任务启动后，等等多久执行方法
    //fixedDelay每隔多久执行方法
    @Scheduled(initialDelay = 1000,fixedDelay = 100*1000)
    public void process(){
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("183.166.171.15",9999)));

        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler()
                        .setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                .thread(5)
                .addPipeline(springDatePipeline)
                .run();
    }
}
