package com.testetecnico;

import java.io.FileOutputStream;
import java.io.IOException;
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

public class WebScrapping {

    public static void run() {

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
        wait(3000);

        List<String> abas = new ArrayList<>(driver.getWindowHandles());

        for (int i = 0; i < abas.size(); i++) {
            String aba = abas.get(i);
            driver.switchTo().window(aba);
            String url = driver.getCurrentUrl();

            if (url.endsWith(".pdf")) {
                String nomeArquivo = url.substring(url.lastIndexOf('/') + 1);
                driver.switchTo().window(aba);
                String pdfUrl = driver.getCurrentUrl();
                System.out.println("URL do PDF: " + pdfUrl);
                downloadPdf(pdfUrl, nomeArquivo);
            }
        }
        driver.quit();
        String downloadPath = projectPath + "/downloads";
        try {
            CompactadorZip.compactarPasta(downloadPath, projectPath + "/output/Anexos.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void wait(int miliseconds) {

        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void downloadPdf(String pdfUrl, String name) {

        String projectPath = System.getProperty("user.dir");
        String downloadPath = projectPath + "/downloads/" + name;
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
