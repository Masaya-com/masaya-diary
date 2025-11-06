package jp.co.sss.com.masaya.diary.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "diary_entry")
public class Diary_entry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	private Long id;
	
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
	@Column(name = "entry_date", nullable = false)
	private LocalDate entryDate;
	
	@Column(name = "todo_today", nullable = true)
    private String todoToday;
	
	@Column(name = "done_today", nullable = true)
	private String doneToday;
	
	@Column(name = "reflection" , nullable = true)
	private String reflection;
	
	@Column(name = "todo_tomorrow", nullable = true)
	private String todoTomorrow;
	
	@Column(name = "mood", nullable = true)
	private Integer mood;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}

	public String getTodoToday() {
		return todoToday;
	}

	public void setTodoToday(String todoToday) {
		this.todoToday = todoToday;
	}

	public String getDoneToday() {
		return doneToday;
	}

	public void setDoneToday(String doneToday) {
		this.doneToday = doneToday;
	}

	public String getReflection() {
		return reflection;
	}

	public void setReflection(String reflection) {
		this.reflection = reflection;
	}

	public String getTodoTomorrow() {
		return todoTomorrow;
	}

	public void setTodoTomorrow(String todoTomorrow) {
		this.todoTomorrow = todoTomorrow;
	}

	public Integer getMood() {
		return mood;
	}

	public void setMood(Integer mood) {
		this.mood = mood;
	}
	
	

}
