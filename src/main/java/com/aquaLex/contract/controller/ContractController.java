package com.aquaLex.contract.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import com.aquaLex.contract.application.ContractService;
import com.aquaLex.contract.dto.ContractResponse;
import com.aquaLex.contract.dto.CreateContractRequest;
import com.aquaLex.contract.dto.UpdateContractRequest;
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
@PatchMapping("/update/{contractId}")
@Operation (summary = "Update a contract", description = "Updates an existing contract with the provided details.")
@ResponseStatus(HttpStatus.OK)
public ContractResponse updateContract(@PathVariable("contractId") UUID contractId, @RequestBody UpdateContractRequest request)
{
    return contractService.updateContract(contractId, request);
}
@DeleteMapping ("/delete/{contractId}")
@Operation (summary = "Delete a contract", description = "Deletes an existing contract by its ID.")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteContract(@PathVariable ("contractId") UUID contractId)
{
    contractService.deleteContract(contractId);
}

}
