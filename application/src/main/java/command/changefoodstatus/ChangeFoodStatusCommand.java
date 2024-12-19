package command.changefoodstatus;

import an.awesome.pipelinr.Command;
import dto.ChangeFoodStatusDTO;
import dto.FoodPackageDTO;

public class ChangeFoodStatusCommand implements Command<FoodPackageDTO> {
  ChangeFoodStatusDTO changeFoodStatusDTO;

  public ChangeFoodStatusCommand(ChangeFoodStatusDTO changeFoodStatusDTO) {
    this.changeFoodStatusDTO = changeFoodStatusDTO;
  }
}
