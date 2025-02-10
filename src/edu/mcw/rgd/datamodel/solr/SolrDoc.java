package edu.mcw.rgd.datamodel.solr;



import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "j_date_s",
        "citation",
        "mesh_terms",
        "keywords",
        "chemicals",
        "affiliation",
        "issn",
        "organism_common_name",
        "organism_term",
        "organism_ncbi_id",
        "organism_count",
        "organism_pos",
        "pmc_id",
        /*******************************/
        "gene_count",
        "mp_id",
        "doi_s",
        "chebi_pos",
        "vt_id",
        "bp_term",
        "chebi_term",
        "p_date",
        "xco_term",
        "chebi_count",
        "rs_term",
        "mp_term",
        "rdo_id",
        "nbo_pos",
        "gene",
        "rs_id",
        "so_term",
        "mp_count",
        "vt_count",
        "bp_id",
        "rgd_obj_count",
        "vt_pos",
        "p_type",
        "nbo_count",
        "xco_id",
        "p_year",
        "authors",
        "xco_count",
        "rdo_count",
        "title",
        "nbo_term",
        "vt_term",
        "hp_pos",
        "nbo_id",
        "so_count",
        "hp_term",
        "so_id",
        "rgd_obj_pos",
        "xco_pos",
        "rs_pos",
        "hp_id",
        "rdo_pos",
        "rs_count",
        "rgd_obj_term",
        "abstract",
        "pmid",
        "bp_count",
        "mp_pos",
        "hp_count",
        "xdb_id",
        "rgd_obj_id",
        "bp_pos",
        "gene_pos",
        "so_pos",
        "rdo_term",
        "chebi_id"
})
@Generated("jsonschema2pojo")
public class SolrDoc {

    @JsonProperty("gene_count")
    private List<String> geneCount = new ArrayList<String>();
    @JsonProperty("mp_id")
    private List<String> mpId = new ArrayList<String>();
    @JsonProperty("doi_s")
    private List<String> doiS = new ArrayList<String>();
    @JsonProperty("chebi_pos")
    private List<String> chebiPos = new ArrayList<String>();
    @JsonProperty("vt_id")
    private List<String> vtId = new ArrayList<String>();
    @JsonProperty("bp_term")
    private List<String> bpTerm = new ArrayList<String>();
    @JsonProperty("chebi_term")
    private List<String> chebiTerm = new ArrayList<String>();
    @JsonProperty("p_date")
    private List<String> pDate = new ArrayList<String>();
    @JsonProperty("xco_term")
    private List<String> xcoTerm = new ArrayList<String>();
    @JsonProperty("chebi_count")
    private List<String> chebiCount = new ArrayList<String>();
    @JsonProperty("rs_term")
    private List<String> rsTerm = new ArrayList<String>();
    @JsonProperty("mp_term")
    private List<String> mpTerm = new ArrayList<String>();
    @JsonProperty("rdo_id")
    private List<String> rdoId = new ArrayList<String>();
    @JsonProperty("nbo_pos")
    private List<String> nboPos = new ArrayList<String>();
    @JsonProperty("gene")
    private List<String> gene = new ArrayList<String>();
    @JsonProperty("rs_id")
    private List<String> rsId = new ArrayList<String>();
    @JsonProperty("so_term")
    private List<String> soTerm = new ArrayList<String>();
    @JsonProperty("mp_count")
    private List<String> mpCount = new ArrayList<String>();
    @JsonProperty("vt_count")
    private List<String> vtCount = new ArrayList<String>();
    @JsonProperty("bp_id")
    private List<String> bpId = new ArrayList<String>();
    @JsonProperty("rgd_obj_count")
    private List<String> rgdObjCount = new ArrayList<String>();
    @JsonProperty("vt_pos")
    private List<String> vtPos = new ArrayList<String>();
    @JsonProperty("p_type")
    private List<String> pType = new ArrayList<String>();
    @JsonProperty("nbo_count")
    private List<String> nboCount = new ArrayList<String>();
    @JsonProperty("xco_id")
    private List<String> xcoId = new ArrayList<String>();
    @JsonProperty("p_year")
    private List<Integer> pYear = new ArrayList<Integer>();
    @JsonProperty("authors")
    private List<String> authors = new ArrayList<String>();
    @JsonProperty("xco_count")
    private List<String> xcoCount = new ArrayList<String>();
    @JsonProperty("rdo_count")
    private List<String> rdoCount = new ArrayList<String>();
    @JsonProperty("title")
    private List<String> title = new ArrayList<String>();
    @JsonProperty("nbo_term")
    private List<String> nboTerm = new ArrayList<String>();
    @JsonProperty("vt_term")
    private List<String> vtTerm = new ArrayList<String>();
    @JsonProperty("hp_pos")
    private List<String> hpPos = new ArrayList<String>();
    @JsonProperty("nbo_id")
    private List<String> nboId = new ArrayList<String>();
    @JsonProperty("so_count")
    private List<String> soCount = new ArrayList<String>();
    @JsonProperty("hp_term")
    private List<String> hpTerm = new ArrayList<String>();
    @JsonProperty("so_id")
    private List<String> soId = new ArrayList<String>();
    @JsonProperty("rgd_obj_pos")
    private List<String> rgdObjPos = new ArrayList<String>();
    @JsonProperty("xco_pos")
    private List<String> xcoPos = new ArrayList<String>();
    @JsonProperty("rs_pos")
    private List<String> rsPos = new ArrayList<String>();
    @JsonProperty("hp_id")
    private List<String> hpId = new ArrayList<String>();
    @JsonProperty("rdo_pos")
    private List<String> rdoPos = new ArrayList<String>();
    @JsonProperty("rs_count")
    private List<String> rsCount = new ArrayList<String>();
    @JsonProperty("rgd_obj_term")
    private List<String> rgdObjTerm = new ArrayList<String>();
    @JsonProperty("abstract")
    private List<String> _abstract = new ArrayList<String>();
    @JsonProperty("pmid")
    private List<String> pmid = new ArrayList<String>();
    @JsonProperty("bp_count")
    private List<String> bpCount = new ArrayList<String>();
    @JsonProperty("mp_pos")
    private List<String> mpPos = new ArrayList<String>();
    @JsonProperty("hp_count")
    private List<String> hpCount = new ArrayList<String>();
    @JsonProperty("xdb_id")
    private List<String> xdbId = new ArrayList<String>();
    @JsonProperty("rgd_obj_id")
    private List<String> rgdObjId = new ArrayList<String>();
    @JsonProperty("bp_pos")
    private List<String> bpPos = new ArrayList<String>();
    @JsonProperty("gene_pos")
    private List<String> genePos = new ArrayList<String>();
    @JsonProperty("so_pos")
    private List<String> soPos = new ArrayList<String>();
    @JsonProperty("rdo_term")
    private List<String> rdoTerm = new ArrayList<String>();
    @JsonProperty("chebi_id")
    private List<String> chebiId = new ArrayList<String>();
    @JsonProperty("j_date_s")
    private List<String> jDates= new ArrayList<String>();
    @JsonProperty("citation")
    private List<String> citation= new ArrayList<String>();
    @JsonProperty("mesh_terms")
    private List<String> meshTerms= new ArrayList<String>();
    @JsonProperty("keywords")
    private List<String> keywords= new ArrayList<String>();
    @JsonProperty("chemicals")
    private List<String> chemicals= new ArrayList<String>();
    @JsonProperty("affiliation")
    private List<String> affiliation= new ArrayList<String>();
    @JsonProperty("issn")
    private List<String> issn= new ArrayList<String>();
    @JsonProperty("organism_common_name")
    private List<String> organismCommonName= new ArrayList<String>();
    @JsonProperty("organism_term")
    private List<String> organismTerm= new ArrayList<String>();
    @JsonProperty("organism_ncbi_id")
    private List<String> organismNcbiId= new ArrayList<String>();
    @JsonProperty("organism_count")
    private List<String> organismCount= new ArrayList<String>();
    @JsonProperty("organism_pos")
    private List<String> organismPos= new ArrayList<String>();

    @JsonProperty("pmc_id")
    private List<String> pmc_id= new ArrayList<String>();
    @JsonProperty("gene_count")
    public List<String> getGeneCount() {
        return geneCount;
    }

    @JsonProperty("gene_count")
    public void setGeneCount(List<String> geneCount) {
        this.geneCount = geneCount;
    }

    public SolrDoc withGeneCount(List<String> geneCount) {
        this.geneCount = geneCount;
        return this;
    }

    @JsonProperty("mp_id")
    public List<String> getMpId() {
        return mpId;
    }

    @JsonProperty("mp_id")
    public void setMpId(List<String> mpId) {
        this.mpId = mpId;
    }

    public SolrDoc withMpId(List<String> mpId) {
        this.mpId = mpId;
        return this;
    }

    @JsonProperty("doi_s")
    public List<String> getDoiS() {
        return doiS;
    }

    @JsonProperty("doi_s")
    public void setDoiS(List<String> doiS) {
        this.doiS = doiS;
    }

    public SolrDoc withDoiS(List<String> doiS) {
        this.doiS = doiS;
        return this;
    }

    @JsonProperty("chebi_pos")
    public List<String> getChebiPos() {
        return chebiPos;
    }

    @JsonProperty("chebi_pos")
    public void setChebiPos(List<String> chebiPos) {
        this.chebiPos = chebiPos;
    }

    public SolrDoc withChebiPos(List<String> chebiPos) {
        this.chebiPos = chebiPos;
        return this;
    }

    @JsonProperty("vt_id")
    public List<String> getVtId() {
        return vtId;
    }

    @JsonProperty("vt_id")
    public void setVtId(List<String> vtId) {
        this.vtId = vtId;
    }

    public SolrDoc withVtId(List<String> vtId) {
        this.vtId = vtId;
        return this;
    }

    @JsonProperty("bp_term")
    public List<String> getBpTerm() {
        return bpTerm;
    }

    @JsonProperty("bp_term")
    public void setBpTerm(List<String> bpTerm) {
        this.bpTerm = bpTerm;
    }

    public SolrDoc withBpTerm(List<String> bpTerm) {
        this.bpTerm = bpTerm;
        return this;
    }

    @JsonProperty("chebi_term")
    public List<String> getChebiTerm() {
        return chebiTerm;
    }

    @JsonProperty("chebi_term")
    public void setChebiTerm(List<String> chebiTerm) {
        this.chebiTerm = chebiTerm;
    }

    public SolrDoc withChebiTerm(List<String> chebiTerm) {
        this.chebiTerm = chebiTerm;
        return this;
    }

    @JsonProperty("p_date")
    public List<String> getpDate() {
        return pDate;
    }

    @JsonProperty("p_date")
    public void setpDate(List<String> pDate) {
        this.pDate = pDate;
    }

    public SolrDoc withpDate(List<String> pDate) {
        this.pDate = pDate;
        return this;
    }

    @JsonProperty("xco_term")
    public List<String> getXcoTerm() {
        return xcoTerm;
    }

    @JsonProperty("xco_term")
    public void setXcoTerm(List<String> xcoTerm) {
        this.xcoTerm = xcoTerm;
    }

    public SolrDoc withXcoTerm(List<String> xcoTerm) {
        this.xcoTerm = xcoTerm;
        return this;
    }

    @JsonProperty("chebi_count")
    public List<String> getChebiCount() {
        return chebiCount;
    }

    @JsonProperty("chebi_count")
    public void setChebiCount(List<String> chebiCount) {
        this.chebiCount = chebiCount;
    }

    public SolrDoc withChebiCount(List<String> chebiCount) {
        this.chebiCount = chebiCount;
        return this;
    }

    @JsonProperty("rs_term")
    public List<String> getRsTerm() {
        return rsTerm;
    }

    @JsonProperty("rs_term")
    public void setRsTerm(List<String> rsTerm) {
        this.rsTerm = rsTerm;
    }

    public SolrDoc withRsTerm(List<String> rsTerm) {
        this.rsTerm = rsTerm;
        return this;
    }

    @JsonProperty("mp_term")
    public List<String> getMpTerm() {
        return mpTerm;
    }

    @JsonProperty("mp_term")
    public void setMpTerm(List<String> mpTerm) {
        this.mpTerm = mpTerm;
    }

    public SolrDoc withMpTerm(List<String> mpTerm) {
        this.mpTerm = mpTerm;
        return this;
    }

    @JsonProperty("rdo_id")
    public List<String> getRdoId() {
        return rdoId;
    }

    @JsonProperty("rdo_id")
    public void setRdoId(List<String> rdoId) {
        this.rdoId = rdoId;
    }

    public SolrDoc withRdoId(List<String> rdoId) {
        this.rdoId = rdoId;
        return this;
    }

    @JsonProperty("nbo_pos")
    public List<String> getNboPos() {
        return nboPos;
    }

    @JsonProperty("nbo_pos")
    public void setNboPos(List<String> nboPos) {
        this.nboPos = nboPos;
    }

    public SolrDoc withNboPos(List<String> nboPos) {
        this.nboPos = nboPos;
        return this;
    }

    @JsonProperty("gene")
    public List<String> getGene() {
        return gene;
    }

    @JsonProperty("gene")
    public void setGene(List<String> gene) {
        this.gene = gene;
    }

    public SolrDoc withGene(List<String> gene) {
        this.gene = gene;
        return this;
    }

    @JsonProperty("rs_id")
    public List<String> getRsId() {
        return rsId;
    }

    @JsonProperty("rs_id")
    public void setRsId(List<String> rsId) {
        this.rsId = rsId;
    }

    public SolrDoc withRsId(List<String> rsId) {
        this.rsId = rsId;
        return this;
    }

    @JsonProperty("so_term")
    public List<String> getSoTerm() {
        return soTerm;
    }

    @JsonProperty("so_term")
    public void setSoTerm(List<String> soTerm) {
        this.soTerm = soTerm;
    }

    public SolrDoc withSoTerm(List<String> soTerm) {
        this.soTerm = soTerm;
        return this;
    }

    @JsonProperty("mp_count")
    public List<String> getMpCount() {
        return mpCount;
    }

    @JsonProperty("mp_count")
    public void setMpCount(List<String> mpCount) {
        this.mpCount = mpCount;
    }

    public SolrDoc withMpCount(List<String> mpCount) {
        this.mpCount = mpCount;
        return this;
    }

    @JsonProperty("vt_count")
    public List<String> getVtCount() {
        return vtCount;
    }

    @JsonProperty("vt_count")
    public void setVtCount(List<String> vtCount) {
        this.vtCount = vtCount;
    }

    public SolrDoc withVtCount(List<String> vtCount) {
        this.vtCount = vtCount;
        return this;
    }

    @JsonProperty("bp_id")
    public List<String> getBpId() {
        return bpId;
    }

    @JsonProperty("bp_id")
    public void setBpId(List<String> bpId) {
        this.bpId = bpId;
    }

    public SolrDoc withBpId(List<String> bpId) {
        this.bpId = bpId;
        return this;
    }

    @JsonProperty("rgd_obj_count")
    public List<String> getRgdObjCount() {
        return rgdObjCount;
    }

    @JsonProperty("rgd_obj_count")
    public void setRgdObjCount(List<String> rgdObjCount) {
        this.rgdObjCount = rgdObjCount;
    }

    public SolrDoc withRgdObjCount(List<String> rgdObjCount) {
        this.rgdObjCount = rgdObjCount;
        return this;
    }

    @JsonProperty("vt_pos")
    public List<String> getVtPos() {
        return vtPos;
    }

    @JsonProperty("vt_pos")
    public void setVtPos(List<String> vtPos) {
        this.vtPos = vtPos;
    }

    public SolrDoc withVtPos(List<String> vtPos) {
        this.vtPos = vtPos;
        return this;
    }

    @JsonProperty("p_type")
    public List<String> getpType() {
        return pType;
    }

    @JsonProperty("p_type")
    public void setpType(List<String> pType) {
        this.pType = pType;
    }

    public SolrDoc withpType(List<String> pType) {
        this.pType = pType;
        return this;
    }

    @JsonProperty("nbo_count")
    public List<String> getNboCount() {
        return nboCount;
    }

    @JsonProperty("nbo_count")
    public void setNboCount(List<String> nboCount) {
        this.nboCount = nboCount;
    }

    public SolrDoc withNboCount(List<String> nboCount) {
        this.nboCount = nboCount;
        return this;
    }

    @JsonProperty("xco_id")
    public List<String> getXcoId() {
        return xcoId;
    }

    @JsonProperty("xco_id")
    public void setXcoId(List<String> xcoId) {
        this.xcoId = xcoId;
    }

    public SolrDoc withXcoId(List<String> xcoId) {
        this.xcoId = xcoId;
        return this;
    }

    @JsonProperty("p_year")
    public List<Integer> getpYear() {
        return pYear;
    }

    @JsonProperty("p_year")
    public void setpYear(List<Integer> pYear) {
        this.pYear = pYear;
    }

    public SolrDoc withpYear(List<Integer> pYear) {
        this.pYear = pYear;
        return this;
    }

    @JsonProperty("authors")
    public List<String> getAuthors() {
        return authors;
    }

    @JsonProperty("authors")
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public SolrDoc withAuthors(List<String> authors) {
        this.authors = authors;
        return this;
    }

    @JsonProperty("xco_count")
    public List<String> getXcoCount() {
        return xcoCount;
    }

    @JsonProperty("xco_count")
    public void setXcoCount(List<String> xcoCount) {
        this.xcoCount = xcoCount;
    }

    public SolrDoc withXcoCount(List<String> xcoCount) {
        this.xcoCount = xcoCount;
        return this;
    }

    @JsonProperty("rdo_count")
    public List<String> getRdoCount() {
        return rdoCount;
    }

    @JsonProperty("rdo_count")
    public void setRdoCount(List<String> rdoCount) {
        this.rdoCount = rdoCount;
    }

    public SolrDoc withRdoCount(List<String> rdoCount) {
        this.rdoCount = rdoCount;
        return this;
    }

    @JsonProperty("title")
    public List<String> getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(List<String> title) {
        this.title = title;
    }

    public SolrDoc withTitle(List<String> title) {
        this.title = title;
        return this;
    }

    @JsonProperty("nbo_term")
    public List<String> getNboTerm() {
        return nboTerm;
    }

    @JsonProperty("nbo_term")
    public void setNboTerm(List<String> nboTerm) {
        this.nboTerm = nboTerm;
    }

    public SolrDoc withNboTerm(List<String> nboTerm) {
        this.nboTerm = nboTerm;
        return this;
    }

    @JsonProperty("vt_term")
    public List<String> getVtTerm() {
        return vtTerm;
    }

    @JsonProperty("vt_term")
    public void setVtTerm(List<String> vtTerm) {
        this.vtTerm = vtTerm;
    }

    public SolrDoc withVtTerm(List<String> vtTerm) {
        this.vtTerm = vtTerm;
        return this;
    }

    @JsonProperty("hp_pos")
    public List<String> getHpPos() {
        return hpPos;
    }

    @JsonProperty("hp_pos")
    public void setHpPos(List<String> hpPos) {
        this.hpPos = hpPos;
    }

    public SolrDoc withHpPos(List<String> hpPos) {
        this.hpPos = hpPos;
        return this;
    }

    @JsonProperty("nbo_id")
    public List<String> getNboId() {
        return nboId;
    }

    @JsonProperty("nbo_id")
    public void setNboId(List<String> nboId) {
        this.nboId = nboId;
    }

    public SolrDoc withNboId(List<String> nboId) {
        this.nboId = nboId;
        return this;
    }

    @JsonProperty("so_count")
    public List<String> getSoCount() {
        return soCount;
    }

    @JsonProperty("so_count")
    public void setSoCount(List<String> soCount) {
        this.soCount = soCount;
    }

    public SolrDoc withSoCount(List<String> soCount) {
        this.soCount = soCount;
        return this;
    }

    @JsonProperty("hp_term")
    public List<String> getHpTerm() {
        return hpTerm;
    }

    @JsonProperty("hp_term")
    public void setHpTerm(List<String> hpTerm) {
        this.hpTerm = hpTerm;
    }

    public SolrDoc withHpTerm(List<String> hpTerm) {
        this.hpTerm = hpTerm;
        return this;
    }

    @JsonProperty("so_id")
    public List<String> getSoId() {
        return soId;
    }

    @JsonProperty("so_id")
    public void setSoId(List<String> soId) {
        this.soId = soId;
    }

    public SolrDoc withSoId(List<String> soId) {
        this.soId = soId;
        return this;
    }

    @JsonProperty("rgd_obj_pos")
    public List<String> getRgdObjPos() {
        return rgdObjPos;
    }

    @JsonProperty("rgd_obj_pos")
    public void setRgdObjPos(List<String> rgdObjPos) {
        this.rgdObjPos = rgdObjPos;
    }

    public SolrDoc withRgdObjPos(List<String> rgdObjPos) {
        this.rgdObjPos = rgdObjPos;
        return this;
    }

    @JsonProperty("xco_pos")
    public List<String> getXcoPos() {
        return xcoPos;
    }

    @JsonProperty("xco_pos")
    public void setXcoPos(List<String> xcoPos) {
        this.xcoPos = xcoPos;
    }

    public SolrDoc withXcoPos(List<String> xcoPos) {
        this.xcoPos = xcoPos;
        return this;
    }

    @JsonProperty("rs_pos")
    public List<String> getRsPos() {
        return rsPos;
    }

    @JsonProperty("rs_pos")
    public void setRsPos(List<String> rsPos) {
        this.rsPos = rsPos;
    }

    public SolrDoc withRsPos(List<String> rsPos) {
        this.rsPos = rsPos;
        return this;
    }

    @JsonProperty("hp_id")
    public List<String> getHpId() {
        return hpId;
    }

    @JsonProperty("hp_id")
    public void setHpId(List<String> hpId) {
        this.hpId = hpId;
    }

    public SolrDoc withHpId(List<String> hpId) {
        this.hpId = hpId;
        return this;
    }

    @JsonProperty("rdo_pos")
    public List<String> getRdoPos() {
        return rdoPos;
    }

    @JsonProperty("rdo_pos")
    public void setRdoPos(List<String> rdoPos) {
        this.rdoPos = rdoPos;
    }

    public SolrDoc withRdoPos(List<String> rdoPos) {
        this.rdoPos = rdoPos;
        return this;
    }

    @JsonProperty("rs_count")
    public List<String> getRsCount() {
        return rsCount;
    }

    @JsonProperty("rs_count")
    public void setRsCount(List<String> rsCount) {
        this.rsCount = rsCount;
    }

    public SolrDoc withRsCount(List<String> rsCount) {
        this.rsCount = rsCount;
        return this;
    }

    @JsonProperty("rgd_obj_term")
    public List<String> getRgdObjTerm() {
        return rgdObjTerm;
    }

    @JsonProperty("rgd_obj_term")
    public void setRgdObjTerm(List<String> rgdObjTerm) {
        this.rgdObjTerm = rgdObjTerm;
    }

    public SolrDoc withRgdObjTerm(List<String> rgdObjTerm) {
        this.rgdObjTerm = rgdObjTerm;
        return this;
    }

    @JsonProperty("abstract")
    public List<String> getAbstract() {
        return _abstract;
    }

    @JsonProperty("abstract")
    public void setAbstract(List<String> _abstract) {
        this._abstract = _abstract;
    }

    public SolrDoc withAbstract(List<String> _abstract) {
        this._abstract = _abstract;
        return this;
    }

    @JsonProperty("pmid")
    public List<String> getPmc_id() {
        return pmid;
    }

    @JsonProperty("pmid")
    public void setPmc_id(List<String> pmid) {
        this.pmid = pmid;
    }

    public SolrDoc withPmid(List<String> pmid) {
        this.pmid = pmid;
        return this;
    }
    @JsonProperty("pmid")
    public List<String> getPmid() {
        return pmid;
    }

    @JsonProperty("pmid")
    public void setPmid(List<String> pmid) {
        this.pmid = pmid;
    }
    @JsonProperty("bp_count")
    public List<String> getBpCount() {
        return bpCount;
    }

    @JsonProperty("bp_count")
    public void setBpCount(List<String> bpCount) {
        this.bpCount = bpCount;
    }

    public SolrDoc withBpCount(List<String> bpCount) {
        this.bpCount = bpCount;
        return this;
    }

    @JsonProperty("mp_pos")
    public List<String> getMpPos() {
        return mpPos;
    }

    @JsonProperty("mp_pos")
    public void setMpPos(List<String> mpPos) {
        this.mpPos = mpPos;
    }

    public SolrDoc withMpPos(List<String> mpPos) {
        this.mpPos = mpPos;
        return this;
    }

    @JsonProperty("hp_count")
    public List<String> getHpCount() {
        return hpCount;
    }

    @JsonProperty("hp_count")
    public void setHpCount(List<String> hpCount) {
        this.hpCount = hpCount;
    }

    public SolrDoc withHpCount(List<String> hpCount) {
        this.hpCount = hpCount;
        return this;
    }

    @JsonProperty("xdb_id")
    public List<String> getXdbId() {
        return xdbId;
    }

    @JsonProperty("xdb_id")
    public void setXdbId(List<String> xdbId) {
        this.xdbId = xdbId;
    }

    public SolrDoc withXdbId(List<String> xdbId) {
        this.xdbId = xdbId;
        return this;
    }

    @JsonProperty("rgd_obj_id")
    public List<String> getRgdObjId() {
        return rgdObjId;
    }

    @JsonProperty("rgd_obj_id")
    public void setRgdObjId(List<String> rgdObjId) {
        this.rgdObjId = rgdObjId;
    }

    public SolrDoc withRgdObjId(List<String> rgdObjId) {
        this.rgdObjId = rgdObjId;
        return this;
    }

    @JsonProperty("bp_pos")
    public List<String> getBpPos() {
        return bpPos;
    }

    @JsonProperty("bp_pos")
    public void setBpPos(List<String> bpPos) {
        this.bpPos = bpPos;
    }

    public SolrDoc withBpPos(List<String> bpPos) {
        this.bpPos = bpPos;
        return this;
    }

    @JsonProperty("gene_pos")
    public List<String> getGenePos() {
        return genePos;
    }

    @JsonProperty("gene_pos")
    public void setGenePos(List<String> genePos) {
        this.genePos = genePos;
    }

    public SolrDoc withGenePos(List<String> genePos) {
        this.genePos = genePos;
        return this;
    }

    @JsonProperty("so_pos")
    public List<String> getSoPos() {
        return soPos;
    }

    @JsonProperty("so_pos")
    public void setSoPos(List<String> soPos) {
        this.soPos = soPos;
    }

    public SolrDoc withSoPos(List<String> soPos) {
        this.soPos = soPos;
        return this;
    }

    @JsonProperty("rdo_term")
    public List<String> getRdoTerm() {
        return rdoTerm;
    }

    @JsonProperty("rdo_term")
    public void setRdoTerm(List<String> rdoTerm) {
        this.rdoTerm = rdoTerm;
    }

    public SolrDoc withRdoTerm(List<String> rdoTerm) {
        this.rdoTerm = rdoTerm;
        return this;
    }

    @JsonProperty("chebi_id")
    public List<String> getChebiId() {
        return chebiId;
    }

    @JsonProperty("chebi_id")
    public void setChebiId(List<String> chebiId) {
        this.chebiId = chebiId;
    }
//         "j_date_s",
//                 "citation",
//                 "mesh_terms",
//                 "keywords",
//                 "chemicals",
//                 "affilication",
//                 "issn",
//                 "organism_common_name",
//                 "organism_term",
//                 "organism_ncbi_id",
//                 "organism_count",
//                 "organism_pos",

    public SolrDoc withChebiId(List<String> chebiId) {
        this.chebiId = chebiId;
        return this;
    }
    @JsonProperty("j_date_s")
    public List<String> getjDates() {
        return jDates;
    }
    @JsonProperty("j_date_s")
    public void setjDates(List<String> jDates) {
        this.jDates = jDates;
    }
    @JsonProperty("citation")
    public List<String> getCitation() {
        return citation;
    }
    @JsonProperty("citation")
    public void setCitation(List<String> citation) {
        this.citation = citation;
    }
    @JsonProperty("mesh_terms")
    public List<String> getMeshTerms() {
        return meshTerms;
    }
    @JsonProperty("mesh_terms")
    public void setMeshTerms(List<String> meshTerms) {
        this.meshTerms = meshTerms;
    }
    @JsonProperty("keywords")
    public List<String> getKeywords() {
        return keywords;
    }
    @JsonProperty("keywords")
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    @JsonProperty("chemicals")
    public List<String> getChemicals() {
        return chemicals;
    }
    @JsonProperty("chemicals")
    public void setChemicals(List<String> chemicals) {
        this.chemicals = chemicals;
    }
    @JsonProperty("affiliation")
    public List<String> getAffiliation() {
        return affiliation;
    }
    @JsonProperty("affiliation")
    public void setAffiliation(List<String> affiliation) {
        this.affiliation = affiliation;
    }
    @JsonProperty("issn")
    public List<String> getIssn() {
        return issn;
    }
    @JsonProperty("issn")
    public void setIssn(List<String> issn) {
        this.issn = issn;
    }
    @JsonProperty("organism_common_name")
    public List<String> getOrganismCommonName() {
        return organismCommonName;
    }
    @JsonProperty("organism_common_name")
    public void setOrganismCommonName(List<String> organismCommonName) {
        this.organismCommonName = organismCommonName;
    }
    @JsonProperty("organism_term")
    public List<String> getOrganismTerm() {
        return organismTerm;
    }
    @JsonProperty("organism_term")
    public void setOrganismTerm(List<String> organismTerm) {
        this.organismTerm = organismTerm;
    }
    @JsonProperty("organism_ncbi_id")
    public List<String> getOrganismNcbiId() {
        return organismNcbiId;
    }
    @JsonProperty("organism_ncbi_id")
    public void setOrganismNcbiId(List<String> organismNcbiId) {
        this.organismNcbiId = organismNcbiId;
    }
    @JsonProperty("organism_count")
    public List<String> getOrganismCount() {
        return organismCount;
    }
    @JsonProperty("organism_count")
    public void setOrganismCount(List<String> organismCount) {
        this.organismCount = organismCount;
    }
    @JsonProperty("organism_pos")
    public List<String> getOrganismPos() {
        return organismPos;
    }
    @JsonProperty("organism_pos")
    public void setOrganismPos(List<String> organismPos) {
        this.organismPos = organismPos;
    }
//    @JsonAnyGetter
//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }
//
//    public SolrDoc withAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//        return this;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SolrDoc.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("geneCount");
        sb.append('=');
        sb.append(((this.geneCount == null)?"<null>":this.geneCount));
        sb.append(',');
        sb.append("mpId");
        sb.append('=');
        sb.append(((this.mpId == null)?"<null>":this.mpId));
        sb.append(',');
        sb.append("doiS");
        sb.append('=');
        sb.append(((this.doiS == null)?"<null>":this.doiS));
        sb.append(',');
        sb.append("chebiPos");
        sb.append('=');
        sb.append(((this.chebiPos == null)?"<null>":this.chebiPos));
        sb.append(',');
        sb.append("vtId");
        sb.append('=');
        sb.append(((this.vtId == null)?"<null>":this.vtId));
        sb.append(',');
        sb.append("bpTerm");
        sb.append('=');
        sb.append(((this.bpTerm == null)?"<null>":this.bpTerm));
        sb.append(',');
        sb.append("chebiTerm");
        sb.append('=');
        sb.append(((this.chebiTerm == null)?"<null>":this.chebiTerm));
        sb.append(',');
        sb.append("pDate");
        sb.append('=');
        sb.append(((this.pDate == null)?"<null>":this.pDate));
        sb.append(',');
        sb.append("xcoTerm");
        sb.append('=');
        sb.append(((this.xcoTerm == null)?"<null>":this.xcoTerm));
        sb.append(',');
        sb.append("chebiCount");
        sb.append('=');
        sb.append(((this.chebiCount == null)?"<null>":this.chebiCount));
        sb.append(',');
        sb.append("rsTerm");
        sb.append('=');
        sb.append(((this.rsTerm == null)?"<null>":this.rsTerm));
        sb.append(',');
        sb.append("mpTerm");
        sb.append('=');
        sb.append(((this.mpTerm == null)?"<null>":this.mpTerm));
        sb.append(',');
        sb.append("rdoId");
        sb.append('=');
        sb.append(((this.rdoId == null)?"<null>":this.rdoId));
        sb.append(',');
        sb.append("nboPos");
        sb.append('=');
        sb.append(((this.nboPos == null)?"<null>":this.nboPos));
        sb.append(',');
        sb.append("gene");
        sb.append('=');
        sb.append(((this.gene == null)?"<null>":this.gene));
        sb.append(',');
        sb.append("rsId");
        sb.append('=');
        sb.append(((this.rsId == null)?"<null>":this.rsId));
        sb.append(',');
        sb.append("soTerm");
        sb.append('=');
        sb.append(((this.soTerm == null)?"<null>":this.soTerm));
        sb.append(',');
        sb.append("mpCount");
        sb.append('=');
        sb.append(((this.mpCount == null)?"<null>":this.mpCount));
        sb.append(',');
        sb.append("vtCount");
        sb.append('=');
        sb.append(((this.vtCount == null)?"<null>":this.vtCount));
        sb.append(',');
        sb.append("bpId");
        sb.append('=');
        sb.append(((this.bpId == null)?"<null>":this.bpId));
        sb.append(',');
        sb.append("rgdObjCount");
        sb.append('=');
        sb.append(((this.rgdObjCount == null)?"<null>":this.rgdObjCount));
        sb.append(',');
        sb.append("vtPos");
        sb.append('=');
        sb.append(((this.vtPos == null)?"<null>":this.vtPos));
        sb.append(',');
        sb.append("pType");
        sb.append('=');
        sb.append(((this.pType == null)?"<null>":this.pType));
        sb.append(',');
        sb.append("nboCount");
        sb.append('=');
        sb.append(((this.nboCount == null)?"<null>":this.nboCount));
        sb.append(',');
        sb.append("xcoId");
        sb.append('=');
        sb.append(((this.xcoId == null)?"<null>":this.xcoId));
        sb.append(',');
        sb.append("pYear");
        sb.append('=');
        sb.append(((this.pYear == null)?"<null>":this.pYear));
        sb.append(',');
        sb.append("authors");
        sb.append('=');
        sb.append(((this.authors == null)?"<null>":this.authors));
        sb.append(',');
        sb.append("xcoCount");
        sb.append('=');
        sb.append(((this.xcoCount == null)?"<null>":this.xcoCount));
        sb.append(',');
        sb.append("rdoCount");
        sb.append('=');
        sb.append(((this.rdoCount == null)?"<null>":this.rdoCount));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("nboTerm");
        sb.append('=');
        sb.append(((this.nboTerm == null)?"<null>":this.nboTerm));
        sb.append(',');
        sb.append("vtTerm");
        sb.append('=');
        sb.append(((this.vtTerm == null)?"<null>":this.vtTerm));
        sb.append(',');
        sb.append("hpPos");
        sb.append('=');
        sb.append(((this.hpPos == null)?"<null>":this.hpPos));
        sb.append(',');
        sb.append("nboId");
        sb.append('=');
        sb.append(((this.nboId == null)?"<null>":this.nboId));
        sb.append(',');
        sb.append("soCount");
        sb.append('=');
        sb.append(((this.soCount == null)?"<null>":this.soCount));
        sb.append(',');
        sb.append("hpTerm");
        sb.append('=');
        sb.append(((this.hpTerm == null)?"<null>":this.hpTerm));
        sb.append(',');
        sb.append("soId");
        sb.append('=');
        sb.append(((this.soId == null)?"<null>":this.soId));
        sb.append(',');
        sb.append("rgdObjPos");
        sb.append('=');
        sb.append(((this.rgdObjPos == null)?"<null>":this.rgdObjPos));
        sb.append(',');
        sb.append("xcoPos");
        sb.append('=');
        sb.append(((this.xcoPos == null)?"<null>":this.xcoPos));
        sb.append(',');
        sb.append("rsPos");
        sb.append('=');
        sb.append(((this.rsPos == null)?"<null>":this.rsPos));
        sb.append(',');
        sb.append("hpId");
        sb.append('=');
        sb.append(((this.hpId == null)?"<null>":this.hpId));
        sb.append(',');
        sb.append("rdoPos");
        sb.append('=');
        sb.append(((this.rdoPos == null)?"<null>":this.rdoPos));
        sb.append(',');
        sb.append("rsCount");
        sb.append('=');
        sb.append(((this.rsCount == null)?"<null>":this.rsCount));
        sb.append(',');
        sb.append("rgdObjTerm");
        sb.append('=');
        sb.append(((this.rgdObjTerm == null)?"<null>":this.rgdObjTerm));
        sb.append(',');
        sb.append("_abstract");
        sb.append('=');
        sb.append(((this._abstract == null)?"<null>":this._abstract));
        sb.append(',');
        sb.append("pmid");
        sb.append('=');
        sb.append(((this.pmid == null)?"<null>":this.pmid));
        sb.append(',');
        sb.append("bpCount");
        sb.append('=');
        sb.append(((this.bpCount == null)?"<null>":this.bpCount));
        sb.append(',');
        sb.append("mpPos");
        sb.append('=');
        sb.append(((this.mpPos == null)?"<null>":this.mpPos));
        sb.append(',');
        sb.append("hpCount");
        sb.append('=');
        sb.append(((this.hpCount == null)?"<null>":this.hpCount));
        sb.append(',');
        sb.append("xdbId");
        sb.append('=');
        sb.append(((this.xdbId == null)?"<null>":this.xdbId));
        sb.append(',');
        sb.append("rgdObjId");
        sb.append('=');
        sb.append(((this.rgdObjId == null)?"<null>":this.rgdObjId));
        sb.append(',');
        sb.append("bpPos");
        sb.append('=');
        sb.append(((this.bpPos == null)?"<null>":this.bpPos));
        sb.append(',');
        sb.append("genePos");
        sb.append('=');
        sb.append(((this.genePos == null)?"<null>":this.genePos));
        sb.append(',');
        sb.append("soPos");
        sb.append('=');
        sb.append(((this.soPos == null)?"<null>":this.soPos));
        sb.append(',');
        sb.append("rdoTerm");
        sb.append('=');
        sb.append(((this.rdoTerm == null)?"<null>":this.rdoTerm));
        sb.append(',');
        sb.append("chebiId");
        sb.append('=');
        sb.append(((this.chebiId == null)?"<null>":this.chebiId));
        sb.append(',');
//        sb.append("additionalProperties");
//        sb.append('=');
//        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
//        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.vtPos == null)? 0 :this.vtPos.hashCode()));
        result = ((result* 31)+((this.chebiTerm == null)? 0 :this.chebiTerm.hashCode()));
        result = ((result* 31)+((this.vtCount == null)? 0 :this.vtCount.hashCode()));
        result = ((result* 31)+((this.mpPos == null)? 0 :this.mpPos.hashCode()));
        result = ((result* 31)+((this.rsId == null)? 0 :this.rsId.hashCode()));
        result = ((result* 31)+((this.nboId == null)? 0 :this.nboId.hashCode()));
        result = ((result* 31)+((this.rdoId == null)? 0 :this.rdoId.hashCode()));
        result = ((result* 31)+((this.rdoCount == null)? 0 :this.rdoCount.hashCode()));
        result = ((result* 31)+((this.pType == null)? 0 :this.pType.hashCode()));
        result = ((result* 31)+((this.pYear == null)? 0 :this.pYear.hashCode()));
        result = ((result* 31)+((this.soId == null)? 0 :this.soId.hashCode()));
        result = ((result* 31)+((this.gene == null)? 0 :this.gene.hashCode()));
        result = ((result* 31)+((this.mpCount == null)? 0 :this.mpCount.hashCode()));
        result = ((result* 31)+((this.nboCount == null)? 0 :this.nboCount.hashCode()));
        result = ((result* 31)+((this.bpId == null)? 0 :this.bpId.hashCode()));
        result = ((result* 31)+((this.rsPos == null)? 0 :this.rsPos.hashCode()));
        result = ((result* 31)+((this.soCount == null)? 0 :this.soCount.hashCode()));
        result = ((result* 31)+((this.rdoPos == null)? 0 :this.rdoPos.hashCode()));
        result = ((result* 31)+((this.nboTerm == null)? 0 :this.nboTerm.hashCode()));
        result = ((result* 31)+((this.vtTerm == null)? 0 :this.vtTerm.hashCode()));
        result = ((result* 31)+((this.mpId == null)? 0 :this.mpId.hashCode()));
        result = ((result* 31)+((this._abstract == null)? 0 :this._abstract.hashCode()));
//        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.xcoPos == null)? 0 :this.xcoPos.hashCode()));
        result = ((result* 31)+((this.authors == null)? 0 :this.authors.hashCode()));
        result = ((result* 31)+((this.bpPos == null)? 0 :this.bpPos.hashCode()));
        result = ((result* 31)+((this.hpPos == null)? 0 :this.hpPos.hashCode()));
        result = ((result* 31)+((this.rgdObjCount == null)? 0 :this.rgdObjCount.hashCode()));
        result = ((result* 31)+((this.rgdObjTerm == null)? 0 :this.rgdObjTerm.hashCode()));
        result = ((result* 31)+((this.bpCount == null)? 0 :this.bpCount.hashCode()));
        result = ((result* 31)+((this.genePos == null)? 0 :this.genePos.hashCode()));
        result = ((result* 31)+((this.rsTerm == null)? 0 :this.rsTerm.hashCode()));
        result = ((result* 31)+((this.title == null)? 0 :this.title.hashCode()));
        result = ((result* 31)+((this.vtId == null)? 0 :this.vtId.hashCode()));
        result = ((result* 31)+((this.soPos == null)? 0 :this.soPos.hashCode()));
        result = ((result* 31)+((this.nboPos == null)? 0 :this.nboPos.hashCode()));
        result = ((result* 31)+((this.bpTerm == null)? 0 :this.bpTerm.hashCode()));
        result = ((result* 31)+((this.chebiCount == null)? 0 :this.chebiCount.hashCode()));
        result = ((result* 31)+((this.xcoId == null)? 0 :this.xcoId.hashCode()));
        result = ((result* 31)+((this.chebiId == null)? 0 :this.chebiId.hashCode()));
        result = ((result* 31)+((this.hpTerm == null)? 0 :this.hpTerm.hashCode()));
        result = ((result* 31)+((this.chebiPos == null)? 0 :this.chebiPos.hashCode()));
        result = ((result* 31)+((this.rsCount == null)? 0 :this.rsCount.hashCode()));
        result = ((result* 31)+((this.rdoTerm == null)? 0 :this.rdoTerm.hashCode()));
        result = ((result* 31)+((this.soTerm == null)? 0 :this.soTerm.hashCode()));
        result = ((result* 31)+((this.rgdObjPos == null)? 0 :this.rgdObjPos.hashCode()));
        result = ((result* 31)+((this.pmid == null)? 0 :this.pmid.hashCode()));
        result = ((result* 31)+((this.hpCount == null)? 0 :this.hpCount.hashCode()));
        result = ((result* 31)+((this.xcoTerm == null)? 0 :this.xcoTerm.hashCode()));
        result = ((result* 31)+((this.xcoCount == null)? 0 :this.xcoCount.hashCode()));
        result = ((result* 31)+((this.geneCount == null)? 0 :this.geneCount.hashCode()));
        result = ((result* 31)+((this.rgdObjId == null)? 0 :this.rgdObjId.hashCode()));
        result = ((result* 31)+((this.hpId == null)? 0 :this.hpId.hashCode()));
        result = ((result* 31)+((this.pDate == null)? 0 :this.pDate.hashCode()));
        result = ((result* 31)+((this.xdbId == null)? 0 :this.xdbId.hashCode()));
        result = ((result* 31)+((this.doiS == null)? 0 :this.doiS.hashCode()));
        result = ((result* 31)+((this.mpTerm == null)? 0 :this.mpTerm.hashCode()));
        return result;
    }

//    @Override
//    public boolean equals(Object other) {
//        if (other == this) {
//            return true;
//        }
//        if ((other instanceof SolrDoc) == false) {
//            return false;
//        }
//        SolrDoc rhs = ((SolrDoc) other);
//        return ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((this.vtPos == rhs.vtPos)||((this.vtPos!= null)&&this.vtPos.equals(rhs.vtPos)))&&((this.chebiTerm == rhs.chebiTerm)||((this.chebiTerm!= null)&&this.chebiTerm.equals(rhs.chebiTerm))))&&((this.vtCount == rhs.vtCount)||((this.vtCount!= null)&&this.vtCount.equals(rhs.vtCount))))&&((this.mpPos == rhs.mpPos)||((this.mpPos!= null)&&this.mpPos.equals(rhs.mpPos))))&&((this.rsId == rhs.rsId)||((this.rsId!= null)&&this.rsId.equals(rhs.rsId))))&&((this.nboId == rhs.nboId)||((this.nboId!= null)&&this.nboId.equals(rhs.nboId))))&&((this.rdoId == rhs.rdoId)||((this.rdoId!= null)&&this.rdoId.equals(rhs.rdoId))))&&((this.rdoCount == rhs.rdoCount)||((this.rdoCount!= null)&&this.rdoCount.equals(rhs.rdoCount))))&&((this.pType == rhs.pType)||((this.pType!= null)&&this.pType.equals(rhs.pType))))&&((this.pYear == rhs.pYear)||((this.pYear!= null)&&this.pYear.equals(rhs.pYear))))&&((this.soId == rhs.soId)||((this.soId!= null)&&this.soId.equals(rhs.soId))))&&((this.gene == rhs.gene)||((this.gene!= null)&&this.gene.equals(rhs.gene))))&&((this.mpCount == rhs.mpCount)||((this.mpCount!= null)&&this.mpCount.equals(rhs.mpCount))))&&((this.nboCount == rhs.nboCount)||((this.nboCount!= null)&&this.nboCount.equals(rhs.nboCount))))&&((this.bpId == rhs.bpId)||((this.bpId!= null)&&this.bpId.equals(rhs.bpId))))&&((this.rsPos == rhs.rsPos)||((this.rsPos!= null)&&this.rsPos.equals(rhs.rsPos))))&&((this.soCount == rhs.soCount)||((this.soCount!= null)&&this.soCount.equals(rhs.soCount))))&&((this.rdoPos == rhs.rdoPos)||((this.rdoPos!= null)&&this.rdoPos.equals(rhs.rdoPos))))&&((this.nboTerm == rhs.nboTerm)||((this.nboTerm!= null)&&this.nboTerm.equals(rhs.nboTerm))))&&((this.vtTerm == rhs.vtTerm)||((this.vtTerm!= null)&&this.vtTerm.equals(rhs.vtTerm))))&&((this.mpId == rhs.mpId)||((this.mpId!= null)&&this.mpId.equals(rhs.mpId))))&&((this._abstract == rhs._abstract)||((this._abstract!= null)&&this._abstract.equals(rhs._abstract))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.xcoPos == rhs.xcoPos)||((this.xcoPos!= null)&&this.xcoPos.equals(rhs.xcoPos))))&&((this.authors == rhs.authors)||((this.authors!= null)&&this.authors.equals(rhs.authors))))&&((this.bpPos == rhs.bpPos)||((this.bpPos!= null)&&this.bpPos.equals(rhs.bpPos))))&&((this.hpPos == rhs.hpPos)||((this.hpPos!= null)&&this.hpPos.equals(rhs.hpPos))))&&((this.rgdObjCount == rhs.rgdObjCount)||((this.rgdObjCount!= null)&&this.rgdObjCount.equals(rhs.rgdObjCount))))&&((this.rgdObjTerm == rhs.rgdObjTerm)||((this.rgdObjTerm!= null)&&this.rgdObjTerm.equals(rhs.rgdObjTerm))))&&((this.bpCount == rhs.bpCount)||((this.bpCount!= null)&&this.bpCount.equals(rhs.bpCount))))&&((this.genePos == rhs.genePos)||((this.genePos!= null)&&this.genePos.equals(rhs.genePos))))&&((this.rsTerm == rhs.rsTerm)||((this.rsTerm!= null)&&this.rsTerm.equals(rhs.rsTerm))))&&((this.title == rhs.title)||((this.title!= null)&&this.title.equals(rhs.title))))&&((this.vtId == rhs.vtId)||((this.vtId!= null)&&this.vtId.equals(rhs.vtId))))&&((this.soPos == rhs.soPos)||((this.soPos!= null)&&this.soPos.equals(rhs.soPos))))&&((this.nboPos == rhs.nboPos)||((this.nboPos!= null)&&this.nboPos.equals(rhs.nboPos))))&&((this.bpTerm == rhs.bpTerm)||((this.bpTerm!= null)&&this.bpTerm.equals(rhs.bpTerm))))&&((this.chebiCount == rhs.chebiCount)||((this.chebiCount!= null)&&this.chebiCount.equals(rhs.chebiCount))))&&((this.xcoId == rhs.xcoId)||((this.xcoId!= null)&&this.xcoId.equals(rhs.xcoId))))&&((this.chebiId == rhs.chebiId)||((this.chebiId!= null)&&this.chebiId.equals(rhs.chebiId))))&&((this.hpTerm == rhs.hpTerm)||((this.hpTerm!= null)&&this.hpTerm.equals(rhs.hpTerm))))&&((this.chebiPos == rhs.chebiPos)||((this.chebiPos!= null)&&this.chebiPos.equals(rhs.chebiPos))))&&((this.rsCount == rhs.rsCount)||((this.rsCount!= null)&&this.rsCount.equals(rhs.rsCount))))&&((this.rdoTerm == rhs.rdoTerm)||((this.rdoTerm!= null)&&this.rdoTerm.equals(rhs.rdoTerm))))&&((this.soTerm == rhs.soTerm)||((this.soTerm!= null)&&this.soTerm.equals(rhs.soTerm))))&&((this.rgdObjPos == rhs.rgdObjPos)||((this.rgdObjPos!= null)&&this.rgdObjPos.equals(rhs.rgdObjPos))))&&((this.pmid == rhs.pmid)||((this.pmid!= null)&&this.pmid.equals(rhs.pmid))))&&((this.hpCount == rhs.hpCount)||((this.hpCount!= null)&&this.hpCount.equals(rhs.hpCount))))&&((this.xcoTerm == rhs.xcoTerm)||((this.xcoTerm!= null)&&this.xcoTerm.equals(rhs.xcoTerm))))&&((this.xcoCount == rhs.xcoCount)||((this.xcoCount!= null)&&this.xcoCount.equals(rhs.xcoCount))))&&((this.geneCount == rhs.geneCount)||((this.geneCount!= null)&&this.geneCount.equals(rhs.geneCount))))&&((this.rgdObjId == rhs.rgdObjId)||((this.rgdObjId!= null)&&this.rgdObjId.equals(rhs.rgdObjId))))&&((this.hpId == rhs.hpId)||((this.hpId!= null)&&this.hpId.equals(rhs.hpId))))&&((this.pDate == rhs.pDate)||((this.pDate!= null)&&this.pDate.equals(rhs.pDate))))&&((this.xdbId == rhs.xdbId)||((this.xdbId!= null)&&this.xdbId.equals(rhs.xdbId))))&&((this.doiS == rhs.doiS)||((this.doiS!= null)&&this.doiS.equals(rhs.doiS))))&&((this.mpTerm == rhs.mpTerm)||((this.mpTerm!= null)&&this.mpTerm.equals(rhs.mpTerm))));
//    }

}
