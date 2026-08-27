package com.nexusverify.checker;

import com.nexusverify.model.VerificationResult;

public interface VerificationChecker {

    VerificationResult check(String input);

}