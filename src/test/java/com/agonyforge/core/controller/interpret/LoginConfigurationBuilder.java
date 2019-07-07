package com.agonyforge.core.controller.interpret;

import com.agonyforge.core.config.LoginConfiguration;

import java.util.HashMap;
import java.util.Map;

class LoginConfigurationBuilder {
    LoginConfiguration build() {
        LoginConfiguration loginConfiguration = new LoginConfiguration();
        Map<String, String> prompts = new HashMap<>();

        prompts.put("reconnect", "[default]Press [yellow]&#91;Enter&#93; [default]to reconnect as %name%. Or, log in a &#91;N&#93;ew character: ");
        prompts.put("askNew", "[default]Create a new character? [y/N]: ");
        prompts.put("loginAskName", "[default]Name: ");
        prompts.put("loginAskPassword", "[default]Password: ");
        prompts.put("createChooseName", "[default]Please choose a name: ");
        prompts.put("createConfirmName", "[default]Are you sure '%name%' is the name you want? [y/N]: ");
        prompts.put("createChoosePassword", "[default]Please choose a password: ");
        prompts.put("createConfirmPassword", "[default]Please confirm your password: ");
        prompts.put("inGame", "[default]%name%> ");

        loginConfiguration.setPrompt(prompts);

        return loginConfiguration;
    }
}
