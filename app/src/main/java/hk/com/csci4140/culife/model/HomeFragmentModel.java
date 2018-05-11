package hk.com.csci4140.culife.model;

/**
 * Created by maoyuxuan(Michael Mao) on 06/05/2018.
 */

public class HomeFragmentModel {

    private String title;//内容

    private String time;

    private String identity;

    private int id;//该条数据的id

    public String getTitle() {
        return title;
    }

    public String getTime() { return time;}

    public String getIdentity() { return identity;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {this.time = time;}

    public void setIdentity(String identity) {this.identity = identity;}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
