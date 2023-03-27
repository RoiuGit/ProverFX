package org.roiugit.deduction;

import org.roiugit.rules.*;

public class NaturalDeduction extends DeductionSystem {
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


}