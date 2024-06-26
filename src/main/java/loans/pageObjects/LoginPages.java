package loans.pageObjects;

import org.openqa.selenium.By;

public class LoginPages {

    public  static By kpxusername = By.xpath("//*[@placeholder='Username']");
    public  static By kpxpassword= By.xpath("//*[@placeholder='PASSWORD']");
    public  static By kpxlogind = By.xpath("//*[@type='submit']");
    public static By googleSign = By.xpath("//*[@class='nsm7Bb-HzV7m-LgbsSe-bN97Pc-sM5MNb ']");
    public static By email_google = By.xpath("//*[@id='identifierId']");
    public static By password_google = By.xpath("//*[@type='password']");
    public static By authenticator = By.xpath("//*[@type= 'tel']");
    public static By Dashboard = By.xpath ( "(//*[contains(text(), 'Dashboard')])[2]" );
    public static By objQCLL = By.xpath ( "//*[@id='__next']/div/div/div[1]/div/div[2]/h1" );
    public static By objcontinue =  By.xpath ( "//*[@id='__next']//span" );
    public static By invalid_login_credential = By.xpath("//*[@class='swal2-title']");
    public static By objokbutton =  By.xpath ( "//*[contains(text(), 'OK')]");





}
