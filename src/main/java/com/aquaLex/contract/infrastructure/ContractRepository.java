package com.aquaLex.contract.infrastructure;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aquaLex.contract.domain.Contract;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
    
}
