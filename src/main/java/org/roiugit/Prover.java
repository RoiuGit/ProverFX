package org.roiugit;

import org.roiugit.formula.Formula;
import org.roiugit.naturaldeduction.NaturalDeduction;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


public class Prover {
    private final String path = "C:\\Users\\puzik\\Documents\\myProofs\\";
    private final Scanner scn = new Scanner(System.in);
    private final NaturalDeduction nd = new NaturalDeduction();

    private List<Formula> promptForPremises() {
        List<Formula> premises = new ArrayList<>();
        int numPremises;
        while (true) {
            try {
                // Prompt user to enter number of initial premises.
                System.out.print("Enter number of premises: ");
                numPremises = Integer.parseInt(scn.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
        //
        for (int i = 0; i < numPremises; i++) {
            try {
                // Prompt user to enter a premise numPremises-number of times.
                System.out.print("Enter premise " + (i + 1) + ": ");
                premises.add(new Formula(scn.nextLine()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid formula. Please try again.");
                i--;
            }
        }
        return premises;
    }

    private void promptForAssumption() {
        while (true) {
            System.out.print("Enter the assumption: ");
            try {
                nd.assume(new Formula(scn.nextLine()));
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid formula. Please try again.");
            }
        }
    }

    private void promptForSave() {
        while (true) {
            System.out.print("Enter the filename: ");
            try {
                nd.toFile(path, scn.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Error saving file. Try again.");
            }
        }
    }

    private boolean promptForLoad() {
        while (true) {
            System.out.print("Do you want to load an existing proof? (Y/N): ");
            String input = scn.nextLine();
            if (input.equalsIgnoreCase("Y")) {
                File dir = new File(path);
                System.out.println("Choose a file to load:");
                List<File> fileList = Arrays.stream(Objects.requireNonNull(dir.listFiles())).toList();
                if (!fileList.isEmpty()) {
                    for (int i = 0; i < fileList.size(); i++) {
                        File file = fileList.get(i);
                        System.out.printf("%d. %s%n", i + 1, file.getName().split("\\.")[0]);
                    }
                    try {
                        nd.fromFile(path, fileList.get(Integer.parseInt(scn.nextLine()) - 1).getName().split("\\.")[0]);
                        return true;
                    } catch (Exception e) {
                        System.out.println("Error loading file. Try again.");
                    }
                } else {
                    System.out.println("There are no files to read.");
                    return false;
                }
            } else if (input.equalsIgnoreCase("N")) {
                return false;
            }
        }
    }

    private void endProofSession() {
        nd.printProof();
        String result = nd.getResult();
        if (result != null) {
            System.out.println(result);
            nd.close();
            while (true) {
                System.out.print("Save the proof? (Y/N): ");
                String input = scn.nextLine();
                if (input.equalsIgnoreCase("Y")) {
                    promptForSave();
                    break;
                } else if (input.equalsIgnoreCase("N")) break;
            }
        } else {
            System.out.println("Could not end the proof. All of the subproofs must be closed.");
        }
    }

    private String promptForRule() {
        System.out.print("""
                Enter:
                -the rule you want to apply,
                -"ASSUME" to add an assumption,
                -"END" to end the proof and exit,
                -"SAVE" to save the unfinished proof and exit:
                """);
        return scn.nextLine().toUpperCase();
    }

    public void applyRule(String rulePrompt) {
        String rule = rulePrompt.substring(0, rulePrompt.indexOf(':'));
        List<Integer> indexes = Arrays.stream(rulePrompt.substring(rulePrompt.indexOf(':') + 1).split(","))
                .map(index -> Integer.parseInt(index.strip())).toList();
        nd.applyRule(rule, indexes);
        }


    public void makeProofDir() {
        //noinspection ResultOfMethodCallIgnored
        new File(path).mkdirs();
    }

    public List<RuleInfo> getRuleInfo() {
        return nd.getRuleList().stream()
                .map(rule -> new RuleInfo(rule.getRuleName(), rule.getSchema()))
                .collect(Collectors.toList());
    }
    public void setMainProof(List<String> expressions){
        nd.setMainProof(expressions.stream().map(Formula::new).collect(Collectors.toList()));
    }
    public String getMainProof(){
        return nd.getMainProof().toString();
    }
}