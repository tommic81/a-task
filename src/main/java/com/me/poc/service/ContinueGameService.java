package com.me.poc.service;

import com.me.poc.controller.TransferObject;
import com.me.poc.controller.View;
import com.me.poc.exception.UnSupportedRequestParams;
import com.me.poc.repository.GameRepository;
import com.me.poc.view.ViewCommand;
import com.me.poc.view.ViewMenu;
import com.me.poc.view.ViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ContinueGameService implements ApplicationService {
    private final GameRepository gameRepository;


    @Override
    public TransferObject handle(Map<String, String> requestParams) {

        TransferObject.TransferObjectBuilder transferObjectBuilder = TransferObject.builder();

        if (requestParams.isEmpty()) {
            ViewModel.ViewModelBuilder viewModelBuilder = ViewModel.builder()
                .withTitle("Continue saved game");

            Set<String> allowedValues = new HashSet<>();
            allowedValues.add("M1");
            allowedValues.add("Q");

            ViewMenu menu = new ViewMenu("BACK (M1) QUIT (Q)", allowedValues);
            viewModelBuilder.withMenu(menu);


            ViewCommand.ViewCommandBuilder commandBuilder = ViewCommand.builder();

            List<String> savedGames = gameRepository.list();

            StringBuilder commandBodyBuilder = new StringBuilder();
            if (savedGames.isEmpty()) {
                commandBodyBuilder.append("You have no saved games!");
            } else {
                for (String saveGame : savedGames) {
                    commandBodyBuilder.append("- ").append(saveGame).append("\n");
                }
                commandBuilder
                    .withTitle("LIST of SAVED GAMES")
                    .withBody(commandBodyBuilder.toString().trim())
                    .withRequired(true)
                    .withLabel("Which game to load? ")
                    .withAllowedValues(savedGames)
                    .withInputKey("_saved-game_");

                viewModelBuilder.withViewCommand(commandBuilder.build());

            }




            transferObjectBuilder.withRedirect(false);
            transferObjectBuilder.withView(View.CONTINUE_GAME);
            transferObjectBuilder.withViewModel(viewModelBuilder.build());
        } else if (requestParams.containsKey("_saved-game_")) {
            //TODO:load serialized game and save to cache
            //TODO: moze trzeba przekazac id gry???
            transferObjectBuilder.withRedirect(true);
            transferObjectBuilder.withView(View.GAME);

        } else if ("M1".equalsIgnoreCase(requestParams.get(ViewMenu.INPUT_KEY))) {
            transferObjectBuilder.withRedirect(true);
            transferObjectBuilder.withView(View.START);
        } else {
            throw new UnSupportedRequestParams();
        }

        return transferObjectBuilder.build();
    }

    public ContinueGameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
}
