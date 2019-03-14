/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hhz.graphicaluserinterface.json2;

/**
 *
 * @author Valerij
 */
public class MainObject {
     private Created created;

    private String project;

    private String iteration;

    private String id;

    private Predictions[] predictions;

    public Created getCreated ()
    {
        return created;
    }

    public void setCreated (Created created)
    {
        this.created = created;
    }

    public String getProject ()
    {
        return project;
    }

    public void setProject (String project)
    {
        this.project = project;
    }

    public String getIteration ()
    {
        return iteration;
    }

    public void setIteration (String iteration)
    {
        this.iteration = iteration;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public Predictions[] getPredictions ()
    {
        return predictions;
    }

    public void setPredictions (Predictions[] predictions)
    {
        this.predictions = predictions;
    }

    @Override
    public String toString()
    {
        return "mainObject [created = "+created+", project = "+project+", iteration = "+iteration+", id = "+id+", predictions = "+predictions+"]";
    }
}
