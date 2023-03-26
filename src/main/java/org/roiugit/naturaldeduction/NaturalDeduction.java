package org.roiugit.naturaldeduction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.roiugit.formula.Formula;
import org.roiugit.proof.Proof;
import org.roiugit.proof.ProofStep;
import org.roiugit.rules.*;
import org.roiugit.rules.gsonadapter.RuleTypeAdapter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NaturalDeduction {
    private final Map<String, Rule> ruleMap = new HashMap<>();
    private Proof mainProof;

    public NaturalDeduction() {
        putRule(new IERule());
        putRule(new IIRule());
        putRule(new CE1Rule());
        putRule(new CE2Rule());
        putRule(new CIRule());
        putRule(new NERule());
        putRule(new NIRule());
        putRule(new DERule());
        putRule(new DI1Rule());
        putRule(new DI2Rule());
    }

    private void putRule(Rule rule) {
        this.ruleMap.put(rule.getRuleName(), rule);
    }

    public void applyRule(String rule, List<Integer> premisesIndexes) {
        Rule ruleInstance = ruleMap.get(rule);
        List<Formula> premises = readPremises(premisesIndexes);
        Formula result = ruleInstance.applyRule(premises, mainProof);
        if (result != null) {
            mainProof.append(new ProofStep(mainProof.getEndingIndex() + 1, premisesIndexes, premises, result, ruleInstance));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void toFile(File file) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Rule.class, new RuleTypeAdapter().nullSafe())
                .setPrettyPrinting()
                .create();
        String json = gson.toJson(mainProof);
        try {
            FileWriter output = new FileWriter(file);
            output.write(json);
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fromFile(File file) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Rule.class, new RuleTypeAdapter().nullSafe())
                .create();
        FileReader input;
        try {
            input = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        mainProof = gson.fromJson(input, Proof.class);
        try {
            input.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Formula> readPremises(List<Integer> indexes) {
        return indexes.stream().map(index -> mainProof.getFormula(index - mainProof.getStartingIndex())).collect(Collectors.toList());
    }

    public void close() {
        mainProof.close();
    }

    public boolean isNotClosed() {
        return mainProof.isNotClosed();
    }

    public void assume(Formula assumption) {
        mainProof.assume(assumption);
    }

    public String getResult() {
        return mainProof.getResult();
    }

    public List<Rule> getRuleList() { //returns list of rules, sorted by name
        return ruleMap.keySet()
                .stream().sorted()
                .map(ruleMap::get).collect(Collectors.toList());
    }

    public Proof getMainProof() {
        return mainProof;
    }

    public void setMainProof(List<Formula> premises) {
        this.mainProof = new Proof(premises);
    }
    public void setMainProof(){
        this.mainProof = new Proof();
    }

    public int getStartingIndex() {
        return mainProof.getStartingIndex();
    }

    public int getEndingIndex() {
        return mainProof.getEndingIndex();
    }
}