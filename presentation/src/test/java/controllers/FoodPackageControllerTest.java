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
import infrastructure.model.FoodPackageStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FoodPackageControllerTest {

	private MockMvc mockMvc;

	@Mock
	private Pipeline pipeline;

	@InjectMocks
	private FoodPackageController foodPackageController;

	private UUID foodId;
	private UUID foodPackageId;
	private UUID recipeId;
	private UUID clientId;
	private UUID addressId;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(foodPackageController)
			.setMessageConverters(new MappingJackson2HttpMessageConverter())
			.build();
		foodId = UUID.randomUUID();
		foodPackageId = UUID.randomUUID();
		recipeId = UUID.randomUUID();
		clientId = UUID.randomUUID();
		addressId = UUID.randomUUID();
	}

	@Test
	void createPackage() throws Exception {
		FoodPackageDTO responseDTO = new FoodPackageDTO(foodPackageId.toString(), recipeId.toString(), clientId.toString(), addressId.toString(), List.of(), FoodPackageStatus.NEW);

		when(pipeline.send(any(CreateFoodPackageCommand.class))).thenReturn(responseDTO);

		// Request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("recipeId", recipeId.toString());
		jsonObject.put("clientId", clientId.toString());
		jsonObject.put("addressId", addressId.toString());

		mockMvc.perform(post("/api/catering/createPackage")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonObject.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").exists())
			.andExpect(jsonPath("$.id").value(foodPackageId.toString()));
	}

	@Test
	void createFood() throws Exception {
		FoodDTO requestDTO = new FoodDTO(foodId.toString(), "Pizza con pina", "PENDING", "DINNER", 1000.0f, foodPackageId.toString());
		FoodPackageDTO responseDTO = new FoodPackageDTO(foodPackageId.toString(), recipeId.toString(), clientId.toString(), addressId.toString(), List.of(requestDTO), FoodPackageStatus.NEW);

		CreateFoodInPackageCommand command = new CreateFoodInPackageCommand(requestDTO);
		when(pipeline.send(any(CreateFoodInPackageCommand.class))).thenReturn(responseDTO);

		// Request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", requestDTO.name());
		jsonObject.put("type", requestDTO.type());
		jsonObject.put("kcal", requestDTO.kcal());
		jsonObject.put("foodPackageId", requestDTO.foodPackageId());


		mockMvc.perform(post("/api/catering/createFoodInPackage").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString())).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.id").value(foodPackageId.toString())).andExpect(jsonPath("$.foods.length()").value(1));
	}

	@Test
	void updateFoodStatus() throws Exception {
		ChangeFoodStatusDTO requestDTO = new ChangeFoodStatusDTO(foodId.toString(), "COOKED");
		FoodDTO responseDTO = new FoodDTO(foodId.toString(), "Pizza con pina", "COOKED", "DINNER", 1000.0f, foodPackageId.toString());

		ChangeFoodStatusCommand command = new ChangeFoodStatusCommand(requestDTO);
		when(pipeline.send(any(ChangeFoodStatusCommand.class))).thenReturn(responseDTO);

		// Request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("foodId", requestDTO.foodId());
		jsonObject.put("status", requestDTO.newStatus());

		mockMvc.perform(post("/api/catering/updateFoodStatus").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString())).andExpect(status().isOk()).andExpect(jsonPath("$.foodId").exists()).andExpect(jsonPath("$.foodId").value(foodId.toString())).andExpect(jsonPath("$.status").value("COOKED"));
	}

	@Test
	void packFoodPackage() throws Exception {
		FoodPackageDTO responseDTO = new FoodPackageDTO(foodPackageId.toString(), recipeId.toString(), clientId.toString(), addressId.toString(), Collections.emptyList(), FoodPackageStatus.PACKED);

		PackFoodPackageCommand command = new PackFoodPackageCommand(foodPackageId.toString());
		when(pipeline.send(any(PackFoodPackageCommand.class))).thenReturn(responseDTO);

		// Request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", foodPackageId.toString());

		mockMvc.perform(post("/api/catering/packFoodPackage").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString())).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.id").value(foodPackageId.toString())).andExpect(jsonPath("$.status").value("PACKED"));
	}

	@Test
	void dispatchFoodPackage() throws Exception {
		FoodPackageDTO responseDTO = new FoodPackageDTO(foodPackageId.toString(), recipeId.toString(), clientId.toString(), addressId.toString(), Collections.emptyList(), FoodPackageStatus.DISPATCHED);

		DispatchFoodPackageCommand command = new DispatchFoodPackageCommand(foodPackageId.toString());
		when(pipeline.send(any(DispatchFoodPackageCommand.class))).thenReturn(responseDTO);

		// Request
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", foodPackageId.toString());

		mockMvc.perform(post("/api/catering/dispatchFoodPackage").contentType(MediaType.APPLICATION_JSON).content(jsonObject.toString())).andExpect(status().isOk()).andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.id").value(foodPackageId.toString())).andExpect(jsonPath("$.status").value("DISPATCHED"));
	}


	@Test
	void getFoodPackages() throws Exception {
		FoodPackageDTO packageDTO = new FoodPackageDTO(foodPackageId.toString(), recipeId.toString(), clientId.toString(), addressId.toString(), Collections.emptyList(), FoodPackageStatus.NEW);

		when(pipeline.send(any(GetFoodPackagesCommand.class))).thenReturn(List.of(packageDTO));

		mockMvc.perform(get("/api/catering/getAllPackages").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1)).andExpect(jsonPath("$[0].id").value(foodPackageId.toString())).andExpect(jsonPath("$[0].status").value("NEW"));
	}
}
