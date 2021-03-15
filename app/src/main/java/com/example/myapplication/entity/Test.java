package com.example.myapplication.entity;

import java.util.List;

public class Test {
    private Integer test_id;
    private String title;
    private String description;
    private Integer score;
    private String hard;
    private Integer quest_num;
    private String limit_time;
    private Integer limit_degree;
    private String deadline;
    private List<Question> questionList;

    public Test(){}
    public Test(Integer test_id, String title, String description, Integer score, String hard, Integer quest_num, String limit_time, Integer limit_degree, String deadline, List<Question> questionList) {
        this.test_id = test_id;
        this.title = title;
        this.description = description;
        this.score = score;
        this.hard = hard;
        this.quest_num = quest_num;
        this.limit_time = limit_time;
        this.limit_degree = limit_degree;
        this.deadline = deadline;
        this.questionList = questionList;
    }

    public String getLimit_time() {
        return limit_time;
    }

    public void setLimit_time(String limit_time) {
        this.limit_time = limit_time;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public Integer getTest_id() {
        return test_id;
    }

    public void setTest_id(Integer test_id) {
        this.test_id = test_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getHard() {
        return hard;
    }

    public void setHard(String hard) {
        this.hard = hard;
    }

    public Integer getQuest_num() {
        return quest_num;
    }

    public void setQuest_num(Integer quest_num) {
        this.quest_num = quest_num;
    }



    public Integer getLimit_degree() {
        return limit_degree;
    }

    public void setLimit_degree(Integer limit_degree) {
        this.limit_degree = limit_degree;
    }


}
