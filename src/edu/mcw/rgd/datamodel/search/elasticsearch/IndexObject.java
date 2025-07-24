package edu.mcw.rgd.datamodel.search.elasticsearch;

import java.util.List;
import java.util.Set;

public class IndexObject {
    private String term_acc;
    private String status;
    private String symbol;
    private List<String> oldSymbols;

    private String name;
    private List<String> oldNames;
    private String htmlStrippedSymbol;
    private String description;
    private List<String> xdbIdentifiers;
    private List<String> xdbNames;
    private String species;
    private String type;
    private String category;
    private String source;
    private String origin;
    private List<String> transcriptIds;
    private List<String> protein_acc_ids;
    private List<String> synonyms;
    private String trait;
    private String subTrait;

    private List<MapInfo> mapDataList;
    private String chromosome;
    private String map;
    private long startPos;
    private long stopPos;
    private int rank;
    private int mapKey;
    private List<String> goAnnotations;
    private boolean withSSLPS;
    private boolean withHomologs;
    private String regionNameStr;
    private String regionNameLcStr;
    private Set<String>  analysisNames;
    private String varNuc;
    private String refNuc;
    private Set<String> lifeStage;
    private Set<String> sex;
    private Set<String> expressionLevel;
    private Set<String>expressionUnit;

    public String getTerm_acc() {
        return term_acc;
    }

    public void setTerm_acc(String term_acc) {
        this.term_acc = term_acc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<String> getOldSymbols() {
        return oldSymbols;
    }

    public void setOldSymbols(List<String> oldSymbols) {
        this.oldSymbols = oldSymbols;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getOldNames() {
        return oldNames;
    }

    public void setOldNames(List<String> oldNames) {
        this.oldNames = oldNames;
    }

    public String getHtmlStrippedSymbol() {
        return htmlStrippedSymbol;
    }

    public void setHtmlStrippedSymbol(String htmlStrippedSymbol) {
        this.htmlStrippedSymbol = htmlStrippedSymbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getXdbIdentifiers() {
        return xdbIdentifiers;
    }

    public void setXdbIdentifiers(List<String> xdbIdentifiers) {
        this.xdbIdentifiers = xdbIdentifiers;
    }

    public List<String> getXdbNames() {
        return xdbNames;
    }

    public void setXdbNames(List<String> xdbNames) {
        this.xdbNames = xdbNames;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getTranscriptIds() {
        return transcriptIds;
    }

    public void setTranscriptIds(List<String> transcriptIds) {
        this.transcriptIds = transcriptIds;
    }

    public List<String> getProtein_acc_ids() {
        return protein_acc_ids;
    }

    public void setProtein_acc_ids(List<String> protein_acc_ids) {
        this.protein_acc_ids = protein_acc_ids;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public String getSubTrait() {
        return subTrait;
    }

    public void setSubTrait(String subTrait) {
        this.subTrait = subTrait;
    }

    public List<MapInfo> getMapDataList() {
        return mapDataList;
    }

    public void setMapDataList(List<MapInfo> mapDataList) {
        this.mapDataList = mapDataList;
    }

    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public long getStartPos() {
        return startPos;
    }

    public void setStartPos(long startPos) {
        this.startPos = startPos;
    }

    public long getStopPos() {
        return stopPos;
    }

    public void setStopPos(long stopPos) {
        this.stopPos = stopPos;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getMapKey() {
        return mapKey;
    }

    public void setMapKey(int mapKey) {
        this.mapKey = mapKey;
    }

    public List<String> getGoAnnotations() {
        return goAnnotations;
    }

    public void setGoAnnotations(List<String> goAnnotations) {
        this.goAnnotations = goAnnotations;
    }

    public boolean isWithSSLPS() {
        return withSSLPS;
    }

    public void setWithSSLPS(boolean withSSLPS) {
        this.withSSLPS = withSSLPS;
    }

    public boolean isWithHomologs() {
        return withHomologs;
    }

    public void setWithHomologs(boolean withHomologs) {
        this.withHomologs = withHomologs;
    }

    public String getRegionNameStr() {
        return regionNameStr;
    }

    public void setRegionNameStr(String regionNameStr) {
        this.regionNameStr = regionNameStr;
    }

    public String getRegionNameLcStr() {
        return regionNameLcStr;
    }

    public void setRegionNameLcStr(String regionNameLcStr) {
        this.regionNameLcStr = regionNameLcStr;
    }

    public Set<String> getAnalysisNames() {
        return analysisNames;
    }

    public void setAnalysisNames(Set<String> analysisNames) {
        this.analysisNames = analysisNames;
    }

    public String getVarNuc() {
        return varNuc;
    }

    public void setVarNuc(String varNuc) {
        this.varNuc = varNuc;
    }

    public String getRefNuc() {
        return refNuc;
    }

    public void setRefNuc(String refNuc) {
        this.refNuc = refNuc;
    }

    public Set<String> getLifeStage() {
        return lifeStage;
    }

    public void setLifeStage(Set<String> lifeStage) {
        this.lifeStage = lifeStage;
    }

    public Set<String> getSex() {
        return sex;
    }

    public void setSex(Set<String> sex) {
        this.sex = sex;
    }

    public Set<String> getExpressionLevel() {
        return expressionLevel;
    }

    public void setExpressionLevel(Set<String> expressionLevel) {
        this.expressionLevel = expressionLevel;
    }

    public Set<String> getExpressionUnit() {
        return expressionUnit;
    }

    public void setExpressionUnit(Set<String> expressionUnit) {
        this.expressionUnit = expressionUnit;
    }
}
