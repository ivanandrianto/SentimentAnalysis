/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentimentanalysis.utils.db;

import java.sql.Date;

/**
 *
 * @author ivan
 */
public class Post {
    private String user;
    private String content;
    private String link;
    private Date time;
            
    public Post(String username, String content, String link, Date time) {
        this.user = username;
        this.content = content;
        this.link = link;
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public String getLink() {
        return link;
    }

    public Date getTime() {
        return time;
    }

}