package edu.mcw.rgd.datamodel.ontologyx;

/**
 * Created by IntelliJ IDEA.
 * User: MTUTAJ
 * Date: Mar 24, 2011
 * Time: 4:00:09 PM
 *
 * relation between two ontology terms
 * <p>
 * Represents an ontology relationship -- duplicates the contents of ONT_RELATIONSHIPS table
 */
public enum Relation {

    NOT_SPECIFIED, // not specified
    IS_A,
    PART_OF,
    REGULATES,
    POSITIVELY_REGULATES,
    NEGATIVELY_REGULATES,
    // added to GO: biological process in 2013
    STARTS_DURING,
    HAPPENS_DURING,
    ENDS_DURING,
    OCCURS_IN,
    // below: introduced in sequence ontology
    VARIANT_OF,
    GUIDED_BY,
    OVERLAPS,
    DERIVES_FROM,
    NON_FUNCTIONAL_HOMOLOG_OF,
    HAS_PART,
    MEMBER_OF,
    CONTAINS,
    ADJACENT_TO,
    DEVELOPS_FROM,
    HAS_COMPONENT, // XCO
    HAS_FUNCTIONAL_PARENT, // CHEBI ontology
    HAS_PARENT_HYBRIDE, // CHEBI ontology
    IS_SUBSTITUENT_GROUP_FOR, // CHEBI ontology
    HAS_ROLE; // CHEBI ontology

    /**
     * convert String stored in db to enum
     */
    static public Relation getRelFromRelId(String relId) {
        if( relId==null )
            return NOT_SPECIFIED;
        switch(relId) {
            case "NS": return NOT_SPECIFIED;
            case "IA": return IS_A;
            case "PO": return PART_OF;

            case "R":  return REGULATES;
            case "PR": return POSITIVELY_REGULATES;
            case "NR": return NEGATIVELY_REGULATES;

            case "SD": return STARTS_DURING;
            case "HD": return HAPPENS_DURING;
            case "ED": return ENDS_DURING;
            case "OI": return OCCURS_IN;

            case "VO": return VARIANT_OF;
            case "DF": return DERIVES_FROM;
            case "NF": return NON_FUNCTIONAL_HOMOLOG_OF;
            case "HP": return HAS_PART;
            case "MO": return MEMBER_OF;
            case "AT": return ADJACENT_TO;
            case "GB": return GUIDED_BY;
            case "C":  return CONTAINS;
            case "O":  return OVERLAPS;
            case "DE": return DEVELOPS_FROM;
            case "HR": return HAS_ROLE;
            case "HC": return HAS_COMPONENT;
            case "FP": return HAS_FUNCTIONAL_PARENT;
            case "PH": return HAS_PARENT_HYBRIDE;
            case "SG": return IS_SUBSTITUENT_GROUP_FOR;
            default:   return NOT_SPECIFIED;
        }
    }

    /**
     * convert enum to String stored in db
     */
    static public String getRelIdFromRel(Relation rel) {
        switch( rel ) {
            case IS_A: return "IA";
            case PART_OF: return "PO";
            case REGULATES: return "R";
            case POSITIVELY_REGULATES: return "PR";
            case NEGATIVELY_REGULATES: return "NR";

            case STARTS_DURING: return "SD";
            case HAPPENS_DURING: return "HD";
            case ENDS_DURING: return "ED";
            case OCCURS_IN: return "OI";

            case VARIANT_OF: return "VO";
            case GUIDED_BY: return "GB";
            case OVERLAPS: return "O";
            case DERIVES_FROM: return "DF";
            case NON_FUNCTIONAL_HOMOLOG_OF: return "NF";
            case HAS_PART: return "HP";
            case MEMBER_OF: return "MO";
            case CONTAINS: return "C";
            case ADJACENT_TO: return "AT";
            case DEVELOPS_FROM: return "DE";
            case HAS_ROLE: return "HR";
            case HAS_COMPONENT: return "HC";
            case HAS_FUNCTIONAL_PARENT: return "FP";
            case HAS_PARENT_HYBRIDE: return "PH";
            case IS_SUBSTITUENT_GROUP_FOR: return "SG";
            default:
            case NOT_SPECIFIED: return "NS";
        }
    }

    /**
     * shows human readable description of the relation
     */
    @Override
    public String toString() {
        switch( this ) {
            case IS_A: return "is-a";
            case PART_OF: return "part-of";
            case REGULATES: return "regulates";
            case POSITIVELY_REGULATES: return "positively-regulates";
            case NEGATIVELY_REGULATES: return "negatively-regulates";

            case STARTS_DURING: return "starts-during";
            case HAPPENS_DURING: return "happens-during";
            case ENDS_DURING: return "ends-during";
            case OCCURS_IN: return "occurs-in";

            case VARIANT_OF: return "variant-of";
            case GUIDED_BY: return "guided-by";
            case OVERLAPS: return "overlaps";
            case DERIVES_FROM: return "derives-from";
            case NON_FUNCTIONAL_HOMOLOG_OF: return "non-functional-homolog-of";
            case HAS_PART: return "has-part";
            case MEMBER_OF: return "member-of";
            case CONTAINS: return "contains";
            case ADJACENT_TO: return "adjacent-to";
            case DEVELOPS_FROM: return "develops-from";
            case HAS_ROLE: return "has-role";
            case HAS_COMPONENT: return "has-component";
            case HAS_FUNCTIONAL_PARENT: return "has-functional-parent";
            case HAS_PARENT_HYBRIDE: return "has-parent-hybride";
            case IS_SUBSTITUENT_GROUP_FOR: return "is-substituent-group-for";
            default:
            case NOT_SPECIFIED: return "n/a";
        }
    }
}
