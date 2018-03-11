package com.gcd.input.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gcd.input.service.GcdInputService;

@RestController
@RequestMapping("/")
public class GcdRestService {

	@Autowired
	GcdInputService gcdInputService;
	
	@RequestMapping(value = "/push", method = RequestMethod.POST)
	public ResponseEntity<String> push(@RequestParam("firstNumber") Integer firstNumber,
			@RequestParam("secondNumber") Integer secondNumber) {
		
		if (firstNumber <0 || secondNumber < 0) {
			return new ResponseEntity<String>("Invalid Input", HttpStatus.BAD_REQUEST);
		}
		gcdInputService.saveInput(firstNumber, secondNumber);
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	
	@RequestMapping(value= "/list", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<List<Integer>> list() {
		List<Integer> outputList = gcdInputService.getAllNumbers();
		return new ResponseEntity<List<Integer>>(outputList, HttpStatus.OK);
	}
}
