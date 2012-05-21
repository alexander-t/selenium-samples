/*
 * Copyright (c) 2012, Alexander Tarnowski
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *   disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package se.tarnowski.sample.async;

import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import se.tarnowski.sample.page.ValidationPage;

/**
 * Demonstrates the use of a variable in Javascript to keep track of an ongoing Ajax submit.
 */
public class AjaxStatusWithJavascriptVariableTest {

    WebDriver webDriver = new HtmlUnitDriver(true);
    ValidationPage page = new ValidationPage(webDriver);

    @Test
    public void run() throws InterruptedException {
        JavascriptExecutor jse = (JavascriptExecutor) webDriver;
        webDriver.get("http://localhost:8080/validate.html");

        String script = "window.ajaxCallCount = 0;" +
                "var oldOpen = xmlhttp.open;" +
                "var oldOnreadestatechange = xmlhttp.onreadystatechange;" +
                "xmlhttp.open = function() {" +
                "    window.ajaxCallCount++;" +
                "    oldOpen.apply(this, arguments);" +
                "};" +
                "xmlhttp.onreadystatechange = function () {" +
                "    if (xmlhttp.readyState == 4) { " +
                "        window.ajaxCallCount--; " +
                "    }" +
                "    oldOnreadestatechange.apply(this, arguments);" +
                "};";
        jse.executeScript(script);
        page.enterTextToValidate("invalid");
        page.clickValidateButton();

        for (int seconds = 0; seconds < 5; seconds++) {
            printValidationMesssageAndCallCount(seconds);
            Thread.sleep(1000);
        }
    }

    private void printValidationMesssageAndCallCount(int seconds) {
        long ajaxCallCount = (Long) ((JavascriptExecutor) webDriver).executeScript("return window.ajaxCallCount;");
        System.err.println("After " + seconds + " seconds: ");
        System.err.println("validation message='" + page.getValidationMessage() + "', ajaxCallCount=" + ajaxCallCount);
    }

}
