package br.com.bdws.facebookRobot;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

import static java.lang.System.lineSeparator;

public interface ICommons {

    final static Random random = new Random();
    public static final Logger logger = LoggerFactory.getLogger(ICommons.class);
    public static final String roboRootFolder = getRoboExecucaoFolder();

    public default void sleep(Integer multiplicador) {
        try {
            Long tempoEmMilisegundos = Long.valueOf((random.nextInt(500) + 500) * multiplicador);
            Thread.sleep(Long.valueOf(tempoEmMilisegundos));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    public default void digitar(WebElement webElementInput, String texto) {
        char[] chars = texto.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            try {
                Thread.sleep(random.nextInt(250) + 250);
            } catch (InterruptedException e) {
            } finally {
                webElementInput.sendKeys(chars[i] + "");
            }
        }
    }

    public default boolean isClicavel(WebElement webElement) {
        return webElement.isDisplayed() && webElement.isEnabled();
    }

    public default void println(String mensagem) {
        if (!Strings.isNullOrEmpty(mensagem)) {
            System.out.println("----- " + mensagem);
        }
    }

    public default void info(String info) {
        logger.info(info);
    }

    public default void error(Exception exception) {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        error(exception.getClass().getName() + lineSeparator() + stackTrace);
    }

    public default void error(String error) {
        logger.error(error);
    }

    public default void errorComMensagem(Exception exception, String mensagem) {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        error(concat(mensagem, lineSeparator(), exception.getClass().getName(), lineSeparator(), stackTrace));
    }

    public default String getSomenteLetrasENumeros(String texto) {
        return texto.replaceAll("[^a-zA-Z0-9]", "");
    }

    public default String getDiaHoraMinutoSegundo() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_HHmmss"));
    }

    public static String getRoboExecucaoFolder() {
        return System.getProperty("user.home").concat("/facebookRobotExecucao");
    }

    public default String concat(String... strs) {
        String str = "";
        for (String s : strs) {
            str = str.concat(s);
        }
        return str;
    }

    public default String concat(Object... args) {
        return concatSb(args).toString();
    }

    public default StringBuilder concatSb(Object... args) {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(args).forEach(a -> sb.append(a));
        return sb;
    }

    public default void tirarPrintScreen(ChromeDriver driver, String pathAndNomeArquivo) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(pathAndNomeArquivo));
        } catch (IOException e) {
            errorComMensagem(e, "takesScreenshot");
        }
    }

    public default WebElement buscarElementoPorTagETexto(ChromeDriver driver, String tag, String texto) {
        String xpath = concat("//", tag == null ? "*" : tag, "[contains(text(), '", texto, "')]");
        return driver.findElement(By.xpath(xpath));
    }

    public default WebElement buscarElementoPorTagEAriaLabel(ChromeDriver driver, String tag, String ariaLabel) {
        String xpath = concat("//", tag == null ? "*" : tag, "[@aria-label='", ariaLabel, "']");
        return driver.findElement(By.xpath(xpath));
    }
}