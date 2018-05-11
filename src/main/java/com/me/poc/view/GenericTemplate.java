package com.me.poc.view;

import com.me.poc.util.ANSII;
import com.me.poc.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GenericTemplate implements ViewTemplate {

    private final BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    public Map<String, String> render(ViewModel model) {

        final StringBuilder errorMessageBuilder = new StringBuilder();
        Map<String, String> results = new HashMap<>();
        ViewMenu menu = model.getMenu();
        do {

            eraseScreen();
            renderTitle(model.getTitle());

            renderInfoMsg(model.getInfoMessage());
            renderErrorMsg(model.getErrorMessage());
            renderErrorMsg(errorMessageBuilder.toString());

            renderMenu(menu.getText());

            renderUnFormattedText(model.getIntro());

            List<ViewCommand> viewCommands = model.getCommands();
            try {

                if (viewCommands.isEmpty()) {
                    renderCommand(menu.getLabel());
                    String inputValue = inputReader.readLine();
                    if (menu.isMenuCommand(inputValue)) {
                        results.put(menu.getInputKey(), inputValue);
                        return results;
                    } else {
                        errorMessageBuilder.append("Provided unsupported Menu value: ").append(inputValue);
                        continue;
                    }
                }
                for (ViewCommand eachCommand : viewCommands) {

                    renderUnFormattedText(eachCommand.getTitle());
                    renderUnFormattedText(eachCommand.getBody());
                    renderUnFormattedText(eachCommand.getLabel());
                    renderCommand(eachCommand.getLabel());

                    //was provided previously
                    String inputValue = results.get(eachCommand.getInputKey());

                    if (StringUtils.isBlank(inputValue)) {
                        inputValue = inputReader.readLine();

                        if (menu.isMenuCommand(inputValue)) {
                            results.put(menu.getInputKey(), inputValue);
                            return results;
                        }
                        Set<ViewCommand.ValidationStatus> validationStatuses = eachCommand.validateInputCommand(inputValue);
                        if (validationStatuses.contains(ViewCommand.ValidationStatus.VALID)) {
                            results.put(eachCommand.getInputKey(), inputValue);
                            errorMessageBuilder.delete(0, errorMessageBuilder.length() - 1);
                            continue;
                        } else {

                            validationStatuses.forEach(validationStatus -> errorMessageBuilder.append(validationStatus.getDefaultMessage()).append("\n"));
                            break;
                        }

                    } else {
                        renderUnFormattedText(inputValue);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

        } while (true);

    }


    void renderCommand(String commandText) {
        System.out.println("\n");
        System.out.print(commandText + " " + ANSII.NORMAL);
    }

    void renderUnFormattedText(String text) {
        if (StringUtils.isNotBlank(text)) {
            System.out.println(text);
        }
    }
    void eraseScreen() {
        System.out.println(ANSII.ERASE_SCREEN);
    }

    void renderTitle(String title) {
        System.out.println(ANSII.BACKGROUND_CYAN + title + ANSII.NORMAL);
    }
    void renderErrorMsg(String errorMessage) {
        if (StringUtils.isNotBlank(errorMessage)) {
            System.out.println(ANSII.BACKGROUND_RED + errorMessage + ANSII.NORMAL);

        }
    }

    void renderInfoMsg(String infoMessage) {
        if (StringUtils.isNotBlank(infoMessage)) {
            System.out.println(ANSII.BACKGROUND_GREEN + infoMessage + ANSII.NORMAL);

        }
    }

    void renderMenu(String menu) {
        System.out.println(ANSII.BLUE + menu + ANSII.NORMAL +"\n");

    }

}
