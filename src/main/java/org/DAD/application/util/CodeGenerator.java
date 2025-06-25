package org.DAD.application.util;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CodeGenerator {
    private final Set<Integer> activeCodes = ConcurrentHashMap.newKeySet();
    private Integer lastCode = 0;
    private final Random random = new Random();

    public String generateUniqueCode() {

        if(lastCode >= 1000000){
            for(int i = 0; i < 1000000; i++){
                if(!activeCodes.contains(i)){
                    lastCode = i;
                }
            }
        }

        Integer currentCode = lastCode;
        lastCode++;
        String check = String.format("%05x", currentCode);
        Integer backInt = Integer.parseInt(check, 16);

        return String.format("%05x", currentCode);

    }

    public void releaseCode(String code) {
        Integer integerCode = Integer.parseInt(code, 16);
        activeCodes.remove(integerCode);
    }

    public boolean isCodeActive(String code) {
        Integer integerCode = Integer.parseInt(code, 16);
        return activeCodes.contains(integerCode);
    }
}
