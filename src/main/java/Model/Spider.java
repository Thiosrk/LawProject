package Model;

//import DB.DAOInterface;
//import Service.DBService;
import DB.LawDBImpl;
import DB.LawDBInterface;
import DB.LawRepository;
import DB.PersonRepository;
import Service.DBService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.bson.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


import java.io.*;
import java.util.*;
import java.util.function.Function;

/**
 * Created by 69401 on 2018/1/28.
 */
@SpringBootApplication(scanBasePackageClasses = LawDBInterface.class)
@EnableMongoRepositories(basePackages = "DB")
public class Spider implements CommandLineRunner {

    private Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    final static String readfile = "C:\\Users\\69401\\Desktop\\毕业设计资料\\法律\\司法解释\\";
    final static String savefile = "C:\\Users\\69401\\Desktop\\毕业设计资料\\法律(改)\\";
    final static String savelaw = "C:\\Users\\69401\\Desktop\\毕业设计资料\\法律\\司法程序法\\";
//    final static String savefile1 = "C:\\Users\\69401\\Desktop\\毕业设计资料\\法律\\";

    @Autowired
    private LawDBInterface lawDBImpl;

    @Autowired
    private LawRepository lawRepository;

    @Autowired
    private PersonRepository personRepository;
    DBService dbService = new DBService();
    @Override
    public void run(String... strings) throws Exception {

        Spider spider = new Spider();
//        String lawid = "a#18666";//民法,行政法，经济法，劳动法，司法程序法,国家机构法，其他法
//        String[] laws = {"a#18666","a#2156","a#21520","a#25905","a#2293","a#8142","a#11819"};
//
//
//        String lawname = "最高人民法院关于审理民间借贷案件适用法律若干问题的规定";
//
//        List<String> lawlist = new ArrayList<String>();
//
//        lawlist.add(lawname);
//////
//        Law law =  spider.getLawcontent(lawlist).get(0);

        System.out.println("-------------StratfindAndUpdate!-------------");
        List<LawModel> lawList = lawDBImpl.findLawAndUpdate();
//        System.out.println("-------------delete collection-------------");
//        lawDBImpl.delete();
        System.out.println("-----------------insert Laws------------------");
//        lawRepository.insert(lawList);
        lawDBImpl.save(lawList);
//        System.out.println("-------------test findOne------------");
//        List<String> lawnames = new ArrayList<String>();
//        lawnames.add("中华人民共和国慈善法");
//        lawnames.add("中华人民共和国境外非政府组织境内活动管理法");
//        List<LawModel> law = lawRepository.findAllByLawnameIn(lawnames);
//        System.out.println(law.getName());

//        for (LawModel tmp:law){
//            System.out.println(tmp.getName());
//        }
//        LawModel lawModel = lawRepository.findByLawname("中华人民共和国慈善法");
//        System.out.println(lawModel.getName());

//        String s = new String();
//        System.out.println(s);
//        Person person = new Person();
//        List<Age> ages = new ArrayList<Age>();
//        Age age = new Age();
//        age.setNian("1995");
//        age.setYue("12");
//        List<String> content = new ArrayList<String>();
//        content.add("123");
//        content.add("456");
//        content.add("789");
//        age.setContent(content);
//        ages.add(age);
//        Age age1 = new Age();
//        age1.setNian("1995");
//        age1.setYue("12");
//        ages.add(age1);
//        person.setAge(ages);
//        person.setName("徐文杰");
//        personRepository.insert(person);

//        Person person = personRepository.findByName("徐文杰");
//        System.out.println(person);
//        System.out.println("user is "+law);

    }

    public static void main(String args[]){

//        Spider spider = new Spider();
//        String lawid = "a#18666";//民法,行政法，经济法，劳动法，司法程序法,国家机构法，其他法
//        String[] laws = {"a#18666","a#2156","a#21520","a#25905","a#2293","a#8142","a#11819"};

        //刑事\民事\经济\交通\行政\民事诉讼程序\其他
//        String[] laws1 = {"a#27582","a#32030","a#9063","a#12872","a#6776","a#31253","a#28928"};
//        String[] lawname1 = {"刑事","民事","经济","交通","行政","民事诉讼程序","其他"};


//        for (int k=0;k<laws1.length;++k){
//            List<String> lawlist = spider.getLaw(laws1[k]);
//            for (int i=0;i<lawlist.size();++i){
//                String tmp = lawlist.get(i).replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
//                for (int j=i+1;j<lawlist.size();++j){
//                    String tmp1 = lawlist.get(j).replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
//                    if (tmp1.equals(tmp)){
//                        lawlist.remove(j);
//                        j--;
//                    }
//                }
//            }
//
//            String lawname = lawname1[k];
//            spider.saveAstxt(lawlist,lawname);
//
//        }


//        List<String> lawlist = spider.getLaw(laws[6]);
//
//        for (int i=0;i<lawlist.size();++i){
//            String tmp = lawlist.get(i).replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
//            for (int j=i+1;j<lawlist.size();++j){
//                String tmp1 = lawlist.get(j).replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
//                if (tmp1.equals(tmp)){
//                    lawlist.remove(j);
//                    j--;
//                }
//            }
//        }
//
//        System.out.println("--------------------final size : "+lawlist.size()+"------------------------------");
//
//        String lawname = "其他法.txt";
//        spider.saveAstxt(lawlist,lawname);



            //test getcontents
//        String url = "http://law.hnadl.cn/detail?record=1&ChannelID=18666&randno=659&resultid=3";
//        spider.getcontents(url);


//        String lawname = "其他法.txt";
//        String filename = savefile+lawname;
//        List<String> lawlist = spider.readFromtxt(filename);
//        lawlist = spider.queue(lawlist);
//        String lawname = "其他法.txt";
//        spider.saveAstxt(lawlist,lawname);
//        HashMap<String,String> lawmap= spider.getLawcontent(laws[4],lawlist);
//        spider.savelawsAstxt(lawmap);


        //无讼
//        String lawname = "中华人民共和国境外非政府组织境内活动管理法";

//        List<String> lawlist = new ArrayList<String>();

//        lawlist.add(lawname);

        SpringApplication.run(Spider.class, args);

//        daoInterface.insert(spider.getLawcontent(lawlist).get(0));

//        System.out.println(spider.getLawcontent(lawlist).get(0).toString());

//        String lawname = "刑事.txt";
//        String filename = readfile+lawname;
//        List<String> lawlist = spider.readFromtxt(filename);

//        String name = "中华人民共和国著作权法";
//
//        List<String> lawlist = new ArrayList<String>();
//
//        lawlist.add(name);

//        List<Law> list = spider.getLawcontent(lawlist);
//        for (int i=0;i<list.size();++i){
//            dbService.insertLaw(list.get(i));
//        }





    }

    //窗口切换
    private boolean switchToWindow(WebDriver driver,String windowTitle){
        boolean flag = false;
        try {
            String currentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String s : handles) {
                if (s.equals(currentHandle))
                    continue;
                else {
                    driver.switchTo().window(s);
                    if (driver.getTitle().contains(windowTitle)) {
                        flag = true;
                        System.out.println("Switch to window: "
                                + windowTitle + " successfully!");
                        break;
                    } else
                        continue;
                }
            }
        } catch (NoSuchWindowException e) {
            System.out.println("Window: " + windowTitle + " cound not found!"+e.fillInStackTrace());
            flag = false;
        }
        return flag;
    }

    //去除标题括号
    private List<String> queue(List<String> lawlist){

        for(int i=0;i<lawlist.size();++i){
            String str = lawlist.get(i).replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
            Collections.replaceAll(lawlist,lawlist.get(i),str);
        }
        return lawlist;

    }

    //删除部分
    private List<String> resave(List<String> lawlist){

        for(int i=0;i<lawlist.size();++i){
            String str = lawlist.get(i);
            if (str.contains("关于")&&str.contains("决定")){
                lawlist.remove(i);
                i--;
            }else if (str.contains("关于")&&str.contains("决议")){
                lawlist.remove(i);
                i--;
            }
        }
        return lawlist;

    }

    //读取文件
    private List<String> readFromtxt(String filename){
        File file = new File(filename);
        BufferedReader reader = null;
        List<String> lawlist = new ArrayList<String>();
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while((tempString = reader.readLine()) != null) {
                System.out.println("line " + line + ": " + tempString);
                line++;
                lawlist.add(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try{
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return lawlist;
    }

    //C:\Users\69401\Desktop\毕业设计资料
    //保存
    private void saveAstxt(List<String> lawlist,String lawname){

        FileWriter fwriter = null;
        int nums  = lawlist.size();
        try {
            fwriter = new FileWriter(readfile+lawname);

            for (String content:lawlist){
                fwriter.write(content+"\r\n");
            }
            fwriter.write(nums+"");

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fwriter.flush();
            fwriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    //保存法律
    private void savelawsAstxt(HashMap<String,String> laws){

        Iterator iterator = laws.entrySet().iterator();

        while(iterator.hasNext()){
            FileWriter fwriter = null;
            Map.Entry entry = (Map.Entry) iterator.next();
            try {
                fwriter = new FileWriter(savelaw+entry.getKey()+".txt");

                fwriter.write(entry.getValue()+"");

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }



    }

    //获取WebDriver
    public WebDriver getDriver(String url) {

        try {

// 等待数据加载的时间

// 为了防止服务器封锁，这里的时间要模拟人的行为，随机且不能太短

            long waitLoadBaseTime = 3000;

//            int waitLoadRandomTime = 2000;

            Random random = new Random(System.currentTimeMillis());

// 设置 chrome 的路径,直接放在chrome的安装路径即可

            String chrome = "C:\\ChromeDriver\\chromedriver.exe";

            System.setProperty("webdriver.chrome.driver", chrome);

            ChromeOptions options = new ChromeOptions();

// 通过配置参数禁止data;的出现

            options.addArguments(

                    "--user-data-dir=C:/Users/Administrator/AppData/Local/Google/Chrome/User Data/Default");

// 通过配置参数删除“您使用的是不受支持的命令行标记：--ignore-certificate-errors。稳定性和安全性会有所下降。”提示

            options.addArguments("--start-maximized", "allow-running-insecure-content", "--test-type");

            options.addArguments("--profile-directory=Default");

// userdata 设置使用chrome的默认参数

            options.addArguments("--user-data-dir=C:/Temp/ChromeProfile");

//也可以只用自己配置的chrom 设置地址：如下

//	options.addArguments("--user-data-dir=C:/Users/ZHL/AppData/Local/Google/Chrome/User Data");

// 创建一个 Chrome 的浏览器实例

            WebDriver driver = new ChromeDriver(options);

// 让浏览器访问微博主页

            driver.get(url);

// 等待页面动态加载完毕

            Thread.sleep(waitLoadBaseTime);

            return driver;

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }


    private  Document getDocument (String url){
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //from 国家法规数据库(http://law.hnadl.cn/web/index2.html)
    public HashMap<String,String> getLawcontent(String lawid,List<String> lawlist){

        String url1 = "http://law.hnadl.cn/tree?treekind=channel&randno=710&templet=tree.jsp&root=1&item=18666&opened=0,11,12,13,26,-1#18666";

        Document doc = getDocument(url1);

        Elements elements = doc.getAllElements();

        String str = elements.select(lawid).attr("href");

        System.out.println("-----------------"+"Start connect to ["+url1+"]-----------------");

        System.out.println("---------------get href :"+str+"-------------------");

        String lawurl = "http://law.hnadl.cn/"+str;

        System.out.println("-----------------"+"Start connect to ["+lawurl+"]-----------------");

        WebDriver webDriver = getDriver(lawurl);

        List<WebElement> tables = webDriver.findElements(By.tagName("table"));

        WebElement content = tables.get(1);

        WebElement choose = tables.get(2);

        WebElement pagedown = choose.findElement(By.id("downrec"));
        HashMap<String,String> lawcontent = new HashMap<String, String>();
        lawcontent = getcontent(content,lawlist,lawcontent,webDriver);

        int i = 0;

        do {
            ++i;
            System.out.println("---------------times : "+i+"-------------------");
            pagedown.click();

            tables = webDriver.findElements(By.tagName("table"));
            content = tables.get(1);
            choose = tables.get(2);
            pagedown = choose.findElement(By.id("downrec"));

            lawcontent = getcontent(content,lawlist,lawcontent,webDriver);

        }while((pagedown.getAttribute("href")!=null));

        return lawcontent;

    }

    private HashMap<String,String> getcontent(WebElement content,List<String> lawlist,HashMap<String,String> law,WebDriver webDriver){

        List<WebElement> laws = content.findElements(By.tagName("a"));

        String Handle = webDriver.getWindowHandle();

        for (WebElement tmp : laws){
            logger.debug("-------------------get name : "+tmp.getText()+"----------------------");

            for (int i=0;i<lawlist.size();++i){

                if (tmp.getText().equals(lawlist.get(i))){
//                    String url = "http://law.hnadl.cn/"+tmp.getAttribute("href");
                    logger.debug("-------------------law : "+lawlist.get(i)+"-------------------");
//                    System.out.println("-------------------url : "+url+"--------------------------");
//                    String contents = getcontents(url);
                    tmp.click();
//                    WebElement element = webDriver.findElement(By.id("tb"));
//                    webDriver.switchTo().window(webDriver.getWindowHandles().iterator().next());
                    switchToWindow(webDriver,lawlist.get(i));
                    WebElement element = webDriver.findElement(By.id("tb"));
                    law.put(lawlist.get(i),element.getText());
                    logger.debug(element.getText());
//                    webDriver.findElement(By.xpath("//table[1]/tbody/tr/td/a[1]")).click();
                    webDriver.close();
                    webDriver.switchTo().window(Handle);
//                    switchToWindow(webDriver,"民法");
                }
            }

        }


        return law;

    }

    public List<String> getLaw(String lawid){
//        String url = "http://law.hnadl.cn/web/index2.html";
//
//        Document doc = getDocument(url);
//
//        System.out.println("-----------------"+"Start connect to ["+url+"]-----------------");
//
//        Elements elements = doc.getAllElements();
//
//        String str = elements.select("frame#navigate").attr("src");

        //国家法律地址：http://law.hnadl.cn/tree?treekind=channel&randno=710&templet=tree.jsp&root=1&item=18666&opened=0,11,12,13,26,-1#18666
        //司法解释地址：http://law.hnadl.cn/tree?treekind=channel&randno=26676&templet=tree.jsp&root=1&item=3885&opened=0,12,13,185,-1#3885

        String url1 = "http://law.hnadl.cn/tree?treekind=channel&randno=26676&templet=tree.jsp&root=1&item=3885&opened=0,12,13,185,-1#3885";

        Document doc = getDocument(url1);

        Elements elements = doc.getAllElements();

        String str = elements.select(lawid).attr("href");

        System.out.println("-----------------"+"Start connect to ["+url1+"]-----------------");

        //http://law.hnadl.cn/tree?treekind=channel&randno=39&templet=tree.jsp&root=1&item=11&opened=0,-1#11
        //http://law.hnadl.cn/tree?treekind=channel&randno=6140&templet=tree.jsp&root=1&item=11&opened=0,11,-1#11

        System.out.println("---------------get href :"+str+"-------------------");

        String lawurl = "http://law.hnadl.cn/"+str;

        System.out.println("-----------------"+"Start connect to ["+lawurl+"]-----------------");

        WebDriver webDriver = getDriver(lawurl);

//        WebElement webElement = webDriver.findElement(By.xpath("/html"));
//
//        System.out.println(webElement.getAttribute("outerHTML"));
        List<WebElement> tables = webDriver.findElements(By.tagName("table"));

        WebElement content = tables.get(1);



        WebElement choose = tables.get(2);

        WebElement pagedown = choose.findElement(By.id("downrec"));

        List<String> lawlist = new ArrayList<String>();

        lawlist = getlaws(content,lawlist);

        int i = 0;

        do {
            ++i;
            System.out.println("---------------times : "+i+"-------------------");


            pagedown.click();

            tables = webDriver.findElements(By.tagName("table"));
            content = tables.get(1);
            choose = tables.get(2);
            pagedown = choose.findElement(By.id("downrec"));

            lawlist = getlaws(content,lawlist);

        }while((pagedown.getAttribute("href")!=null));


//        WebElement webElement = webDriver.findElement(By.id("goto1"));
//
//        System.out.println("-------------------get name : "+webElement.getText()+"----------------------");

        webDriver.close();

        return lawlist;

    }

    private List<String> getlaws(WebElement content,List<String> lawlist){

//        List<WebElement> trs = table.findElements(By.tagName("tr"));

//        for (WebElement tmp : trs){
//            WebElement td4 = tmp.findElements(By.tagName("td")).get(3);
//            if (td4.getText())
//            System.out.println("--------------if valid : "+td4.getText()+"-----------------" );
//        }

        List<WebElement> laws = content.findElements(By.tagName("a"));

        System.out.println("------------------size : "+laws.size()+"--------------------");

        for (WebElement tmp : laws){

            String tmpstr = tmp.getText();
            System.out.println("-------------------get name : "+tmpstr+"----------------------");
            if (tmpstr.contains("最高人民法院")&&tmpstr.contains("关于")&&tmpstr.contains("规定"))
                lawlist.add(tmpstr);
        }

        return lawlist;
    }


    //from 无讼(https://www.itslaw.com/)
    public List<Law> getLawcontent(List<String> lawlist){

        List<Law> result = new ArrayList<Law>();

        String url = "https://www.itslaw.com/search/lawsAndRegulations?searchMode=lawsAndRegulations&sortType=5&needCorrection=1";

        WebDriver webDriver = getDriver(url);

        for (int i=0;i<lawlist.size();i++){
            Law law = new Law(lawlist.get(i));

            law = getLaw(law,webDriver);

            result.add(law);
            dbService.insertLaw(law);
        }



        return result;
    }

    private boolean ElementExist(WebDriver driver,By locator)
    {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    protected Function<WebDriver, Boolean> isPageLoaded() {
        return new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
    }


    private Law getLaw(Law law,WebDriver webDriver){
        WebDriverWait wait = new WebDriverWait(webDriver, 20);
        String lawname = law.getName();
        wait.until(isPageLoaded());
        List<WebElement> elements = webDriver.findElements(By.tagName("input"));
        logger.debug("Size : "+elements.size());
        WebElement webElement = elements.get(0);
        webElement.click();
        webElement.sendKeys(lawname);
        webDriver.findElement(By.className("search-box-btn")).click();

        ///html/body/div[1]/div/section[1]/section[2]/section
        ///html/body/div[1]/div/section[1]/section[3]/section/header/span[4]
        ///html/body/div[1]/ws-header/header
        ///html/body/div[1]/div/section[1]/section[2]/section/header/span[4]
        List<WebElement> sections = new ArrayList<>();
        wait.until(isPageLoaded());
        if (ElementExist(webDriver,By.xpath("/html/body/div[1]/div/section[1]/section[3]/section/header/span[4]"))){
//            webDriver.findElement(By.xpath("/html/body/div[1]/div/section[1]/section[3]/section/header/span[4]")).click();
            ////*[@id="现行有效::TopLevel::1_anchor"]
//            webDriver.findElement(By.xpath("//*[@id=\"现行有效::TopLevel::1_anchor\"]")).click();
            wait.until(isPageLoaded());
            sections = webDriver.findElements(By.xpath("/html/body/div[1]/div/section[1]/section[3]/section/section"));
        }else {

//            webDriver.findElement(By.xpath("/html/body/div[1]/div/section[1]/section[2]/section/header/span[4]")).click();
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[1]/div/section[1]/section[2]/section/section")));
            ///html/body/div[1]/div/section[1]/section[2]/section/section[1]
            sections = webDriver.findElements(By.xpath("/html/body/div[1]/div/section[1]/section[2]/section/section"));
        }






        logger.debug(sections.size());

        for (WebElement element :sections){
            WebElement status = element.findElement(By.tagName("span"));
            logger.debug(status.getText());
            String statusstr = status.getText();
            if (statusstr.equals("现行有效")){
                WebElement title = element.findElement(By.className("detail")).findElement(By.tagName("a"));
//                String titlestr= title.getText().replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
                String titlestr= title.getText();
                logger.debug(titlestr);
//                if (lawname.equals("中华人民共和国反不正当竞争法2017")){
//                    lawname = "中华人民共和国反不正当竞争法";
//                }
//                lawname = lawname.replaceAll("\\(.*?\\)|\\{.*?}|\\[.*?]|（.*?）", "");
                //||titlestr.contains(lawname)
                if (titlestr.equals(lawname)){
                    title.click();
                    break;
                }
            }

        }

        String handle = webDriver.getWindowHandle();
        for (String handles : webDriver.getWindowHandles()) {
            if (handles.equals(handle))
                continue;
            webDriver.switchTo().window(handles);
            logger.debug("has changed!");
        }

//        WebDriverWait wait = new WebDriverWait(webDriver, 20);


        wait.until(isPageLoaded());

//        System.out.println(webDriver.getTitle());

        //正文：/html/body/div[1]/div/article/section/section/section[3]/section

        //attributes: /html/body/div[1]/div/article/section/section/section[1]/div[2]
        WebElement attributes = webDriver.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[1]/div[2]"));

        List<WebElement> a = attributes.findElements(By.xpath("/html/body/div[1]/div/article/section/section/section[1]/div[2]/span"));
        String publish = "";
        String start = "";
        String timelimit = "";
        if (a.size()>2){
            //publish: /html/body/div[1]/div/article/section/section/section[1]/div[2]/span[1]/span
            publish = attributes.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[1]/div[2]/span[1]/span")).getText();
            //start: /html/body/div[1]/div/article/section/section/section[1]/div[2]/span[2]/span
            start = attributes.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[1]/div[2]/span[2]/span")).getText();
            //timelimit: /html/body/div[1]/div/article/section/section/section[1]/div[2]/span[3]/span
            timelimit = attributes.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[1]/div[2]/span[3]/span")).getText();
        }else {
            //publish: /html/body/div[1]/div/article/section/section/section[1]/div[2]/span[1]/span
            publish = attributes.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[1]/div[2]/span[1]/span")).getText();
            //timelimit: /html/body/div[1]/div/article/section/section/section[1]/div[2]/span[3]/span
            timelimit = attributes.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[1]/div[2]/span[2]/span")).getText();
        }

        law.setTimelimit(timelimit);
        law.setPublishtime(publish);
        law.setStarttime(start);
        if (timelimit.equals("失效")){
            webDriver.close();
            webDriver.switchTo().window(handle);
            //“x”Button 后退
            WebElement element = webDriver.findElement(By.xpath("//div[1]/div/section/nav/div/div/span//span[2]"));
            element.click();
        }else {
            law = getTiao(webDriver,law,handle,timelimit);
        }


        return law;

    }

    private Law getTiao(WebDriver webDriver,Law law,String handle,String timelimit){

        List<WebElement> divs = new ArrayList<>();

        if (timelimit.equals("现行有效")){
            ///html/body/div[1]/div/article/section/section/section[2]
            WebElement content = null;
            if (ElementExist(webDriver,By.xpath("/html/body/div[1]/div/article/section/section/section[3]/section"))){
                content = webDriver.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[3]/section"));
                divs = content.findElements(By.xpath("/html/body/div[1]/div/article/section/section/section[3]/section/div"));
            }else {
                content = webDriver.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[2]/section"));
                divs = content.findElements(By.xpath("/html/body/div[1]/div/article/section/section/section[2]/section/div"));
            }
            logger.debug(content.getText());
            logger.debug("divs size :"+divs.size());
        }else {
            ///html/body/div[1]/div/article/section/section/section[3]
            ///html/body/div[1]/div/article/section/section/section[4]/section
            WebElement content = webDriver.findElement(By.xpath("/html/body/div[1]/div/article/section/section/section[4]/section"));
            logger.debug(content.getText());
            divs = content.findElements(By.xpath("/html/body/div[1]/div/article/section/section/section[4]/section/div"));
            logger.debug("divs size :"+divs.size());
        }


        List<Tiao> tiaoList = new ArrayList<Tiao>();
        Tiao tiao = null;
        List<Kuan> kuanList = new ArrayList<Kuan>();
        Kuan kuan = null;
        List<String> xiang = new ArrayList<String>();
        boolean flag = true;
        boolean start = false;
        for (WebElement div :divs){
            String tmp = div.findElement(By.tagName("span")).getText();
            logger.debug("***"+tmp+"***");

            if (start){
                if (tmp.startsWith("第")&&tmp.contains("章")){
                    continue;
                }
                if(tmp.startsWith("第")&&tmp.endsWith("条")){
                    if (tiao != null){

                        logger.debug(tiao.getContent()+"结束！");
                        tiao.setKuan(kuanList);
                        tiaoList.add(tiao);
                    }
                    logger.debug(tmp+"开始：");
                    logger.debug("刷新条----------");
                    tiao = new Tiao();
                    logger.debug("刷新款list----------");
                    kuanList = new ArrayList<Kuan>();
                    tiao.setContent(tmp);
                    continue;
                }

                if(flag){
//                if (kuan!=null){
//                    logger.debug("第"+kuanList.indexOf(kuan)+"款"+"结束！");
//                    kuan = new Model.Kuan();
//                }
//                logger.debug("第"+(kuanList.indexOf(kuan)+1)+"款"+"开始");
                    logger.debug("刷新款----------");
                    kuan = new Kuan();
                    kuan.setContent(tmp);
                    if (tmp.contains("：")||tmp.contains(":")){
                        flag = false;
                        logger.debug("进入项---------");
                    }else {
                        Kuan kuan1 = new Kuan(kuan);
                        kuanList.add(kuan1);
                    }
                }else {
                    logger.debug("加入项------------");
                    xiang.add(tmp);
                    if (tmp.contains("。")){
                        logger.debug("项结束----------");
                        flag = true;
                        kuan.setXiang(xiang);
                        Kuan kuan1 = new Kuan(kuan);
                        kuanList.add(kuan1);
                        kuan = new Kuan();
                        logger.debug("刷新项----------");
                        xiang = new ArrayList<String>();
                    }
                }
                tiao.setKuan(kuanList);
                tiaoList.add(tiao);
            }else {
                if (tmp.equals("第一条")){
                    start = true;
                    tiao = new Tiao();
                    kuanList = new ArrayList<Kuan>();
                    tiao.setContent(tmp);
                }
            }



//            logger.debug();
        }
        law.setTiao(tiaoList);

        webDriver.close();
        webDriver.switchTo().window(handle);
        //“x”Button 后退
        WebElement element = webDriver.findElement(By.xpath("//div[1]/div/section/nav/div/div/span//span[2]"));
        element.click();

        return law;

    }


}
