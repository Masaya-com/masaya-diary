package jp.co.sss.com.masaya.diary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_preferences")
public class Notification_preference {
	@Id
	@Column(name = "user_id", nullable = false)
	private Integer userId;
	
	@Column(name = "enable_webpush", nullable = false)
	private Boolean enableWebPush = true;
	
	@Column(name = "enable_email", nullable = false)
	private Boolean enableEmail = true;
	
	@Column(name = "notify_hour", nullable = false)
	private Integer notifyHour = 7;

	@Column(name = "last_notified_date")
	private java.time.LocalDate lastNotifiedDate;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getEnableWebPush() {
		return enableWebPush;
	}

	public void setEnableWebPush(Boolean enableWebPush) {
		this.enableWebPush = enableWebPush;
	}

	public Boolean getEnableEmail() {
		return enableEmail;
	}

	public void setEnableEmail(Boolean enableEmail) {
		this.enableEmail = enableEmail;
	}

	public Integer getNotifyHour() {
		return notifyHour;
	}

	public void setNotifyHour(Integer notifyHour) {
		this.notifyHour = notifyHour;
	}

	public java.time.LocalDate getLastNotifiedDate() {
		return lastNotifiedDate;
	}

	public void setLastNotifiedDate(java.time.LocalDate lastNotifiedDate) {
		this.lastNotifiedDate = lastNotifiedDate;
	}
}