package com.SoftwareMatrix.metrics;

import com.SoftwareMatrix.ParseAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;

public class NMIMetric extends Metric {
    NMIMetric(String name, double minVal, double maxVal) {
        super(name, minVal, maxVal);
    }

    NMIMetric(String name) {
        super(name);
    }

    @Override
    public double calculate(Project project, PsiClass target) {
        int cnt=0;
        for(PsiMethod m : target.getAllMethods()) {
            if(!m.hasModifierProperty(PsiModifier.PRIVATE))
                cnt++;
        }
        return (double)cnt;
    }
}
