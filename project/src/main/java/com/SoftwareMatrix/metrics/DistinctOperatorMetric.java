package com.SoftwareMatrix.metrics;

import com.SoftwareMatrix.ParseAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;

import java.util.HashSet;
import java.util.Set;

public class DistinctOperatorMetric extends Metric {
    public DistinctOperatorMetric(String name, double minVal, double maxVal) {
        super(name, minVal, maxVal);
    }
    public DistinctOperatorMetric(String name) {
        super(name);
    }

    @Override
    public double calculate(Project project, PsiClass target) {
        Set<PsiElement> operators;
        Set<String> distinctOperators = new HashSet<>();

        if(target == null)
            return lastResult;

        operators = ParseAdapter.getOperators(target);
        for (PsiElement value : operators) {
            distinctOperators.add(value.toString());
        }

        lastResult = distinctOperators.size();
        return lastResult;
    }
}
