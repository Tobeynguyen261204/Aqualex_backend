package com.aquaLex.ai.contract.application;

import org.springframework.stereotype.Service;

@Service
public class MockContractRevisionService implements ContractRevisionService {

    @Override
    public String revise(String draftContent, String instruction) {
        return draftContent
                + "\n\n[Mock revision applied]\n"
                + "User instruction: "
                + instruction.trim();
    }
}
