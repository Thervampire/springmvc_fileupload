package com.sxt.bean;

public class Attachment {
    private int fid;
    private String foldname;
    private String fnewname;
    private String fcontenttype;
    private String fpath;

    public Attachment() {
    }

    public Attachment(int fid) {
        this.fid = fid;
    }

    public Attachment(String foldname, String fnewname, String fcontenttype, String fpath) {
        this.foldname = foldname;
        this.fnewname = fnewname;
        this.fcontenttype = fcontenttype;
        this.fpath = fpath;
    }

    public Attachment(int fid, String foldname, String fnewname, String fcontenttype, String fpath) {
        this.fid = fid;
        this.foldname = foldname;
        this.fnewname = fnewname;
        this.fcontenttype = fcontenttype;
        this.fpath = fpath;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getFoldname() {
        return foldname;
    }

    public void setFoldname(String foldname) {
        this.foldname = foldname;
    }

    public String getFnewname() {
        return fnewname;
    }

    public void setFnewname(String fnewname) {
        this.fnewname = fnewname;
    }

    public String getFcontenttype() {
        return fcontenttype;
    }

    public void setFcontenttype(String fcontenttype) {
        this.fcontenttype = fcontenttype;
    }

    public String getFpath() {
        return fpath;
    }

    public void setFpath(String fpath) {
        this.fpath = fpath;
    }
}
