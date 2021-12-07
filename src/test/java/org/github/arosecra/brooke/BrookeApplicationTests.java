package org.github.arosecra.brooke;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.htmlunit.webdriver.LocalHostWebConnectionHtmlUnitDriver;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;

@SpringBootTest
@AutoConfigureMockMvc
class BrookeApplicationTests {
    
    @Autowired
    private WebApplicationContext context;
    
    private MockMvc mockMvc;
    private WebDriver driver;
	private WebClient webClient;
    
	@BeforeEach
    @SuppressWarnings("static-access")
    public void before() {

        this.mockMvc = MockMvcBuilders.
        		webAppContextSetup(this.context)
//        		.alwaysDo(print())
        		.build();
        this.webClient = MockMvcWebClientBuilder
        		.webAppContextSetup(context)
        		.mockMvcSetup(mockMvc)
        		.build();
        MockEnvironment env = new MockEnvironment();
        this.driver = MockMvcHtmlUnitDriverBuilder
        		.webAppContextSetup(this.context)
        		.mockMvcSetup(mockMvc)
        		.alwaysUseMockMvc()
        		.withDelegate(new LocalHostWebConnectionHtmlUnitDriver(env, BrowserVersion.CHROME))
        		.build();
    }
    
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.driver.get("/");
        System.out.println(driver.getPageSource());
        assertEquals("Home", driver.findElement(By.xpath("//a[1]")).getText());
        
        driver.findElement(By.xpath("//a[2]")).click();
        System.out.println(driver.getPageSource());
//        webClient.getPage("http://localhost:8080/")
    }

}
