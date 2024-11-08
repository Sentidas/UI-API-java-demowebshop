package tests;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class LoginTests extends TestBase {


    @Test
    void loginWithUITest() {
        step("Open login page", () ->
                open("/login"));
        step("Fill login form", () -> {
            $("#Email").setValue(login);
            $("#Password").setValue(password).pressEnter();
        });
        step("Fill login form", () ->
                $(".account").shouldHave(text(login)));
    }

    @Test
    void loginWithAPITest() {

        step("Get authorization cookie by api and set in to browser", () -> {
            String authCookieKey = "NOPCOMMERCE.AUTH";
            String authCookieValue = given()
                    .headers("Content-Type", "application/x-www-form-urlencoded")
                    .formParam("Email", login)
                    .formParam("Password", password)
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(302)
                    .extract()
                    .cookie("NOPCOMMERCE.AUTH");

            open("/Content/jquery-ui-themes/smoothness/jquery-ui-1.10.3.custom.min.css");
            Cookie cookie = new Cookie(authCookieKey, authCookieValue);
            getWebDriver().manage().addCookie(cookie);
        });

        step("Open main page", () ->
                open("/login"));


        step("Verify successful authorization", () ->
                $(".account").shouldHave(text(login)));
    }
}
