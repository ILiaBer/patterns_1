package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import generator.Data;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TestPatterns{

        @BeforeAll
        static void setUpAll() {
            SelenideLogger.addListener("allure", new AllureSelenide());
        }

        @AfterAll
        static void tearDownAll() {
            SelenideLogger.removeListener("allure");
        }

        @BeforeEach
        void setUp() {
            open("http://localhost:9999/");
        }

        @Test
        void shouldSendCorrectForm() {
            Data.UserInfo user = Data.Registration.generateUser();
            $("[data-test-id='city'] input").setValue(Data.getRandomCity());
            $("[data-test-id=date] input").doubleClick().sendKeys(Data.generateDate(3));
            $("[data-test-id=name] input").setValue(user.getName());
            $("[data-test-id=phone] input").setValue(user.getPhone());
            $(".checkbox__box").click();
            $$("button").find(exactText("Запланировать")).click();
            $("[data-test-id=success-notification]").shouldHave(text("Встреча успешно запланирована на " + Data.generateDate(3)), Duration.ofSeconds(15));
            $("[data-test-id=date] input").doubleClick().sendKeys(Data.generateDate(6));
            $$("button").find(exactText("Запланировать")).click();
            $("[data-test-id=replan-notification]").shouldBe(visible).shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"), Duration.ofSeconds(15));
            $$("button").find(exactText("Перепланировать")).click();
            $("[data-test-id=success-notification]").shouldHave(text("Встреча успешно запланирована на " + Data.generateDate(6)), Duration.ofSeconds(15));
        }
    }
}

