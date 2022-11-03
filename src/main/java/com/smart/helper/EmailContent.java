package com.smart.helper;

public class EmailContent {

	private String to;
	private String subject;
	private String content;
	public EmailContent(String to, String subject, String content) {
		super();
		this.to = to;
		this.subject = subject;
		this.content = content;
	}
	public EmailContent() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "EmailContent [to=" + to + ", subject=" + subject + ", content=" + content + "]";
	}
	
	
	
}
