package com.andrewsummers.otashu;

public class Noteset {
	
	private long id;
	private String notevalues;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getNotevalues() {
		return notevalues;
	}
	
	public void setNotevalues(String notevalues) {
		this.notevalues = notevalues;
	}
	
	@Override
	public String toString() {
		return notevalues;
	}
}
