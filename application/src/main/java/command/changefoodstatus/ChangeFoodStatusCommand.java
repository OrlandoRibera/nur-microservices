package command.changefoodstatus;

import an.awesome.pipelinr.Command;
import dto.ChangeFoodStatusDTO;
import dto.FoodDTO;

public class ChangeFoodStatusCommand implements Command<FoodDTO> {
  ChangeFoodStatusDTO changeFoodStatusDTO;

  public ChangeFoodStatusCommand(ChangeFoodStatusDTO changeFoodStatusDTO) {
    this.changeFoodStatusDTO = changeFoodStatusDTO;
  }
}
