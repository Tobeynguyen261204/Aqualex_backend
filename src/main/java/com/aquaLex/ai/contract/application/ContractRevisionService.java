package com.aquaLex.ai.contract.application;

public interface ContractRevisionService {

    String revise(String draftContent, String instruction);
}
