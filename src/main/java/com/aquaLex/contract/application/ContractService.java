package com.aquaLex.contract.application;

import com.aquaLex.common.exception.ContractNotFoundException;
import com.aquaLex.contract.domain.Contract;
import com.aquaLex.contract.domain.User;
import com.aquaLex.contract.dto.CreateContractRequest;
import com.aquaLex.contract.dto.UpdateContractRequest;
import com.aquaLex.contract.dto.ContractResponse;
import com.aquaLex.contract.infrastructure.ContractRepository;
import com.aquaLex.contract.infrastructure.UserRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    public ContractService(
            ContractRepository contractRepository,
            UserRepository userRepository
    ) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
    }

    public ContractResponse createContract(
            CreateContractRequest request,
            String userEmail
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

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
                request.arrivalPort(),
                user
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
    public List<ContractResponse> getAllContract(String userEmail)
    {
        return contractRepository.findAllByUser_Email(userEmail)
                .stream()
                .map(this::toResponse)
                .toList();
        
    }
    public ContractResponse getContractById(UUID contractId, String userEmail)
    {
        Contract contract = findUserContract(contractId, userEmail);
        return toResponse(contract);
    }
    public ContractResponse updateContract(
            UUID contractId,
            UpdateContractRequest request,
            String userEmail
    )
    {
        Contract contract = findUserContract(contractId, userEmail);
        contract.update(
        request.exporterName(), request.importerName(), request.productName(), request.quantity(), request.unit(), request.currency(), request.unitPrice(), request.incoterm(), request.departurePort(), request.arrivalPort());
        Contract updatedContract = contractRepository.save(contract);
        return toResponse(updatedContract);
    }
    public ContractResponse deleteContract(UUID contractId, String userEmail)
    {
        Contract contract = findUserContract(contractId, userEmail);
        contractRepository.delete(contract);
        return toResponse(contract);
    }

    private Contract findUserContract(UUID contractId, String userEmail) {
        return contractRepository.findByIdAndUser_Email(contractId, userEmail)
                .orElseThrow(() -> new ContractNotFoundException(contractId));
    }
}
