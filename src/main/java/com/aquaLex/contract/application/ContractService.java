package com.aquaLex.contract.application;

import com.aquaLex.contract.domain.Contract;
import com.aquaLex.contract.dto.CreateContractRequest;
import com.aquaLex.contract.dto.UpdateContractRequest;
import com.aquaLex.contract.dto.ContractResponse;
import com.aquaLex.contract.infrastructure.ContractRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public ContractResponse createContract(CreateContractRequest request) {
        Contract contract = new Contract(
                request.exporterName(),
                request.importerName(),
                request.productName(),
                request.quantity(),
                request.unit(),
                request.currency(),
                request.unitPrice(),
                request.incoterm(),
                request.departurePort(),
                request.arrivalPort()
        );

        Contract savedContract = contractRepository.save(contract);
        return toResponse(savedContract);
    }

    private ContractResponse toResponse(Contract contract) {
        return new ContractResponse(
                contract.getId(),
                contract.getExporterName(),
                contract.getImporterName(),
                contract.getProductName(),
                contract.getQuantity(),
                contract.getUnit(),
                contract.getCurrency(),
                contract.getUnitPrice(),
                contract.getIncoterm(),
                contract.getDeparturePort(),
                contract.getArrivalPort(),
                contract.getStatus(),
                contract.getCreatedAt()
        );
    }
    public List<ContractResponse> getAllContract()
    {
        return contractRepository.findAll().stream().map(this::toResponse).toList();
        
    }
    public ContractResponse getContractById(UUID contractId)
    {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new RuntimeException("Contract not found"));
        return toResponse(contract);
    }
    public ContractResponse updateContract(UUID contractId, UpdateContractRequest request)
    {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new RuntimeException("Contract not found"));
        contract.update(
        request.exporterName(), request.importerName(), request.productName(), request.quantity(), request.unit(), request.currency(), request.unitPrice(), request.incoterm(), request.departurePort(), request.arrivalPort());
        Contract updatedContract = contractRepository.save(contract);
        return toResponse(updatedContract);
    }
    public ContractResponse deleteContract(UUID contractId)
    {
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new RuntimeException("Contract not found"));
        contractRepository.delete(contract);
        return toResponse(contract);
    }
}
