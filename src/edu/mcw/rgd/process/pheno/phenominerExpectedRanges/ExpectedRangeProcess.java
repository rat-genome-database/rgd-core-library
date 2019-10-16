package edu.mcw.rgd.process.pheno.phenominerExpectedRanges;

import edu.mcw.rgd.dao.impl.OntologyXDAO;
import edu.mcw.rgd.dao.impl.PhenominerDAO;
import edu.mcw.rgd.dao.impl.PhenominerExpectedRangeDao;
import edu.mcw.rgd.dao.impl.PhenominerStrainGroupDao;
import edu.mcw.rgd.datamodel.ontologyx.Relation;
import edu.mcw.rgd.datamodel.ontologyx.Term;
import edu.mcw.rgd.datamodel.pheno.Condition;
import edu.mcw.rgd.datamodel.pheno.Experiment;
import edu.mcw.rgd.datamodel.pheno.Record;
import edu.mcw.rgd.datamodel.phenominerExpectedRange.*;
import edu.mcw.rgd.process.Utils;

import java.util.*;

/**
 * Created by jthota on 5/29/2018.
 */
public class ExpectedRangeProcess extends OntologyXDAO{


        PhenominerStrainGroupDao sdao= new PhenominerStrainGroupDao();
        PhenominerDAO pdao= new PhenominerDAO();
        PhenominerExpectedRangeDao dao=new PhenominerExpectedRangeDao();
        public String getClinicalMeasurement(List<PhenominerExpectedRange> records){
            return records.get(0).getClinicalMeasurement();
        }
        public PhenominerExpectedRange getNormalRange(List<PhenominerExpectedRange> records, String sex){

            for(PhenominerExpectedRange r:records){

                if(r.getExpectedRangeName().toLowerCase().contains("normalstrain")){

                    if(r.getSex().equalsIgnoreCase(sex) && r.getAgeLowBound()==0 && r.getAgeHighBound()==999){
                        return r;
                    }

                }


            }
            return null;
        }
    public PhenominerExpectedRange getNormalRange1(List<PhenominerExpectedRange> records) throws Exception {
            String phenotypeAccId= records.get(0).getClinicalMeasurementOntId();
            List<PhenominerExpectedRange> normalRecords=dao.getNormalRangeRecordUnstratified(phenotypeAccId);
            if(normalRecords!=null && normalRecords.size()>0){
                return normalRecords.get(0);
            }
        return null;
    }
        public String getStrainGroupName(int strainGroupId) throws Exception {
            return  sdao.getStrainGroupName(strainGroupId);
        }
        public List<PhenominerExpectedRange>  getExpectedRangesByTraitNStrainGroupId(int strainGroupId, String traitOntId) throws Exception {
            boolean isPGA=false;
            if(traitOntId!=null && traitOntId.equals("pga")){
                isPGA=true;
            }
            return dao.getExPectedRangesByTraitNStrainGroupId(traitOntId, strainGroupId, isPGA);
        }
        public int getRecordCountByStrain(List<PhenominerExpectedRange> records) throws Exception {
            Set<Integer> groupIds= new HashSet<>();
            for(PhenominerExpectedRange r: records) {
                String strainGroupName= sdao.getStrainGroupName(r.getStrainGroupId());
                if (!strainGroupName.toLowerCase().contains("normalstrain")) {
                    int strainGroupId = r.getStrainGroupId();
                    groupIds.add(strainGroupId);
                }
            }
            return groupIds.size();

        }

        public int getRecordCountBySex(List<PhenominerExpectedRange> records) throws Exception {
            int i=0;

            Set<Integer> strainGroupIds= new HashSet<>();
            for(PhenominerExpectedRange record:records){
                String strainGroupName=sdao.getStrainGroupName(record.getStrainGroupId());
                if(!strainGroupName.toLowerCase().contains("normalstrain")){
                    strainGroupIds.add(record.getStrainGroupId());
                }

            }
            nextId:   for(int id:strainGroupIds){
                int count=0;
                boolean maleFlag= false;
                boolean femaleFlag=false;
                boolean mixedFlag=false;
                for(PhenominerExpectedRange r:records){
                    if(r.getStrainGroupId()==id){
                        if(r.getSex().equalsIgnoreCase("male") || r.getSex().equalsIgnoreCase("female")|| r.getSex().equalsIgnoreCase("mixed")) {
                            if (!maleFlag) {
                                if (r.getSex().equalsIgnoreCase("male")) {
                                    count++;
                                    maleFlag = true;
                                }
                            }
                            if (!femaleFlag) {
                                if (r.getSex().equalsIgnoreCase("female")) {
                                    count++;
                                    femaleFlag = true;
                                }
                            }
                            if (!mixedFlag) {
                                if (r.getSex().equalsIgnoreCase("mixed")) {
                                    count++;
                                    mixedFlag = true;
                                }
                            }

                        }
                        if(count>=2){
                            i++;
                            continue nextId;
                        }


                    }
                }
            }

            return i;
        }
        public int getRecordCountByAge(List<PhenominerExpectedRange> records) throws Exception {
            int i=0;
            Set<Integer> strainGroupIds= new HashSet<>();
            for(PhenominerExpectedRange record:records){
                String strainGroupName=sdao.getStrainGroupName(record.getStrainGroupId());
                if(!strainGroupName.toLowerCase().contains("normalstrain")){
                    strainGroupIds.add(record.getStrainGroupId());
                }

            }
            nextId:   for(int id:strainGroupIds) {
                int count=0;
                boolean age1=false;
                boolean age2=false;
                boolean age3=false;
                boolean age4=false;
                for (PhenominerExpectedRange record : records) {
                    if(record.getStrainGroupId()==id){

                        if ((((record.getAgeLowBound()) >= 0 && (record.getAgeHighBound()) <= 79)) ||
                                (((record.getAgeLowBound()) >= 80 && (record.getAgeHighBound()) <= 99)) ||
                                (((record.getAgeLowBound()) >= 100 && (record.getAgeHighBound()) <= 999)) ||
                                (((record.getAgeLowBound()) == 0 && (record.getAgeHighBound()) == 999))){
                            if(!age1) {
                                if ((((record.getAgeLowBound()) >= 0 && (record.getAgeHighBound()) <= 79))) {
                                    count++;
                                    age1 = true;
                                }
                            }
                            if(!age2) {
                                if ((((record.getAgeLowBound()) >= 80 && (record.getAgeHighBound()) <= 99))) {
                                    count++;
                                    age2 = true;
                                }
                            }
                            if(!age3) {
                                if ((((record.getAgeLowBound()) >=100 && (record.getAgeHighBound()) <= 999))) {
                                    count++;
                                    age3 = true;
                                }
                            }
                            if(!age4) {
                                if ((((record.getAgeLowBound()) == 0 && (record.getAgeHighBound()) == 999))) {
                                    count++;
                                    age4 = true;
                                }
                            }
                            if(count>=2){
                                i++;
                                continue nextId;
                            }

                        }

                    }

                }
            }
            return i;
        }


        public List<PlotValues> getPlotData(List<PhenominerExpectedRange> records, String recordType) throws Exception {
            List<PlotValues> plotData=new ArrayList<>();
            if(records!=null) {
                for (PhenominerExpectedRange r : records) {
                    PlotValues trace = new PlotValues();
                    int strainGroupId = r.getStrainGroupId();
                    String strainGroup = sdao.getStrainGroupName(strainGroupId);
                    if(!strainGroup.toLowerCase().contains("normalstrain")){
                        // strainGroup="Normal Strain";

                        List<Double> y = new ArrayList<>();

                        y.add(r.getRangeHigh());
                        //     y.add(r.getGroupSD());
                        y.add(r.getRangeValue());
                        y.add(r.getRangeLow());
                        trace.setY(y);
                        if(recordType.equalsIgnoreCase("phenotype")) {
                            String name=new String();
                            if(r.getSex().equals("Mixed")){
                                if(r.getAgeLowBound()==0 && r.getAgeHighBound()==999 && !r.getExpectedRangeName().contains("vascular")  && !r.getExpectedRangeName().contains("tail")){
                                    name=strainGroup;

                                }else{
                                    if(r.getExpectedRangeName().contains("vascular")  || r.getExpectedRangeName().contains("tail")){
                                        if(r.getExpectedRangeName().contains("vascular"))
                                            name=strainGroup+"_vascular";
                                        else
                                            name=strainGroup+"_tail";
                                    }
                                    if(r.getAgeLowBound()!=0 || r.getAgeHighBound()!=999)
                                        name=strainGroup+"_"+r.getAgeLowBound()+"-"+r.getAgeHighBound()+" days";
                                }
                            }else{
                                name=strainGroup+"_"+r.getSex();
                            }
                            trace.setName(name);
                        }
                        if(recordType.equalsIgnoreCase("strain")) {
                            //    trace.setName(r.getExpectedRangeName());
                            String name=new String();
                            if (r.getSex().equals("Mixed")) {
                                if (r.getAgeLowBound() == 0 && r.getAgeHighBound() == 999 && !r.getExpectedRangeName().contains("vascular") && !r.getExpectedRangeName().contains("tail")) {
                                    name = r.getClinicalMeasurement();

                                } else {
                                    if (r.getExpectedRangeName().contains("vascular") || r.getExpectedRangeName().contains("tail")) {
                                        if (r.getExpectedRangeName().contains("vascular"))
                                            name = r.getClinicalMeasurement() + "_vascular";
                                        else
                                            name = r.getClinicalMeasurement() + "_tail";
                                    }
                                    if (r.getAgeLowBound() != 0 || r.getAgeHighBound() != 999)
                                        name = r.getClinicalMeasurement() + "_" + r.getAgeLowBound() + "-" + r.getAgeHighBound() + " days";
                                }
                            } else {
                                name = r.getClinicalMeasurement() + "_" + r.getSex();
                            }

                            trace.setName(name);
                        }
                        trace.setType("box");

                        plotData.add(trace);
                    }
                }
            }
            return plotData;
        }

        public List<Term> getConditions(List<Record> records) throws Exception {
            List<Term> conditionTerms= new ArrayList<>();
            List<String> conditions= new ArrayList<>();
            for(Record r: records){
                if(r.getConditions()!=null) {
                    for (Condition c : r.getConditions()) {
                        if(!conditions.contains(c.getOntologyId())){
                            conditions.add(c.getOntologyId());
                            conditionTerms.add(getTerm(c.getOntologyId()));
                        }

                    }

                }

            }
            return conditionTerms;

        }
        public List<Term> getMethods(List<Record> records) throws Exception {
            List<Term> methodTerms= new ArrayList<>();
            List<String> methods= new ArrayList<>();
            for(Record r:records){
                if(!methods.contains(r.getMeasurementMethod().getAccId())){
                    methods.add(r.getMeasurementMethod().getAccId());
                    methodTerms.add(getTerm(r.getMeasurementMethod().getAccId()));
                }
            }
            return methodTerms;
        }

        public List<Term> getStrains(List<Record> records) throws Exception {
            List<Term> strainTerms= new ArrayList<>();
            List<String> strains= new ArrayList<>();
            for(Record r:records){
                String strain_ont_id=r.getSample().getStrainAccId();
                if(!strains.contains(strain_ont_id)){
                    strains.add(strain_ont_id);
                    strainTerms.add(getTerm(strain_ont_id));
                }
            }
            strainTerms.sort((o1, o2) -> Utils.stringsCompareToIgnoreCase(o1.getTerm(), o2.getTerm()));
            return strainTerms;
        }
        public Map<String,Integer> getStrainGroupMap(List<PhenominerExpectedRange> ranges) throws Exception {
            Map<String,Integer> strainGroupMap= new TreeMap<>();
            for(PhenominerExpectedRange range:ranges){
                int strainGroupId= range.getStrainGroupId();
                String strainGroupName= sdao.getStrainGroupName(strainGroupId);
                if(!sdao.getStrainGroupName(strainGroupId).contains("NormalStrain"))
                    strainGroupMap.put(strainGroupName,strainGroupId);
            }
            return strainGroupMap;
        }
        public Map<String, String> getTraitMap(List<Record> records) throws Exception {
            String trait= new String();
            String traitOntId= new String();
            Map<String, String> traitMap= new HashMap<>();
            for(Record r: records){
                Experiment experiment= pdao.getExperiment(r.getExperimentId());
                if(experiment!=null) {
                    if(experiment.getTraitOntId()!=null) {
                        //   System.out.println(getTerm(r.getClinicalMeasurement().getAccId()).getTerm()+"\t"+ getTerm(r.getMeasurementMethod().getAccId()).getTerm()+"\t"+ getTerm(experiment.getTraitOntId()).getTerm() + "\t" + getTerm(experiment.getTraitOntId()).getAccId());
                        trait=getTerm(experiment.getTraitOntId()).getTerm();
                        traitOntId=getTerm(experiment.getTraitOntId()).getAccId();
                        traitMap.put(traitOntId, trait);
                        break;
                    }
                }
            }
            return traitMap;
        }



        public boolean containsPhysiologyTrait(List<String> traits){
            List<String> physiologyTraits= new ArrayList<>(Arrays.asList("VT:2000009", "VT:2000000", "VT:0000183"));
            for(String t:traits){
                if(physiologyTraits.contains(t)){
                    return true;
                }
            }
            return false;
        }
        public List<String> getSelectedCondtions(String conditionsSelected){
            List<String> selectedConditions= new ArrayList<>();
            StringTokenizer conditionTokens= new StringTokenizer(conditionsSelected,",");
            while (conditionTokens.hasMoreTokens()){
                selectedConditions.add(conditionTokens.nextToken());
            }
            return selectedConditions;
        }
        public List<Integer> getSelectedStrainsGroupIds(String strainsSelected){
            List<Integer> selectedStrains= new ArrayList<>();
            StringTokenizer strainTokens= new StringTokenizer(strainsSelected,",");
            while (strainTokens.hasMoreTokens()){
                selectedStrains.add(Integer.parseInt(strainTokens.nextToken()));
            }
            return selectedStrains;
        }
        public List<String> getSelectedMethods(String methodsSelected){
            List<String> selectedMethods= new ArrayList<>();
            StringTokenizer methodTokens= new StringTokenizer(methodsSelected, ",");
            while(methodTokens.hasMoreTokens()){
                String selectedMethod=methodTokens.nextToken();
                if(!selectedMethods.contains(selectedMethod))
                    selectedMethods.add(selectedMethod);
            }
            return selectedMethods;
        }
        public List<String> getSelectedSex(String sexSelected){
            List<String> selectedSex= new ArrayList<>();
            StringTokenizer sexTokens= new StringTokenizer(sexSelected, ",");
            while(sexTokens.hasMoreTokens()){
                selectedSex.add(sexTokens.nextToken());
            }
            return selectedSex;
        }
        public List<Integer> getSelectedAge(String ageSelected, String lowOrHigh){
            List<Integer> selectedAgeLow= new ArrayList<>();
            List<Integer> selectedAgeHigh= new ArrayList<>();

            StringTokenizer ageTokens= new StringTokenizer(ageSelected, ",");

            while(ageTokens.hasMoreTokens()){
                String token=ageTokens.nextToken();
                String[] age= token.split("-");
                if(!age[0].trim().equals("0"))
                    selectedAgeLow.add(Integer.parseInt(age[0].trim()));
                else selectedAgeLow.add(0);
                selectedAgeHigh.add(Integer.parseInt(age[1].trim()));
            }
            if(lowOrHigh.equalsIgnoreCase("low"))
                return selectedAgeLow;
            else
                return selectedAgeHigh;

        }




        public Map<String, String> getTraits() throws Exception {

            Map<String, String> traitMap= new TreeMap<>();
            Map<String, Relation> descendants= getTermDescendants("VT:0015074");
            for(Map.Entry e: descendants.entrySet()){
                //  System.out.println(e.getKey()+"\t"+ getTerm(e.getKey().toString()).getTerm()+"\t"+ e.getValue());
                traitMap.put(getTerm(e.getKey().toString()).getTerm(),e.getKey().toString());
            }

            return traitMap;
        }
     /*   public List<String> getPhenotypesByAncestorTrait(String traitOntId) throws Exception {
            List<String> phenotypes=new ArrayList<>();
            if(traitOntId!=null){
                if(!traitOntId.equals("") && !traitOntId.equals("pga")){
                    // trait=xdao.getTerm(traitOntId).getTerm();
                    phenotypes= dao.getDistinctClinicalMeasurementOntIdsByAncestorTrait(traitOntId); //get list of phenotypes based on  selected trait facet
                }else{
                    if(traitOntId.equals("pga")){
                        phenotypes=dao.getDistinctPGAMeasurements(); //get list of phenotypes that has no VT terms assigned when selected facet PGA.
                        //     isPGA=true;
                    }else
                        phenotypes= dao.getDistinctPhenotypes(null); // get list of all phenotypes when selected trait is "".
                }
            }else{
                phenotypes= dao.getDistinctPhenotypes(null); // get list of all phenotypes when selected "All Traits" facet or  trait is null
            }
            return phenotypes;
        }
*/
     public List<String> getPhenotypesByAncestorTrait(String traitOntId) throws Exception {
         new ArrayList();
         List<String> phenotypes;
         if (traitOntId != null) {
             if (!traitOntId.equals("")) {
                 phenotypes = this.dao.getDistinctClinicalMeasurementOntIdsByAncestorTrait(traitOntId);
             } else {
                 phenotypes = this.dao.getDistinctPhenotypes((String) null);
             }
         } else {
             phenotypes = this.dao.getDistinctPhenotypes((String) null);
         }

         return phenotypes;
     }
        public Map<String, String> getDistinctExpectedRangeTraits() throws Exception {
            List<String> traits= dao.getDistinctTraits(); // gets list of all distinct trait_Ont_ids from PHENOMINER_RANGE_TRAIT
            Map<String, String> tMap=new TreeMap<>();
            for(String t:traits){
                tMap.put(getTerm(t).getTerm(), t);
            }
            return tMap;
        }
    public Map<Term, Integer> getAggregationByTraitCounts() throws Exception {
        List<String> traits = dao.getDistinctTraits(); // gets list of all distinct trait_Ont_ids from PHENOMINER_RANGE_TRAIT
        Map<Term, Integer> tMap = new TreeMap<Term, Integer>(new Comparator<Term>() {
            @Override
            public int compare(Term o1, Term o2) {
                return Utils.stringsCompareToIgnoreCase(o1.getTerm(), o2.getTerm());
            }
        });
        for (String t : traits) {
            int count= dao.getExpectedRangesByTrait(t);
            Term term= getTermByAccId(t);
            tMap.put(term, count);
            //System.out.println(getTerm(t).getTerm()+"\t"+ count);
        }
        return tMap;
    }
        public List<TraitObject> getPhenotypeTraitParents(String phenotype, String trait) throws Exception {
            return  dao.getDistinctPhenotypeTraitParents(phenotype, trait);

        }
   /*     public PhenotypeObject getOverAllObject(List<PhenominerExpectedRange> records, String p) throws Exception {

            PhenotypeObject overAllObj= new PhenotypeObject();
            overAllObj.setClinicalMeasurement(getClinicalMeasurement(records));
            overAllObj.setClinicalMeasurementOntId(p);
            PhenominerExpectedRange normalRecord =getNormalRange(records, "Mixed");
            if (normalRecord != null)
                overAllObj.setNormalRange(normalRecord.getRangeLow() + " - " + normalRecord.getRangeHigh());
            overAllObj.setRanges(records);
            overAllObj.setStrainSpecifiedRecordCount(getRecordCountByStrain(records));
            overAllObj.setSexSpecifiedRecordCount(getRecordCountBySex(records));
            overAllObj.setAgeSpecifiedRecordCount(getRecordCountByAge(records));
            return overAllObj;
        }
        public List<Term> getTraitTerms(List<PhenominerExpectedRange> records) throws Exception {
            List<Term> terms= new ArrayList<>();
            for(PhenominerExpectedRange r:records){
                String trait=r.getTraitOntId();
                if(!existInTerms(trait, terms)){
                    terms.add(getTerm(trait));
                }
            }
            return terms;
        }*/
        public boolean existInTerms(String traitOntId, List<Term> terms){
            for(Term t:terms){
                if(t.getAccId().equalsIgnoreCase(traitOntId)){
                    return true;
                }
            }
            return false;
        }
        public List<PhenominerExpectedRange> addExtraAttributes(List<PhenominerExpectedRange> records) throws Exception {
            for(PhenominerExpectedRange r:records) {
                if(!r.getExpectedRangeName().toLowerCase().contains("normalstrain")) {
                    List<Integer> experimentRecordIds = dao.getExperimentRecordIds(r.getExpectedRangeId());
                    List<Record> expRecords = pdao.getFullRecords(experimentRecordIds);
                    List<Term> conditions = getConditions(expRecords);
                    List<Term> methods = getMethods(expRecords);
                    List<Term> strains = getStrains(expRecords);
                    Map<String, String> traitMap = getTraitMap(expRecords);
                    r.setConditions(conditions);
                    r.setMethods(methods);
                    r.setStrains(strains);
                    for (Map.Entry e : traitMap.entrySet()) {
                        r.setTraitOntId((String) e.getKey());
                        r.setTrait((String) e.getValue());

                    }
                }
            }
            return records;
        }
        public List<String> getMethodOptions(List<PhenominerExpectedRange> records){
            List<String> methods= new ArrayList<>();
            for(PhenominerExpectedRange range:records){
                if(range.getExpectedRangeName().contains("vascular")){
                    if(!methods.contains("vascular")){
                        methods.add("vascular");

                    }
                    if(!methods.contains("mixed")){
                        methods.add("mixed");
                    }
                }
                if(range.getExpectedRangeName().contains("tail")){
                    if(!methods.contains("tail")){
                        methods.add("tail");

                    }
                    if(!methods.contains("mixed")){
                        methods.add("mixed");
                    }
                }
            }
            methods.sort(Utils::stringsCompareToIgnoreCase);
            return methods;
        }

     /*   public  Map<List<Term>, List<PhenotypeObject>> getOverAllObjectsNDistinctTraits(List<String> phenotypes, String traitOntId, boolean isPGA) throws Exception {
            List<Term> distinctTraits=new ArrayList<>();
            List<PhenotypeObject> overallObjects=new ArrayList<>();
            Map<List<Term>, List<PhenotypeObject>> objectsNDistinctTraits=new HashMap<>();
            for (String p : phenotypes) {

                List<TraitObject> phenoTraits = getPhenotypeTraitParents(p, traitOntId);
                if (phenoTraits.size() > 0) {
                    for (TraitObject t : phenoTraits) {
                        if (t != null) {
                            List<PhenominerExpectedRange> records = dao.getExpectedRangesByParentTrait(p, isPGA, t.getSubTrait().getAccId());
                            PhenotypeObject overAllObj = getOverAllObject(records, p);
                            overAllObj.setTraits(getTraitTerms(records));
                            overAllObj.setTraitAncestors(new ArrayList<>(Arrays.asList(t)));
                            if (isDistinctTrait(distinctTraits, t.getSubTrait()))
                                distinctTraits.add(t.getSubTrait());

                            overallObjects.add(overAllObj);

                        } else {
                            List<PhenominerExpectedRange> records = dao.getExpectedRanges(p, null, null, null, null, null, isPGA);
                            overallObjects.add(getOverAllObject(records, p));
                        }
                    }
                } else {
                    List<PhenominerExpectedRange> records = dao.getExpectedRanges(p, null, null, null, null, null, isPGA);
                    overallObjects.add(getOverAllObject(records, p));
                }
            }
            overallObjects.sort((o1, o2) -> Utils.stringsCompareToIgnoreCase(o1.getClinicalMeasurement(), o2.getClinicalMeasurement()));
            objectsNDistinctTraits.put(distinctTraits, overallObjects);
            return objectsNDistinctTraits;
        }


        public  Map<List<Term>, List<PhenotypeObject>> getOverAllObjectsNDistinctTraits(List<String> phenotypes, String traitOntId, boolean isPGA) throws Exception {
            List<Term> distinctTraits=new ArrayList<>();
            List<PhenotypeObject> overallObjects=new ArrayList<>();
            Map<List<Term>, List<PhenotypeObject>> objectsNDistinctTraits=new HashMap<>();

            for (String p : phenotypes) {

                List<TraitObject> phenoTraits = getPhenotypeTraitParents(p, traitOntId);
                if (phenoTraits.size() > 0) {

                    for (TraitObject t : phenoTraits) {
                        if (t != null) {
                            List<PhenominerExpectedRange> records = dao.getExpectedRangesByParentTrait(p, isPGA, t.getSubTrait().getAccId());
                            PhenotypeObject overAllObj = getOverAllObject(records, p);
                            overAllObj.setTraits(getTraitTerms(records));
                            overAllObj.setTraitAncestors(new ArrayList<>(Arrays.asList(t)));
                            if (isDistinctTrait(distinctTraits, t.getSubTrait()))
                                distinctTraits.add(t.getSubTrait());

                            overallObjects.add(overAllObj);

                        } else {
                            List<PhenominerExpectedRange> records = dao.getExpectedRanges(p, null, null, null, null, null, isPGA);
                        if(records.size()>0)
                            overallObjects.add(getOverAllObject(records, p));
                        }
                    }
                if(traitOntId==null || Objects.equals(traitOntId, "") || traitOntId.equalsIgnoreCase("pga")) {
                    List<PhenominerExpectedRange> records = this.dao.getExpectedRanges(p, (List) null, (List) null, (List) null, (List) null, (List) null, true);
                    if (records.size() > 0)
                        overallObjects.add(this.getOverAllObject(records, p));
                }
                } else {
                    List<PhenominerExpectedRange> records = dao.getExpectedRanges(p, null, null, null, null, null, isPGA);
                    overallObjects.add(getOverAllObject(records, p));
                }
            }


            overallObjects.sort((o1, o2) -> Utils.stringsCompareToIgnoreCase(o1.getClinicalMeasurement(), o2.getClinicalMeasurement()));
            objectsNDistinctTraits.put(distinctTraits, overallObjects);
            return objectsNDistinctTraits;
        }
*/

        public boolean isDistinctTrait(List<Term> distinctTraits, Term trait){
            boolean isDistinctTrait=true;
            if(trait!=null) {
                for (Term t : distinctTraits) {
                    if (trait.getAccId().equalsIgnoreCase(t.getAccId())) {
                        isDistinctTrait = false;
                    }
                }
            }
            return isDistinctTrait;
        }

        public List<StrainObject> getStrainGroups(String traitOntId) throws Exception {
            List<StrainObject> strainObjects= new ArrayList<>();
            PhenominerExpectedRangeDao dao= new PhenominerExpectedRangeDao();
            PhenominerStrainGroupDao sdao= new PhenominerStrainGroupDao();
            List<String> strainGroups= dao.getDistinctStrainGroups();
            /*********************************STRAIN GROUP OBJECTS**************************************************/
            for(String s: strainGroups){
                List<String> cmoIds;
                StrainObject object= new StrainObject();
                String strainGroupName=sdao.getStrainGroupName(Integer.parseInt(s));
                if(!strainGroupName.contains("Normal")) {
                    object.setStrainGroupId(s);
                    object.setStrainGroupName(strainGroupName);
                    if(traitOntId!=null && !Objects.equals(traitOntId, "")){
                        cmoIds= dao.getDistinctPhenotypesByTrait(s, traitOntId);
                    }else
                        cmoIds= dao.getDistinctPhenotypesByStrainGroupId(s);
                    object.setCmoAccIds(cmoIds);
                    if(cmoIds.size()>0)
                        strainObjects.add(object);
                }

            }
            strainObjects.sort((o1,o2)->Utils.stringsCompareToIgnoreCase(o1.getStrainGroupName(),o2.getStrainGroupName()));

            return strainObjects;

        }
        public  Map<Term, List<PhenotypeObject>> getTraitSubtraitMap(List<Term> distinctTraits, List<PhenotypeObject> overallObjects){
            Map<Term, List<PhenotypeObject>> traitSubtraitMap = new LinkedHashMap<>();
            for(Term t:distinctTraits){
                List<PhenotypeObject> subtraits= new ArrayList<>();

                for(PhenotypeObject o : overallObjects){
                    if(o.getTraitAncestors()!=null) {
                        if(t.getAccId()!=null) {
                            if (t.getAccId().equalsIgnoreCase(o.getTraitAncestors().get(0).getSubTrait().getAccId())) {
                                subtraits.add(o);
                            }
                        }
                    }
                 /*   if(t.getTerm().equalsIgnoreCase("pga")){
                        if(o.getTraits()==null){
                            subtraits.add(o);
                        }
                    }*/
                }
                traitSubtraitMap.put(t, subtraits);

            }
            return traitSubtraitMap;
        }
    public Map<List<Term>, List<PhenotypeObject>> getOverAllObjectsNDistinctTraits(List<String> phenotypes, String traitOntId, boolean selectBytrait) throws Exception {
        List<Term> distinctTraits = new ArrayList<>();
        List<PhenotypeObject> overallObjects = new ArrayList<>();
        Map<List<Term>, List<PhenotypeObject>> objectsNDistinctTraits = new HashMap<>();

        for (String p : phenotypes) {
            List<PhenominerExpectedRange> rangerecs = new ArrayList<>();
            List<TraitObject> phenoTraits = getPhenotypeTraitParents(p, traitOntId);
            if (phenoTraits.size() > 0) {

                for (TraitObject t : phenoTraits) {
                    if (t != null) {
                        rangerecs = dao.getExpectedRangesByParentTrait(p, selectBytrait, t.getSubTrait().getAccId());

                        PhenotypeObject overAllObj = getOverAllObject(rangerecs, p);
                        if (rangerecs.size() > 0)
                            overAllObj.setTraits(getTraitTerms(rangerecs));
                        overAllObj.setTraitAncestors(new ArrayList<>(Arrays.asList(t)));
                        if (isDistinctTrait(distinctTraits, t.getSubTrait()))
                            distinctTraits.add(t.getSubTrait());

                        overallObjects.add(overAllObj);

                    } else {
                        rangerecs = dao.getExpectedRanges(p, null, null, null, null, null, selectBytrait);
                        if (rangerecs.size() > 0)
                            overallObjects.add(getOverAllObject(rangerecs, p));
                    }
                }

            } else {
                rangerecs = dao.getExpectedRangesOfPhenotype(p, null, null, null, null, null, selectBytrait);
                overallObjects.add(getOverAllObject(rangerecs, p));
            }
        }
        overallObjects.sort((o1, o2) -> Utils.stringsCompareToIgnoreCase(o1.getClinicalMeasurement(), o2.getClinicalMeasurement()));
        objectsNDistinctTraits.put(distinctTraits, overallObjects);
        return objectsNDistinctTraits;

    }
   /* public Map<List<Term>, List<PhenotypeObject>> getOverAllObjectsNDistinctTraits(List<String> phenotypes, String traitOntId, boolean isPGA) throws Exception {
        List<Term> distinctTraits=new ArrayList<>();
        List<PhenotypeObject> overallObjects=new ArrayList<>();
        Map<List<Term>, List<PhenotypeObject>> objectsNDistinctTraits=new HashMap<>();

        for (String p : phenotypes) {
            List<PhenominerExpectedRange> records= new ArrayList<>();
            List<TraitObject> phenoTraits = getPhenotypeTraitParents(p, traitOntId);
            if (phenoTraits.size() > 0) {

                for (TraitObject t : phenoTraits) {
                    if (t != null) {
                        records = dao.getExpectedRangesByParentTrait(p, isPGA, t.getSubTrait().getAccId());
                        PhenotypeObject overAllObj = getOverAllObject(records, p);
                        if(records.size()>0)
                            overAllObj.setTraits(getTraitTerms(records));
                        overAllObj.setTraitAncestors(new ArrayList<>(Arrays.asList(t)));
                        if (isDistinctTrait(distinctTraits, t.getSubTrait()))
                            distinctTraits.add(t.getSubTrait());

                        overallObjects.add(overAllObj);

                    } else {
                        records = dao.getExpectedRanges(p, null, null, null, null, null, isPGA);
                        if(records.size()>0)
                            overallObjects.add(getOverAllObject(records, p));
    }
                }
                if(traitOntId==null || Objects.equals(traitOntId, "") || traitOntId.equalsIgnoreCase("pga")) {
                    records = this.dao.getExpectedRangesOfPhenotype(p, (List) null, (List) null, (List) null, (List) null, (List) null, true);
                    if (records.size() > 0)
                        overallObjects.add(this.getOverAllObject(records, p));
                }
            } else {
                records = dao.getExpectedRangesOfPhenotype(p, null, null, null, null, null, isPGA);
                overallObjects.add(getOverAllObject(records, p));
            }
        }


        overallObjects.sort((o1, o2) -> Utils.stringsCompareToIgnoreCase(o1.getClinicalMeasurement(), o2.getClinicalMeasurement()));
        objectsNDistinctTraits.put(distinctTraits, overallObjects);
        return objectsNDistinctTraits;
    }
*/
   public PhenotypeObject getOverAllObject(List<PhenominerExpectedRange> rangeRecs, String p) throws Exception {
       PhenotypeObject overAllObj = new PhenotypeObject();
       overAllObj.setClinicalMeasurement(this.getClinicalMeasurement(rangeRecs));
       overAllObj.setClinicalMeasurementOntId(p);
    //   PhenominerExpectedRange normalRecord = this.getNormalRange(rangeRecs, "Mixed");
       PhenominerExpectedRange normalRecord = this.getNormalRange1(rangeRecs);
       if(normalRecord != null) {
           overAllObj.setNormalRange(normalRecord.getRangeLow() + " - " + normalRecord.getRangeHigh());
       }

       overAllObj.setRanges(rangeRecs);
       overAllObj.setStrainSpecifiedRecordCount(this.getRecordCountByStrain(rangeRecs));
       overAllObj.setSexSpecifiedRecordCount(this.getRecordCountBySex(rangeRecs));
       overAllObj.setAgeSpecifiedRecordCount(this.getRecordCountByAge(rangeRecs));
       return overAllObj;
   }

    public List<Term> getTraitTerms(List<PhenominerExpectedRange> records) throws Exception {
        List<Term> terms= new ArrayList<>();
        for(PhenominerExpectedRange r:records){
            String trait=r.getTraitOntId();
            if(trait!=null)
                if(!existInTerms(trait, terms)){
                    terms.add(getTerm(trait));
                }
        }
        return terms;
    }
    }

