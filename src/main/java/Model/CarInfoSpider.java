package Model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Random;

public class CarInfoSpider {

    public static final String url = "http://brand.vw.com.cn/carmodels/models.php";

    public static void main(String[] args) {


//        ChromeDriverManager.getInstance().setup();
//        driver = new ChromeDriver();
        WebDriver webDriver = getDriver(url);

//        WebElement table = webDriver.findElement(By.id("main-box"));
        WebElement index = webDriver.findElement(By.xpath("//*[@id=\"main-box\"]/div[1]"));
//        List<WebElement> =

       String car = index.findElements(By.tagName("p")).get(0).getText();
        System.out.println(car);

    }

    public static WebDriver getDriver(String url) {

        try {

// 等待数据加载的时间

// 为了防止服务器封锁，这里的时间要模拟人的行为，随机且不能太短

            long waitLoadBaseTime = 3000;

//            int waitLoadRandomTime = 2000;

            Random random = new Random(System.currentTimeMillis());

// 设置 chrome 的路径,直接放在chrome的安装路径即可

            String chrome = "C:\\ChromeDriver\\chromedriver.exe";

//            String chrome = "/chromedriver.exe";

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

}
