package Index;

public class Data{
	private String _id;
	private String html;
	private String text;
	private Integer id;
	private String title;
	
	public Data(){
	}
	public Data(String text, String _id, String title, String html, Integer id){
		this.text = text;
		this._id = _id;
		this.title = title;
		this.html = html;
		this.id = id;
		
	}
	public String get_ID(){
		return _id;
	}
	public void set_id(String _ID){
		this._id = _ID;
	}
	public String getHTML(){
		return html;
	}
	public void setHTML(String html){
		this.html = html;
	}
	public String getText(){
		return text;
	}
	public void setText(String text){
		this.text = text;
	}
	public Integer getID(){
		return id;
	}
	public void setID(Integer id){
		this.id = id;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	@Override
	public String toString(){
		return getID() + " " + get_ID();
	}
}