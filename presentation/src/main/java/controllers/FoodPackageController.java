package controllers;

import an.awesome.pipelinr.Pipeline;
import command.createfoodinpackage.CreateFoodInPackageCommand;
import command.createfoodpackage.CreateFoodPackageCommand;
import dto.FoodDTO;
import dto.FoodPackageDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
