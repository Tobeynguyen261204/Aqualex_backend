package com.aquaLex.contract.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import com.aquaLex.contract.application.ContractService;
import com.aquaLex.contract.dto.ContractResponse;
import com.aquaLex.contract.dto.CreateContractRequest;
@RestController
@RequestMapping("/api/v1/contracts")
public class ContractController {
    private final ContractService contractService;
    
public ContractController(ContractService contractService){
    this.contractService = contractService;
}    
@PostMapping("/create")
@Operation (summary = "Create a new contract", description = "Creates a new contract with the provided details.")
@ResponseStatus(HttpStatus.CREATED)
public ContractResponse createContract(@RequestBody CreateContractRequest request){
    return contractService.createContract(request);
}
@GetMapping("/list")
@Operation(summary = "Get all contracts", description = "Retrieves a list of all contracts.")
@ResponseStatus(HttpStatus.OK)
public List<ContractResponse> getAllContract()
{
    return contractService.getAllContract();
}

}
