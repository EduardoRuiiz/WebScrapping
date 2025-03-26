package com.testetecnico;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class App {

    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir");
        String driverPath = projectPath + "/drivers/chromedriver.exe";

        System.setProperty("webdriver.chrome.driver", driverPath);

        WebDriver driver = new ChromeDriver();

        driver.get("https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos");

        WebElement buttonCookies = driver.findElement(By.className("btn-accept"));
        buttonCookies.click();
        WebElement link1 = driver.findElement(By.linkText("Anexo I."));
        link1.click();
        WebElement link2 = driver.findElement(By.linkText("Anexo II."));
        link2.click();
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> abas = new ArrayList<>(driver.getWindowHandles());

        driver.switchTo().window(abas.get(1));
        String pdfUrl1 = driver.getCurrentUrl();
        downloadPdf(pdfUrl1, "AnexoI");

        driver.switchTo().window(abas.get(2));
        String pdfUrl2 = driver.getCurrentUrl();
        downloadPdf(pdfUrl2, "AnexoII");

        driver.quit();

    }

    private static void downloadPdf(String pdfUrl, String name) {

        String projectPath = System.getProperty("user.dir");
        String downloadPath = projectPath + "/downloads/" + name + ".pdf";
        try {
            URL url = new URL(pdfUrl);
            InputStream in = url.openStream();
            ReadableByteChannel rbc = Channels.newChannel(in);
            FileOutputStream fos = new FileOutputStream(downloadPath);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            in.close();
            System.out.println("Download concluído!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
