package edu.mcw.rgd.datamodel.phenominerExpectedRange;

import edu.mcw.rgd.datamodel.ontologyx.Term;

import java.io.Serializable;

/**
 * Created by jthota on 4/30/2018.
 */
public class TraitObject implements Serializable {
    private Term trait;
    private Term subTrait;
    private Term subSubTrait;
    private int expectedRangeId;

    public Term getSubSubTrait() {
        return subSubTrait;
    }

    public void setSubSubTrait(Term subSubTrait) {
        this.subSubTrait = subSubTrait;
    }

    public Term getTrait() {
        return trait;
    }

    public void setTrait(Term trait) {
        this.trait = trait;
    }

    public Term getSubTrait() {
        return subTrait;
    }

    public void setSubTrait(Term subTrait) {
        this.subTrait = subTrait;
    }

    public int getExpectedRangeId() {
        return expectedRangeId;
    }

    public void setExpectedRangeId(int expectedRangeId) {
        this.expectedRangeId = expectedRangeId;
    }
}

