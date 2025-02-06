package controllers;

import an.awesome.pipelinr.Pipeline;
import command.changefoodstatus.ChangeFoodStatusCommand;
import command.createfoodinpackage.CreateFoodInPackageCommand;
import command.createfoodpackage.CreateFoodPackageCommand;
import command.dispatchfoodpackage.DispatchFoodPackageCommand;
import command.getFoodPackages.GetFoodPackagesCommand;
import command.packfoodpackage.PackFoodPackageCommand;
import dto.ChangeFoodStatusDTO;
import dto.FoodDTO;
import dto.FoodPackageDTO;
import dto.PackDispatchFoodPackageDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catering")
public class FoodPackageController {
  final Pipeline pipeline;

  public FoodPackageController(Pipeline pipeline) {
    this.pipeline = pipeline;
  }

  @PostMapping("/createPackage")
  public FoodPackageDTO createPackage(@RequestBody FoodPackageDTO foodPackageDTO) {
    CreateFoodPackageCommand createFoodPackageCommand = new CreateFoodPackageCommand(foodPackageDTO.recipeId(), foodPackageDTO.clientId(), foodPackageDTO.addressId());
    return createFoodPackageCommand.execute(pipeline);
  }

  @PostMapping("/createFoodInPackage")
  public FoodPackageDTO createFood(@RequestBody FoodDTO foodDTO) {
    CreateFoodInPackageCommand createFoodInPackageCommand = new CreateFoodInPackageCommand(foodDTO);
    return createFoodInPackageCommand.execute(pipeline);
  }

  @PostMapping("/updateFoodStatus")
  public FoodDTO updateFoodStatus(@RequestBody ChangeFoodStatusDTO changeFoodStatusDTO) {
    ChangeFoodStatusCommand changeFoodStatusCommand = new ChangeFoodStatusCommand(changeFoodStatusDTO);
    return changeFoodStatusCommand.execute(pipeline);
  }

  @PostMapping("/packFoodPackage")
  public FoodPackageDTO packFoodPackage(@RequestBody FoodPackageDTO foodPackageId) {
    PackFoodPackageCommand packFoodPackageCommand = new PackFoodPackageCommand(foodPackageId.id());
    return packFoodPackageCommand.execute(pipeline);
  }

  @PostMapping("/dispatchFoodPackage")
  public FoodPackageDTO dispatchFoodPackage(@RequestBody FoodPackageDTO foodPackageId) {
    DispatchFoodPackageCommand dispatchFoodPackageCommand = new DispatchFoodPackageCommand(foodPackageId.id());
    return dispatchFoodPackageCommand.execute(pipeline);
  }

  @GetMapping("/getAllPackages")
  public List<FoodPackageDTO> getFoodPackages() {
    GetFoodPackagesCommand getFoodPackagesCommand = new GetFoodPackagesCommand();
    return getFoodPackagesCommand.execute(pipeline);
  }
}
