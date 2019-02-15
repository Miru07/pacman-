package main.pacman;

import java.sql.Date;

public class User {
    private String name;
    private Integer score;
    private Date resultDate;

    public User()
    {
        this.name = "";
        this.score = 0;
        this.resultDate = null;
    }

    public User(String name,Integer score, Date date)
    {
        this.name = name;
        this.score = score;
        this.resultDate = date;
    }

    /**
     * Getter for the user's name
     * @return user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the user's name
     * @param name user's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the score
     * @return score
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Setter for the score
     * @param score int that represents the score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Getter for the date
     * @return date
     */
    public Date getResultDate() {
        return resultDate;
    }

    /**
     * Setter for the date
     * @param resultDate date
     */
    public void setResultDate(Date resultDate) {
        this.resultDate = resultDate;
    }

}
