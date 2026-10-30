package com.aquaLex.contract.infrastructure;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.aquaLex.contract.domain.Contract;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findAllByUser_Email(String email);

    Optional<Contract> findByIdAndUser_Email(UUID id, String email);
}
