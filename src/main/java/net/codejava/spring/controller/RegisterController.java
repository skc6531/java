package net.codejava.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Map;
import net.codejava.spring.model.User;
import net.codejava.spring.model.WeatherRpt;

@Controller
public class RegisterController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login(Map<String, Object> model) {
		User userForm = new User();
		model.put("userForm", userForm);

		// ModelAndView model= new ModelAndView ("Registration");
		return "Registration";
	}

	@RequestMapping(value = "/loginsuccess", method = RequestMethod.POST)
	public String loginSuccess(@ModelAttribute("userForm") User user) {

		// ModelAndView model= new ModelAndView ("RegistrationSuccess");
		return "RegistrationSuccess";
	}

	@RequestMapping(value = "/Getweather", method = RequestMethod.POST)
	public ModelAndView getWeather(@ModelAttribute("userForm") User user1) {
		String zipcode = user1.getZipcode();
		final String uri = "http://api.openweathermap.org/data/2.5/weather?zip=" + zipcode
				+ ",us&units=imperial&appid=1313ccf9127c7d162cb50e1fb3eef177";

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.getForObject(uri, String.class);
		ModelAndView model = new ModelAndView("GetWeather");

		WeatherRpt weather = new WeatherRpt();

		try {
			ObjectNode json = new ObjectMapper().readValue(result, ObjectNode.class);
			JsonNode mainnode = json.get("main");
			weather.setTemperature(mainnode.get("temp").toString());

			weather.setPlace(json.get("name").toString());

		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addObject("weather", weather);
		return model;
	}
}
