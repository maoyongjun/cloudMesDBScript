package org.foxconn.entity;

public class ALL_SYNONYMS {

    private String OWNER;
    private String SYNONYM_NAME;
    private String TABLE_OWNER;
    private String TABLE_NAME;
    public String getOWNER(){
        return OWNER;
     }
    public void setOWNER (String OWNER)
    {
        this.OWNER = OWNER;
    }
    public String getSYNONYM_NAME()
    {
        return SYNONYM_NAME;
    }

    public void setSYNONYM_NAME(String SYNONYM_NAME)
    {
        this.SYNONYM_NAME = SYNONYM_NAME;
    }
    public String getTABLE_OWNER()
    {
        return TABLE_OWNER;
    }

    public void setTABLE_OWNER(String TABLE_OWNER)
    {
        this.TABLE_OWNER = TABLE_OWNER;
    }
    public String getTABLE_NAME()
    {
        return TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME)
    {
        this.TABLE_NAME = TABLE_NAME;
    }
}