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
import org.springframework.security.core.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
@SecurityRequirement(name = "bearerAuth")
@ResponseStatus(HttpStatus.CREATED)
public ContractResponse createContract(
        @RequestBody CreateContractRequest request,
        Authentication authentication
){
    return contractService.createContract(request, authentication.getName());
}
@GetMapping("/list")
@SecurityRequirement(name = "bearerAuth")
@Operation(summary = "Get all contracts", description = "Retrieves a list of all contracts.")
@ResponseStatus(HttpStatus.OK)
public List<ContractResponse> getAllContract(Authentication authentication)
{
    return contractService.getAllContract(authentication.getName());
}
@PatchMapping("/update/{contractId}")
@Operation (summary = "Update a contract", description = "Updates an existing contract with the provided details.")
@ResponseStatus(HttpStatus.OK)
@SecurityRequirement(name = "bearerAuth")
public ContractResponse updateContract(
        @PathVariable("contractId") UUID contractId,
        @RequestBody UpdateContractRequest request,
        Authentication authentication
)
{
    return contractService.updateContract(
            contractId,
            request,
            authentication.getName()
    );
}
@DeleteMapping ("/delete/{contractId}")
@SecurityRequirement(name = "bearerAuth")
@Operation (summary = "Delete a contract", description = "Deletes an existing contract by its ID.")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteContract(
        @PathVariable("contractId") UUID contractId,
        Authentication authentication
)
{
    contractService.deleteContract(contractId, authentication.getName());
}

}
