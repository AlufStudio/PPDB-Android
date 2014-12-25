package app.rumus.ppdb.model;

public class CodeModel {
	int id;
	String code,tanggal;
	
	public CodeModel(){
		
	}
	
	public CodeModel(int id,String code,String tanggal){
		this.id = id;
		this.code = code;
		this.tanggal = tanggal;
	}
	
	public CodeModel(String code,String tanggal){
		this.code = code;
		this.tanggal = tanggal;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setCode(String code){
		this.code = code;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public void setTanggal(String tanggal){
		this.tanggal = tanggal;
	}
	
	public String getTanggal(){
		return this.tanggal;
	}
}
