package br.com.bdws.facebookRobot;

import br.com.bdws.facebookRobot.service.DriverService;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Vasculhador implements ICommons {

    private int indexAtual = 0;
    private ChromeDriver driver = DriverService.get().getDriver();

    public void start() {
        driver.get("https://www.facebook.com/groups/feed/");
        sleep(5);
        percorrerEImprimir();

//        driver.findElements(By.xpath("//*[@class=\"qi72231t o9w3sbdw nu7423ey tav9wjvu flwp5yud tghlliq5 gkg15gwv s9ok87oh s9ljgwtm lxqftegz bf1zulr9 frfouenu bonavkto djs4p424 r7bn319e bdao358l fsf7x5fv tgm57n0e jez8cy9q s5oniofx m8h3af8h l7ghb35v kjdc1dyq kmwttqpk dnr7xe2t aeinzg81 srn514ro oxkhqvkx rl78xhln nch0832m om3e55n1 cr00lzj9 rn8ck1ys s3jn8y49 g4tp4svg jl2a5g8c f14ij5to l3ldwz01 icdlwmnq h8391g91 m0cukt09 kpwa50dg ta68dy8c b6ax4al1\"]"))
//                .stream()
//                .forEach(e -> {
//                    System.out.println(e.getText());
//                    System.out.println(e.getAttribute("href"));
//                    e.sendKeys(Keys.ARROW_DOWN);
//                });
    }

    private void percorrerEImprimir() {
        sleep(2);
        try {
            WebElement grupoAtual = getGrupoAtual();
            System.out.println(grupoAtual.getText());
            System.out.println(grupoAtual.getAttribute("href"));
            try {
                grupoAtual.sendKeys(Keys.ARROW_DOWN);
            } catch (ElementNotInteractableException e) {
                
            }
            indexAtual++;
            sleep(2);
            percorrerEImprimir();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WebElement getGrupoAtual() {
//        return driver.findElements(By.xpath("//*[@class=\"qi72231t o9w3sbdw nu7423ey tav9wjvu flwp5yud tghlliq5 gkg15gwv s9ok87oh s9ljgwtm lxqftegz bf1zulr9 frfouenu bonavkto djs4p424 r7bn319e bdao358l fsf7x5fv tgm57n0e jez8cy9q s5oniofx m8h3af8h l7ghb35v kjdc1dyq kmwttqpk dnr7xe2t aeinzg81 srn514ro oxkhqvkx rl78xhln nch0832m om3e55n1 cr00lzj9 rn8ck1ys s3jn8y49 g4tp4svg jl2a5g8c f14ij5to l3ldwz01 icdlwmnq h8391g91 m0cukt09 kpwa50dg ta68dy8c b6ax4al1\"]"))
        return driver.findElements(By.xpath("//*[@class=\"qi72231t o9w3sbdw nu7423ey tav9wjvu flwp5yud tghlliq5 gkg15gwv s9ok87oh s9ljgwtm lxqftegz bf1zulr9 frfouenu bonavkto djs4p424 r7bn319e bdao358l fsf7x5fv tgm57n0e jez8cy9q s5oniofx m8h3af8h l7ghb35v kjdc1dyq kmwttqpk dnr7xe2t aeinzg81 srn514ro oxkhqvkx rl78xhln nch0832m om3e55n1 cr00lzj9 rn8ck1ys s3jn8y49 g4tp4svg jl2a5g8c f14ij5to l3ldwz01 icdlwmnq h8391g91 m0cukt09 kpwa50dg ta68dy8c b6ax4al1\"]"))
                .get(indexAtual);
    }
}