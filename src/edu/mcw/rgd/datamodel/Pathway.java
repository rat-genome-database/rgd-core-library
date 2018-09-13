package edu.mcw.rgd.datamodel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pjayaraman
 * Date: Mar 28, 2011
 * Time: 4:35:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pathway {


    private String id;
    private String name;
    private String description;
    private String hasAlteredPath;
    private List<String> pathwayCategories;
    private List<Reference> referenceList;
    private List<PathwayObject> objectList;

    public List<Reference> getReferenceList() {
        return referenceList;
    }

    public void setReferenceList(List<Reference> referenceList) {
        this.referenceList = referenceList;
    }

    public String getHasAlteredPath() {
        return hasAlteredPath;
    }

    public void setHasAlteredPath(String hasAlteredPath) {
        this.hasAlteredPath = hasAlteredPath;
    }

    public List<PathwayObject> getObjectList() {
        return objectList;
    }

    public void setObjectList(List<PathwayObject> objectList) {
        this.objectList = objectList;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    public List<String> getPathwayCategories() {
        return pathwayCategories;
    }

    public void setPathwayCategories(List<String> pathwayCategories) {
        this.pathwayCategories = pathwayCategories;
    }
}
